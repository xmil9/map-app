package map;

import java.util.ArrayList;
import java.util.List;

import geometry.Point2D;
import geometry.Polygon2D;

// Defines a tile on the map.
public class MapTile {

	public final Point2D seed;
	public final Polygon2D shape;
	// Back-reference to the map object.
	private final Map map;
	// Array of indices into the map's node array. Ordered ccw.
	private List<Integer> nodes;
	// Array of indices into the map's tile array.
	private List<Integer> neighbors = new ArrayList<Integer>();
	
	public MapTile(Point2D seed, Polygon2D shape, Map map) {
		this.seed = seed;
		this.shape = shape;
		this.map = map;
	}
	
	public void setNodes(List<Integer> nodes) {
		this.nodes = nodes;
	}
	
	// Adds a tile given by its index as a neighboring tile.
	public void addNeighbor(int tileIdx) {
		if (!neighbors.contains(tileIdx))
			neighbors.add(tileIdx);
	}

	public int countNeighbors() {
		return neighbors.size();
	}

	// Returns the tile index (into the map's tile collection) of a neighboring
	// tile given by its index (into the neighbor collection of this tile).
	public int neighbor(int idx) {
		return neighbors.get(idx);
	}
}
