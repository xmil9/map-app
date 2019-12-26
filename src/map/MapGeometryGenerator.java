package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import geometry.Point2D;
import geometry.PoissonDiscSampling;
import geometry.Polygon2D;
import geometry.Rect2D;
import geometry.Triangle2D;
import geometry.VoronoiTesselation;
import geometry.VoronoiTile;
import math.MathUtil;
import types.Pair;


// Generates the layout of tiles for a map.
public class MapGeometryGenerator {

	public static class Spec {
		public final Rect2D bounds;
		// Minimal distance of randomly generated sample points.
		// Smaller distance => smaller and more map tiles.
		public final double minSampleDistance;
		// Number of candidates for generated sample points.
		// Larger number => more evenly spaced sample points but slower.
		public final int numSampleCandidates;
		
		public Spec(Rect2D bounds, double minSampleDist, int numCandidates) {
			this.bounds = bounds;
			this.minSampleDistance = minSampleDist;
			this.numSampleCandidates = numCandidates;
		}
	}

	///////////////
	
	private final Map map;
	private Map.Representation rep;
	private final Spec spec;
	private final Random rand;

	public MapGeometryGenerator(Map map, Spec spec, Random rand) {
		this.map = map;
		this.rep = new Map.Representation();
		this.spec = spec;
		this.rand = rand;
	}

	// Starts generating the geometry.
	public Map.Representation generate() {
		List<Point2D> seeds = generateTileSeeds(spec.bounds, spec.minSampleDistance,
				spec.numSampleCandidates, rand);
		makeMapGeometry(new VoronoiTesselation(seeds, spec.bounds));
		return rep;
	}
	
	// Generates tile seeds within given bounds.
	private static List<Point2D> generateTileSeeds(Rect2D bounds, double minSampleDist,
			int numCandidates, Random rand) {
		PoissonDiscSampling sampler =
				new PoissonDiscSampling(bounds, minSampleDist, numCandidates, rand);
		return sampler.generate();
	}
	
	// Constructs the map's geometry for a given tesselation of the mapped area.  
	private void makeMapGeometry(VoronoiTesselation tess) {
		List<VoronoiTile> tessTiles = tess.tesselate();
		makeMapTiles(tessTiles);
		populateTileNeighbors(tess.getTriangulation());
		populateNodeNeighbors(tessTiles);
	}
	
	// Constructs the tiles that the map is segmented into from given tiles of
	// a tesselation.
	private void makeMapTiles(List<VoronoiTile> tessTiles) {
		for (var tessTile : tessTiles)
			makeMapTile(tessTile);
	}
	
	// Constructs a map tile from a tesselation tile.
	private void makeMapTile(VoronoiTile tessTile) {
		MapTile tile = new MapTile(tessTile.seed, tessTile.outline, map);
		tile.setNodes(makeTileNodes(tessTile.outline));
		rep.addTile(tile);
	}
	
	// Contructs the map nodes that define map properties at each vertex of a
	// tile's shape.
	private List<Integer> makeTileNodes(Polygon2D shape) {
		List<Integer> tileNodes = new ArrayList<Integer>();
		
		for (int i = 0; i < shape.countVertices(); ++i) {
			Point2D pt = shape.vertex(i);
			
			// Check if a map node exists already at this point.
			int nodeIdx = -1;
			Pair<MapNode, Integer> node = rep.findNodeAt(pt);
			if (node == null)
				nodeIdx = rep.addNode(new MapNode(pt, map));
			else
				nodeIdx = node.b;
			
			tileNodes.add(nodeIdx);
		}
		
		return tileNodes;
	}
	
	// Populates the data structure that holds information about which tiles neighbor
	// each other.
	private void populateTileNeighbors(List<Triangle2D> triangulation) {
		// Each vertex of a triangle corresponds to a tile seed. The triangle edges
		// connect neighboring tiles. Mark the tiles of all triangle vertices as
		// connected to each other.
		for (var triangle : triangulation) {
			connectTilesAt(triangle.vertex(0), triangle.vertex(1));
			connectTilesAt(triangle.vertex(1), triangle.vertex(2));
			connectTilesAt(triangle.vertex(2), triangle.vertex(0));
		}
	}
	
	// Marks two map tiles at given locations as neighbors.
	private void connectTilesAt(Point2D a, Point2D b) {
		Pair<MapTile, Integer> tileA = rep.findTileAt(a);
		Pair<MapTile, Integer> tileB = rep.findTileAt(b);
		if (tileA != null && tileB != null) {
			tileA.a.addNeighbor(tileB.b);
			tileB.a.addNeighbor(tileA.b);
		}
	}
	
	// Populates the data structure that holds information about which nodes neighbor
	// each other.
	private void populateNodeNeighbors(List<VoronoiTile> tessTiles) {
		// Each vertex of a Voronoi tile's outline corresponds to a map node. The
		// border edges connect neighboring nodes. Mark the nodes of neighboring
		// vertices as connected to each other.
		for (var tessTile : tessTiles) {
			int numVertices = tessTile.outline.countVertices();
			for (int i = 0; i < numVertices; ++i) {
				Point2D vertex = tessTile.outline.vertex(i);
				Point2D next = tessTile.outline.vertex(
						MathUtil.cyclicNext(i, numVertices));
				connectNodesAt(vertex, next);
			}
		}
	}
	
	// Marks two map nodes at given locations as neighbors.
	private void connectNodesAt(Point2D a, Point2D b) {
		Pair<MapNode, Integer> nodeA = rep.findNodeAt(a);
		Pair<MapNode, Integer> nodeB = rep.findNodeAt(b);
		if (nodeA != null && nodeB != null) {
			nodeA.a.addNeighbor(nodeB.b);
			nodeB.a.addNeighbor(nodeA.b);
		}
	}
}
