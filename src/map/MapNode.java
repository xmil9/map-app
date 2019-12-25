package map;

import java.util.ArrayList;
import java.util.List;

import geometry.Point2D;

// Holds map properties for a location on the map.
public class MapNode {

	private final Point2D pos;
	private double elevation;
	// Back-reference to the map object.
	private final Map map;
	// Array of indices into the map's node array.
	private List<Integer> neighbors = new ArrayList<Integer>();
	
	public MapNode(Point2D pos, Map map) {
		this.pos = pos;
		this.map = map;
	}
	
	public void addNeighbor(int nodeIdx) {
		if (!neighbors.contains(nodeIdx))
			neighbors.add(nodeIdx);
	}
}
