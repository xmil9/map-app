package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import geometry.Point2D;
import geometry.Polygon2D;
import geometry.Rect2D;
import types.Pair;

public class Map {

	public static class Spec {
		public final MapGeometryGenerator.Spec geom;
		public final MapTopographyGenerator.Spec topo;
		
		public Spec(MapGeometryGenerator.Spec geom, MapTopographyGenerator.Spec topo) {
			this.geom = geom;
			this.topo = topo;
		}
	}
	
	///////////////
	
	// Data structures that hold the map data.
	// Kept in separate class to allow generator objects to work with it more esily.
	public static class Representation {
		// Tiles that map is made from.
		private List<MapTile> tiles;
		// Lookup of tile index by its seed location.
		private java.util.Map<Point2D, Integer> tileLookup;
		// Unique nodes defining the shape of all tiles. A node shared between tiles
		// is only listed once.
		private List<MapNode> nodes;
		// Lookup of node index by its location.
		private java.util.Map<Point2D, Integer> nodeLookup;
		
		public Representation() {
			tiles = new ArrayList<MapTile>();
			tileLookup = new TreeMap<Point2D, Integer>(Point2D.makeXYComparator());
			nodes = new ArrayList<MapNode>();
			nodeLookup = new TreeMap<Point2D, Integer>(Point2D.makeXYComparator());
		}
		
		// Adds a given tile.
		public int addTile(MapTile tile) {
			tiles.add(tile);
			int tileIdx = tiles.size() - 1;
			tileLookup.put(tile.seed, tileIdx);
			return tileIdx;
		}
		
		public int countTiles() {
			return tiles.size();
		}
		
		public MapTile tile(int idx) {
			return tiles.get(idx);
		}
		
		// Returns the tile and its index whose seed is located at a given position.
		public Pair<MapTile, Integer> findTileAt(Point2D pos) {
			Integer idx = tileLookup.get(pos);
			if (idx == null)
				return null;
			return new Pair<MapTile, Integer>(tiles.get(idx), idx);
		}

		// Adds a given node.
		public int addNode(MapNode node) {
			nodes.add(node);
			int nodeIdx = nodes.size() - 1;
			nodeLookup.put(node.pos, nodeIdx);
			return nodeIdx;
		}
		
		public int countNodes() {
			return nodes.size();
		}
		
		public MapNode node(int idx) {
			return nodes.get(idx);
		}
		
		// Returns the node and its index that is located at a given position.
		public Pair<MapNode, Integer> findNodeAt(Point2D pos) {
			Integer idx = nodeLookup.get(pos);
			if (idx == null)
				return null;
			return new Pair<MapNode, Integer>(nodes.get(idx), idx);
		}
	}
	
	///////////////
	
	private final Spec spec;
	private final Random rand;
	private Representation rep;

	public Map(Spec spec, Random rand) {
		this.spec = spec;
		this.rand = rand;
		this.rep = new Representation();
	}

	// Generates the map tiles.
	public void generate() {
		generateGeometry();
		generateTopography();
	}

	// Returns the number of tiles in the map.
	public int countTiles() {
		return rep.tiles.size();
	}

	// Returns the shapes of all tiles.
	public List<Polygon2D> tileShapes() {
		List<Polygon2D> shapes = new ArrayList<Polygon2D>(rep.tiles.size());
		for (var tile : rep.tiles)
			shapes.add(tile.shape);
		return shapes;
	}

	// Generates the tile layout of the map.
	private void generateGeometry() {
		MapGeometryGenerator gen = new MapGeometryGenerator(this, spec.geom);
		rep = gen.generate(rand);
	}
	
	// Generates the node elevations.
	private void generateTopography() {
		MapTopographyGenerator gen = new MapTopographyGenerator(rep, spec.topo, rand);
		gen.generate();
	}
}
