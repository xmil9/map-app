package map;

import geometry.Point2D;

// Collects min and max elevation values.
public class ExtremeElevationFinder {

	public double max = -2;
	public Point2D maxPos = null;
	public double min = 2;
	public Point2D minPos = null;
	
	// Finds min/max elevation for given tile.
	public void find(MapTile tile) {
		max = -2;
		maxPos = null;
		min = 2;
		minPos = null;
		
		int numNodes = tile.countNodes();
		for (int i = 0; i < numNodes; ++i) {
			MapNode node = tile.node(i);
			double elev = node.elevation();
			if (elev > max) {
				max = elev;
				maxPos = node.pos;
			}
			if (elev < min) {
				min = elev;
				minPos = node.pos;
			}
		}
		
		if (max > 1)
			max = 1;
		if (min < -1)
			min = -1;
	}
}
