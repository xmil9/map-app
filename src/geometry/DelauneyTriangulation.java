package geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


// Implementation of Bowyer-Watson algorithm to perform a Delauney triangulation
// in 2D.
// Triangulates a set of points so that each resulting triangle's cirumcircle
// has an empty interior, i.e. does not contain any of the other points. This
// is called the 'Delauney condition'.
// Source:
// http://paulbourke.net/papers/triangulate/
public class DelauneyTriangulation {
	
	// Data structure to hold individual edges of triangles. 
	private static class EdgeBuffer {
		private List<LineSegment2D> edges = new ArrayList<LineSegment2D>();

		void addEdges(DelauneyTriangle t) {
			for (int j = 0; j <= 2; ++j)
				edges.add(t.edge(j));
		}
		
		int size() {
			return edges.size();
		}
		
		LineSegment2D get(int idx) {
			return edges.get(idx);
		}
		
		void clear() {
			edges.clear();
		}
		
		void removeDuplicates() {
			Set<Integer> duplicates = new HashSet<Integer>();
			for (int i = 0; i < edges.size(); ++i) {
				for (int j = i + 1; j < edges.size(); ++j) {
					if (isDuplicateEdge(edges.get(i), edges.get(j))) {
						// Remove both duplicates!
						duplicates.add(i);
						duplicates.add(j);
					}
				}
			}
			
			List<Integer> sorted = new ArrayList<Integer>(duplicates); 
	        Collections.sort(sorted);
	        
	        for (int i = sorted.size() - 1; i >= 0; --i)
	        	edges.remove((int)sorted.get(i));
	    }
		
		private boolean isDuplicateEdge(LineSegment2D a, LineSegment2D b) {
			Point2D sa = a.startPoint(); 
			Point2D ea = a.endPoint(); 
			Point2D sb = b.startPoint(); 
			Point2D eb = b.endPoint(); 
			return (sa.equals(sb) && ea.equals(eb)) ||
					(sa.equals(eb) && ea.equals(sb));
		}
	}
	
	// List of points that define the triangulation.
	private final List<Point2D> samples;
	// Triangle that bounds all input points.
	private final Triangle2D boundingTriangle;
	// Current state of the triangulation. Holds triangles and some data that
	// they are annotated with.
	private List<DelauneyTriangle> triangulation =
			new ArrayList<DelauneyTriangle>();
	
	public DelauneyTriangulation(Set<Point2D> samplePoints) {
		samples = new ArrayList<Point2D>(samplePoints);
		// Determine the bounding triangle.
		boundingTriangle = calcBoundingTriangle(samples);
		
		// Add bounding triangle vertices to the end of the vertex list.
		if (!boundingTriangle.isDegenerate()) {
			samples.add(boundingTriangle.vertex(0));
			samples.add(boundingTriangle.vertex(1));
			samples.add(boundingTriangle.vertex(2));
		}
	}
	
	// Starts the Delauney triangulation.
	public List<Triangle2D> triangulate() {
		if (boundingTriangle.isDegenerate())
			return new ArrayList<Triangle2D>();
		
		try {
			triangulation.add(new DelauneyTriangle(boundingTriangle));
			EdgeBuffer edges = new EdgeBuffer();
			
			for (Point2D sample : samples) {
				edges.clear();
				findEnclosingPolygonEdges(sample, triangulation, edges);
				edges.removeDuplicates();
				generateNewTriangles(sample, edges, triangulation);
			}
			
			removeTrianglesSharingVertices(triangulation, boundingTriangle);
		} catch (GeometryException e) {
			// Abort the triangulation.
			triangulation.clear();
		}
		
		return prepareResult(triangulation);
	}
	
	// Returns the triangulation extended with information that the algorithm
	// cached for each triangle, e.g. a triangle's circumcircle. Access is given
	// as optimization, so that callers don't need to recalculate data that the
	// algorithm already calculated.
	public List<DelauneyTriangle> delauneyTriangles() {
		return triangulation;
	}
	
	// Calculates a triangle that encloses the given points. The points need to
	// be completely inside the triangle, not just on its outline.
	private static Triangle2D calcBoundingTriangle(List<Point2D> points) {
		Rect2D bounds = GeometryUtil.calcBoundingBox(points);
		if (bounds.isDegenerate())
			return new Triangle2D();
		
		double dimMax = Math.max(bounds.width(), bounds.height());
		Point2D center = bounds.center();
		final double SCALE = 20.0;

		Point2D a = new Point2D(center.x - SCALE * dimMax, center.y - dimMax);
		Point2D b = new Point2D(center.x, center.y + SCALE *dimMax);
		Point2D c = new Point2D(center.x + SCALE * dimMax, center.y - dimMax);
		return new Triangle2D(a, b, c);
	}
	
	// Adds the edges of given triangles whose circumcircle contains a given sample
	// point to a given edge buffer and removes the triangles from the given list.
	private static void findEnclosingPolygonEdges(Point2D sample,
			List<DelauneyTriangle> triangles, EdgeBuffer edges) {
		int i = 0;
		while (i < triangles.size()) {
			DelauneyTriangle t = triangles.get(i); 
			if (t.isPointInCircumcircle(sample)) {
				edges.addEdges(t);
				triangles.remove(i);
				// Since we removed the triangle we don't need to increase
				// the loop counter.
			} else {
				++i;
			}
		}
	}
	
	// For each given edge generate a new triangle with a given sample point and store
	// it in a given triangle list.
	private static void generateNewTriangles(Point2D sample,
			EdgeBuffer edges, List<DelauneyTriangle> triangles) {
		for (int i = 0; i < edges.size(); ++i) {
			LineSegment2D e = edges.get(i);
			try {
				Triangle2D t = new Triangle2D(sample, e.startPoint(), e.endPoint());
				// Skip triangles that are lines or points.
				if (!t.isDegenerate())
					triangles.add(new DelauneyTriangle(t));
			} catch (GeometryException ex) {
				// Skip this triangle.
			}
		}
	}
	
	// Removes triangles from a given list that share vertices with a given master
	// triangle.
	private static void removeTrianglesSharingVertices(
			List<DelauneyTriangle> triangles, Triangle2D master) {
		int i = 0;
		while (i < triangles.size()) {
			DelauneyTriangle t = triangles.get(i);
			
			boolean wasRemoved = false;
			for (int j = 0; j < 3; ++j) {
				if (isVertexOf(t.vertex(j), master)) {
					triangles.remove(i);
					wasRemoved = true;
					break;
				}
			}
			
			if (!wasRemoved)
				++i;
		}
	}
	
	// Checks if a given point is a vertex of a given triangle.
	private static boolean isVertexOf(Point2D vertex, Triangle2D t) {
		for (int i = 0; i < 3; ++i)
			if (vertex.equals(t.vertex(i)))
				return true;
		return false;
	}
	
	// Returns a list of plain triangles from a given list of Delauney triangles.
	private static List<Triangle2D> prepareResult(
			List<DelauneyTriangle> delauneyTriangles) {
		List<Triangle2D> result = new ArrayList<Triangle2D>();
		for (DelauneyTriangle dt : delauneyTriangles)
			result.add(dt.triangle());
		return result;
	}
	
	// Checks whether a given list of triangles satisfies the Delauney condition,
	// i.e. each triangle's cirumcircle has an empty interior (does not contain
	// any of the other triangles corner points).
	public static boolean isDelauneyConditionSatisfied(List<Triangle2D> triangles) {
		Set<Point2D> vertices = collectPoints(triangles);
		for (Triangle2D t : triangles) {
			try {
				Circle2D ccircle = t.calcCircumcircle();
				for (Point2D p : vertices) {
					if (ccircle.isPointInsideCircle(p))
						return false;
				}
			} catch (GeometryException e) {
				// Skip triangle.
			}
		}
		
		return true;
	}
	
	// Returns a set of all unique vertices of a given list of triangles.
	private static Set<Point2D> collectPoints(List<Triangle2D> triangles) {
		Set<Point2D> vertices = new HashSet<Point2D>();
		for (Triangle2D t : triangles)
			for (int i = 0; i < 3; ++i)
				vertices.add(t.vertex(i));
		return vertices;
	}
}
