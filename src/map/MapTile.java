package map;

import java.util.ArrayList;
import java.util.List;

import geometry.Point2D;
import geometry.Polygon2D;

// Defines a tile on the map.
public class MapTile extends Object {

	public final Point2D seed;
	public final Polygon2D shape;
	// Nodes for each point in the tile's shape. Ordered ccw.
	private List<MapNode> nodes;
	// Neighboring tiles.
	private List<MapTile> neighbors = new ArrayList<MapTile>();
	
	public MapTile(Point2D seed, Polygon2D shape) {
		this.seed = seed;
		this.shape = shape;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		MapTile otherTile = (MapTile) other;
		return seed.equals(otherTile.seed);
	}
	
	public void setNodes(List<MapNode> nodes) {
		this.nodes = nodes;
	}
	
	// Adds a given tile as a neighboring tile.
	public void addNeighbor(MapTile neighbor) {
		if (!neighbors.contains(neighbor))
			neighbors.add(neighbor);
	}

	public int countNeighbors() {
		return neighbors.size();
	}

	// Returns a neighboring tile.
	public MapTile neighbor(int idx) {
		return neighbors.get(idx);
	}

	public int countNodes() {
		return nodes.size();
	}

	// Returns a node in the tile's outline.
	public MapNode node(int idx) {
		return nodes.get(idx);
	}
}
