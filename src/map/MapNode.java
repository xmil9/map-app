package map;

import java.util.ArrayList;
import java.util.List;

import geometry.Point2D;

// Holds map properties for a location on the map.
public class MapNode {

	public final Point2D pos;
	private double elevation = -1;
	// Back-reference to the map object.
	private final Map map;
	// Array of indices into the map's node array.
	private List<Integer> neighbors = new ArrayList<Integer>();
	
	public MapNode(Point2D pos, Map map) {
		this.pos = pos;
		this.map = map;
	}
	
	// Adds a node given by its index as a neighboring node.
	public void addNeighbor(int nodeIdx) {
		if (!neighbors.contains(nodeIdx))
			neighbors.add(nodeIdx);
	}

	public int countNeighbors() {
		return neighbors.size();
	}

	// Returns the node index (into the map's node collection) of a neighboring
	// node given by its index (into the neighbor collection of this node).
	public int neighbor(int idx) {
		return neighbors.get(idx);
	}
	
	public double elevation() {
		return elevation;
	}
	
	public void setElevation(double elevation) {
		this.elevation = elevation;
	}
}
