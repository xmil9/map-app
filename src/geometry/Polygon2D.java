package geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


// A closed polygon shape.
public class Polygon2D extends Object {
	
	private List<Point2D> vertices;
	
	public Polygon2D() {
		vertices = new ArrayList<Point2D>();
	}
	
	public Polygon2D(Point2D pt) {
		this();
		vertices.add(pt);
	}
	
	public Polygon2D(Collection<Point2D> points) {
		vertices = new ArrayList<Point2D>(points);
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		Polygon2D otherPoly = (Polygon2D) other;
		return vertices.equals(otherPoly.vertices);
	}

	@Override
	public int hashCode() {
		return vertices.hashCode();
	}
	
	public int countVertices() {
		return vertices.size();
	}
	
	public Point2D vertex(int idx) {
		return vertices.get(idx);
	}
	
	public void setVertex(int idx, Point2D pt) {
		vertices.set(idx, pt);
	}
	
	// Checks if a given point is a vertex of the polygon.
	public boolean hasVertex(Point2D pt) {
		for (Point2D v : vertices)
			if (v.equals(pt))
				return true;
		return false;
	}

	public void addVertex(Point2D pt) {
		vertices.add(pt);
	}
	
	public int countEdges() {
		if (vertices.size() == 1)
			return 0;
		return vertices.size();
	}
	
	public LineSegment2D edge(int idx) {
		if (idx == countEdges() - 1)
			return new LineSegment2D(vertices.get(idx), vertices.get(0));
		return new LineSegment2D(vertices.get(idx), vertices.get(idx + 1));
	}
	
	public boolean isConvex() {
		return GeometryUtil.isConvexPath(vertices);
	}
}
