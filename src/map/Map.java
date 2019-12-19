package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import geometry.Point2D;
import geometry.PoissonDiscSampling;
import geometry.Polygon2D;
import geometry.Rect2D;
import geometry.Triangle2D;
import geometry.VoronoiTile;
import geometry.VoronoiTesselation;

public class Map {

	private final Rect2D bounds;
	// Tiles that map is made from.
	private List<MapTile> tiles;
	// Lookup of tile index by its seed location.
	private java.util.Map<Point2D, Integer> tileLookup;
	// Unique nodes defining the shape of all tiles. A node shared between tiles
	// is only listed once.
	private List<MapNode> nodes;
	// Lookup of node index by its location.
	private java.util.Map<Point2D, Integer> nodeLookup;

	public Map(Rect2D bounds) {
		this.bounds = bounds;
		this.tiles = new ArrayList<MapTile>();
		this.tileLookup = new HashMap<Point2D, Integer>();
		this.nodes = new ArrayList<MapNode>();
		this.nodeLookup = new HashMap<Point2D, Integer>();
	}

	// Generates the map tiles.
	public void generate() {
		List<Point2D> seeds = generateTileSeeds(bounds);
		constructMapData(new VoronoiTesselation(seeds, bounds));
	}

	// Returns the number of tiles in the map.
	public int countTiles() {
		return tiles.size();
	}

	// Returns the shapes of all tiles.
	public List<Polygon2D> tileShapes() {
		List<Polygon2D> shapes = new ArrayList<Polygon2D>(tiles.size());
		for (var tile : tiles)
			shapes.add(tile.shape);
		return shapes;
	}

	// Generates tile seeds within given bounds.
	private static List<Point2D> generateTileSeeds(Rect2D bounds) {
		final double MIN_SAMPLE_DIST = 0.5;
		final int NUM_CANDIDATES = 30;
		PoissonDiscSampling sampler =
				new PoissonDiscSampling(bounds, MIN_SAMPLE_DIST, NUM_CANDIDATES);
		return sampler.generate();
	}
	
	// Constructs the map's data structures for a given tesselation of the mapped area.  
	private void constructMapData(VoronoiTesselation tess) {
		constructTiles(tess.tesselate());
		populateTileNeighbors(tess.getTriangulation());
	}
	
	// Constructs the tiles that the map is segmented into from given tiles of
	// a tesselation.
	private void constructTiles(List<VoronoiTile> tessTiles) {
		tiles = new ArrayList<MapTile>(tessTiles.size());
		for (var tessTile : tessTiles)
			constructTile(tessTile);
	}
	
	// Constructs a map tile from a tesselation tile.
	private void constructTile(VoronoiTile tessTile) {
		MapTile tile = new MapTile(tessTile.seed, tessTile.outline, this);
		tile.setNodes(constructTileNodes(tessTile.outline));
		
		tiles.add(tile);
		Integer tileIdx = tiles.size() - 1;
		tileLookup.put(tessTile.seed, tileIdx);
	}
	
	// Contructs the map nodes that define map properties at each vertex of a
	// tile's shape.
	private List<Integer> constructTileNodes(Polygon2D shape) {
		List<Integer> tileNodes = new ArrayList<Integer>();
		
		for (int i = 0; i < shape.countVertices(); ++i) {
			Point2D pt = shape.vertex(i);
			
			// Check if a map node exists already at this point.
			Integer nodeIdx = nodeLookup.get(pt);
			if (nodeIdx == null) {
				// Add new node.
				nodes.add(new MapNode(pt));
				nodeIdx = nodes.size() - 1;
				nodeLookup.put(pt, nodeIdx);
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
		for (var triangle : triangulation) {
			Point2D seedA = triangle.vertex(0);
			Integer tileIdxA = tileLookup.get(seedA);
			MapTile tileA = tiles.get(tileIdxA);
			Point2D seedB = triangle.vertex(1);
			Integer tileIdxB = tileLookup.get(seedB);
			MapTile tileB = tiles.get(tileIdxB);
			Point2D seedC = triangle.vertex(2);
			Integer tileIdxC = tileLookup.get(seedC);
			MapTile tileC = tiles.get(tileIdxC);

			tileA.addNeighbor(tileIdxB);
			tileA.addNeighbor(tileIdxC);
			tileB.addNeighbor(tileIdxA);
			tileB.addNeighbor(tileIdxC);
			tileC.addNeighbor(tileIdxA);
			tileC.addNeighbor(tileIdxB);
		}
	}
}
