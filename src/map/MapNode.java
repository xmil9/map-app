package map;

import geometry.Point2D;

// Holds map properties for a location on the map.
public class MapNode {

	private Point2D pos;
	private double elevation;
	
	public MapNode(Point2D pos) {
		this.pos = pos;
	}
}
