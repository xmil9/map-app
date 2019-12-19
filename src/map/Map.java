package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import geometry.Point2D;
import geometry.Polygon2D;
import geometry.Rect2D;

public class Map {

	// Data structures that hold the map data.
	// Kept in separate class to allow generator objects to work with it.
	public static class Representation {
		// Tiles that map is made from.
		public List<MapTile> tiles;
		// Lookup of tile index by its seed location.
		public java.util.Map<Point2D, Integer> tileLookup;
		// Unique nodes defining the shape of all tiles. A node shared between tiles
		// is only listed once.
		public List<MapNode> nodes;
		// Lookup of node index by its location.
		public java.util.Map<Point2D, Integer> nodeLookup;
		
		public Representation() {
			tiles = new ArrayList<MapTile>();
			tileLookup = new HashMap<Point2D, Integer>();
			nodes = new ArrayList<MapNode>();
			nodeLookup = new HashMap<Point2D, Integer>();
		}
	}
	
	///////////////
	
	private final Rect2D bounds;
	private Representation rep;

	public Map(Rect2D bounds) {
		this.bounds = bounds;
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
		MapGeometryGenerator gen = new MapGeometryGenerator(bounds, this);
		rep = gen.generate();
	}
	
	// Generates the node elevations.
	private void generateTopography() {
		MapTopographyGenerator.Spec spec = new MapTopographyGenerator.Spec();
		MapTopographyGenerator gen = new MapTopographyGenerator(rep, spec);
		gen.generate();
	}
}
