package map;

import java.util.ArrayList;
import java.util.List;

import geometry.Point2D;
import geometry.PoissonDiscSampling;
import geometry.Polygon2D;
import geometry.Rect2D;
import geometry.Triangle2D;
import geometry.VoronoiTesselation;
import geometry.VoronoiTile;


// Generates the layout of tiles for a map.
public class MapGeometryGenerator {

	private final Rect2D bounds;
	private final Map map;
	private Map.Representation rep;

	public MapGeometryGenerator(Rect2D bounds, Map map) {
		this.bounds = bounds;
		this.map = map;
		this.rep = new Map.Representation();
	}

	// Starts generating the geometry.
	public Map.Representation generate() {
		List<Point2D> seeds = generateTileSeeds(bounds);
		makeMapGeometry(new VoronoiTesselation(seeds, bounds));
		return rep;
	}
	
	// Generates tile seeds within given bounds.
	private static List<Point2D> generateTileSeeds(Rect2D bounds) {
		final double MIN_SAMPLE_DIST = 1.0;
		final int NUM_CANDIDATES = 30;
		PoissonDiscSampling sampler =
				new PoissonDiscSampling(bounds, MIN_SAMPLE_DIST, NUM_CANDIDATES);
		return sampler.generate();
	}
	
	// Constructs the map's geometry for a given tesselation of the mapped area.  
	private void makeMapGeometry(VoronoiTesselation tess) {
		makeMapTiles(tess.tesselate());
		populateTileNeighbors(tess.getTriangulation());
	}
	
	// Constructs the tiles that the map is segmented into from given tiles of
	// a tesselation.
	private void makeMapTiles(List<VoronoiTile> tessTiles) {
		rep.tiles = new ArrayList<MapTile>(tessTiles.size());
		for (var tessTile : tessTiles)
			makeMapTile(tessTile);
	}
	
	// Constructs a map tile from a tesselation tile.
	private void makeMapTile(VoronoiTile tessTile) {
		MapTile tile = new MapTile(tessTile.seed, tessTile.outline, map);
		tile.setNodes(makeTileNodes(tessTile.outline));
		
		rep.tiles.add(tile);
		Integer tileIdx = rep.tiles.size() - 1;
		rep.tileLookup.put(tessTile.seed, tileIdx);
	}
	
	// Contructs the map nodes that define map properties at each vertex of a
	// tile's shape.
	private List<Integer> makeTileNodes(Polygon2D shape) {
		List<Integer> tileNodes = new ArrayList<Integer>();
		
		for (int i = 0; i < shape.countVertices(); ++i) {
			Point2D pt = shape.vertex(i);
			
			// Check if a map node exists already at this point.
			Integer nodeIdx = rep.nodeLookup.get(pt);
			if (nodeIdx == null) {
				// Add new node.
				rep.nodes.add(new MapNode(pt));
				nodeIdx = rep.nodes.size() - 1;
				rep.nodeLookup.put(pt, nodeIdx);
			}
			
			tileNodes.add(nodeIdx);
		}
		
		return tileNodes;
	}
	
	// Populates the data structure that holds information about which tiles neighbor
	// each other.
	private void populateTileNeighbors(List<Triangle2D> triangulation) {
		// Each vertex of the triangle corresponds to a tile seed. The triangle edges
		// connect neighboring tiles. Mark the tiles of all triangle vertices as
		// connected to each other.
		Point2D[] seed = new Point2D[3];
		Integer[] tileIdx = new Integer[3];
		MapTile[] tile = new MapTile[3];
		
		for (var triangle : triangulation) {
			for (int i = 0; i < 3; ++i) {
				seed[i] = triangle.vertex(i);
				tileIdx[i] = rep.tileLookup.get(seed[i]);
				tile[i] = rep.tiles.get(tileIdx[i]);
			}
			for (int i = 0; i < 3; ++i) {
				int next = (i < 2) ? i + 1 : 0;
				tile[i].addNeighbor(tileIdx[next]);
				tile[i].addNeighbor(tileIdx[(next < 2) ? next + 1 : 0]);
			}			
		}
	}
}
