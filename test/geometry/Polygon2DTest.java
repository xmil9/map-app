package geometry;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class Polygon2DTest {

	@Test
	public void construct_Default() {
		Polygon2D poly = new Polygon2D();
		assertEquals(0, poly.countVertices());
		assertEquals(0, poly.countEdges());
	}

	@Test
	public void construct_WithOnePoint() {
		Polygon2D poly = new Polygon2D(new Point2D(1, 2));
		assertEquals(1, poly.countVertices());
		assertEquals(0, poly.countEdges());
	}

	@Test
	public void construct_WithPoints() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		assertEquals(4, poly.countVertices());
		assertEquals(4, poly.countEdges());
	}

	@Test
	public void equals_WithSelf() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		assertTrue(poly.equals(poly));
	}

	@Test
	public void equals_WithNull() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		assertFalse(poly.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		assertFalse(poly.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly1 = new Polygon2D(points);
		Polygon2D poly2 = new Polygon2D(points);
		assertTrue(poly1.equals(poly2));
	}

	@Test
	public void equals_WhenInequal() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly1 = new Polygon2D(points);

		List<Point2D> otherPoints = new ArrayList<Point2D>();
		otherPoints.add(new Point2D(1, 2));
		otherPoints.add(new Point2D(2, -2));
		otherPoints.add(new Point2D(7, 8));
		otherPoints.add(new Point2D(4, -2));
		Polygon2D poly2 = new Polygon2D(otherPoints);
		
		assertFalse(poly1.equals(poly2));
	}

	@Test
	public void countVertices() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		assertEquals(4, poly.countVertices());
	}

	@Test
	public void countVertices_ForNoVertex() {
		List<Point2D> points = new ArrayList<Point2D>();
		Polygon2D poly = new Polygon2D(points);
		assertEquals(0, poly.countVertices());
	}

	@Test
	public void vertex() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		assertEquals(points.get(0), poly.vertex(0));
		assertEquals(points.get(1), poly.vertex(1));
		assertEquals(points.get(2), poly.vertex(2));
		assertEquals(points.get(3), poly.vertex(3));
	}

	@Test
	public void setVertex() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);

		Point2D otherPt = new Point2D(100, 100);
		poly.setVertex(0, otherPt);
		
		assertEquals(otherPt, poly.vertex(0));
	}

	@Test
	public void hasVertex_WhenPolygonHasThePoint() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		
		assertTrue(poly.hasVertex(points.get(0).copy()));
		assertTrue(poly.hasVertex(points.get(2).copy()));
	}

	@Test
	public void hasVertex_WhenPolygonDoesNotHaveThePoint() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		
		assertFalse(poly.hasVertex(new Point2D(7, 7)));
	}

	@Test
	public void addVertex() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);

		Point2D added = new Point2D(0,  1);
		poly.addVertex(added);
		
		assertEquals(added, poly.vertex(4));
	}

	@Test
	public void addVertex_ForFirstVertex() {
		Polygon2D poly = new Polygon2D();
		Point2D added = new Point2D(0,  1);
		poly.addVertex(added);
		assertEquals(added, poly.vertex(0));
	}

	@Test
	public void insertVertex_AtBeginning() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);

		Point2D added = new Point2D(0,  1);
		poly.insertVertex(added, 0);
		
		assertEquals(5, poly.countVertices());
		assertEquals(added, poly.vertex(0));
	}

	@Test
	public void insertVertex_InMiddle() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);

		Point2D added = new Point2D(0,  1);
		poly.insertVertex(added, 2);
		
		assertEquals(5, poly.countVertices());
		assertEquals(added, poly.vertex(2));
	}

	@Test
	public void insertVertex_AtEnd() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);

		Point2D added = new Point2D(0,  1);
		poly.insertVertex(added, 4);
		
		assertEquals(5, poly.countVertices());
		assertEquals(added, poly.vertex(4));
	}
	
	@Test
	public void countEdges() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		assertEquals(4, poly.countEdges());
	}

	@Test
	public void countEdges_ForOneVertex() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		Polygon2D poly = new Polygon2D(points);
		assertEquals(0, poly.countEdges());
	}

	@Test
	public void countEdges_ForNoVertex() {
		List<Point2D> points = new ArrayList<Point2D>();
		Polygon2D poly = new Polygon2D(points);
		assertEquals(0, poly.countEdges());
	}
	
	@Test
	public void edge() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		
		assertEquals(new LineSegment2D(points.get(0), points.get(1)), poly.edge(0));
		assertEquals(new LineSegment2D(points.get(1), points.get(2)), poly.edge(1));
		assertEquals(new LineSegment2D(points.get(2), points.get(3)), poly.edge(2));
		assertEquals(new LineSegment2D(points.get(3), points.get(0)), poly.edge(3));
	}
	
	@Test
	public void bounds() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, -2));
		points.add(new Point2D(3, 2));
		points.add(new Point2D(4, -2));
		Polygon2D poly = new Polygon2D(points);
		
		assertEquals(new Rect2D(1, -2, 4, 2), poly.bounds());
	}
	
	@Test
	public void bounds_ForNoVertex() {
		Polygon2D poly = new Polygon2D();
		assertEquals(new Rect2D(), poly.bounds());
	}
	
	@Test
	public void bounds_ForOneVertex() {
		Point2D pt = new Point2D(1, 2);
		Polygon2D poly = new Polygon2D(pt);
		assertEquals(new Rect2D(pt, pt), poly.bounds());
	}

	@Test
	public void reversed() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(3, 0));
		path.add(new Point2D(4, -2));
		path.add(new Point2D(2, -3));
		Polygon2D poly = new Polygon2D(path);
		
		Polygon2D rev = poly.reversed();
		
		assertEquals(path.get(0), rev.vertex(rev.countVertices() - 1));
		assertEquals(path.get(1), rev.vertex(rev.countVertices() - 2));
		assertEquals(path.get(2), rev.vertex(rev.countVertices() - 3));
		assertEquals(path.get(3), rev.vertex(rev.countVertices() - 4));
	}

	@Test
	public void isConvex_ForConvexPolygon() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(3, 0));
		path.add(new Point2D(4, -2));
		path.add(new Point2D(2, -3));
		Polygon2D poly = new Polygon2D(path);
		assertTrue(poly.isConvex());
	}

	@Test
	public void isConvex_ForConcavePolygon() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 0));
		path.add(new Point2D(3, 1));
		path.add(new Point2D(4, 3));
		path.add(new Point2D(2, 2));
		path.add(new Point2D(0, 5));
		Polygon2D poly = new Polygon2D(path);
		assertFalse(poly.isConvex());
	}

	@Test
	public void isPointInsideConvexPolygon_ForEmptyPolygon() {
		Polygon2D poly = new Polygon2D();
		assertFalse(Polygon2D.isPointInsideConvexPolygon(new Point2D(1, 2), poly));
	}

	@Test
	public void isPointInsideConvexPolygon_ForPolygonWithOneVertex() {
		Polygon2D poly = new Polygon2D(new Point2D(1, 2));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(1, 2), poly));
		assertFalse(Polygon2D.isPointInsideConvexPolygon(new Point2D(2, 2), poly));
	}

	@Test
	public void isPointInsideConvexPolygon_ForPolygonWithTwoVertices() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 1));
		path.add(new Point2D(2, 2));
		Polygon2D poly = new Polygon2D(path);
		
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(1.5, 1.5), poly));
		assertFalse(Polygon2D.isPointInsideConvexPolygon(new Point2D(2, 3), poly));
	}

	@Test
	public void isPointInsideConvexPolygon_ForCcwConvexPolygon() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(3, 0));
		path.add(new Point2D(4, -2));
		path.add(new Point2D(2, -3));
		Polygon2D poly = new Polygon2D(path);
		
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(2, -1), poly));
		assertFalse(Polygon2D.isPointInsideConvexPolygon(new Point2D(2, 3), poly));
	}

	@Test
	public void isPointInsideConvexPolygon_ForCwConvexPolygon() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(2, -3));
		path.add(new Point2D(4, -2));
		path.add(new Point2D(3, 0));
		Polygon2D poly = new Polygon2D(path);
		
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(2, -1), poly));
		assertFalse(Polygon2D.isPointInsideConvexPolygon(new Point2D(2, 3), poly));
	}

	private static List<Point2D> makeOctagon() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(6, 8));
		path.add(new Point2D(8, 8));
		path.add(new Point2D(10, 6));
		path.add(new Point2D(10, 4));
		path.add(new Point2D(8, 2));
		path.add(new Point2D(6, 2));
		path.add(new Point2D(4, 4));
		path.add(new Point2D(4, 6));
		return path;
	}

	@Test
	public void isPointInsideConvexPolygon_ForPointOnEdge() {
		Polygon2D poly = new Polygon2D(makeOctagon());
		
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(7, 8), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(9, 7), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(10, 5), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(9, 3), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(7, 2), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(5, 5), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(4, 5), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(5, 7), poly));
	}

	@Test
	public void isPointInsideConvexPolygon_ForPointOnVertex() {
		Polygon2D poly = new Polygon2D(makeOctagon());
		
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(6, 8), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(8, 8), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(10, 6), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(10, 4), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(8, 2), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(6, 2), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(4, 4), poly));
		assertTrue(Polygon2D.isPointInsideConvexPolygon(new Point2D(4, 6), poly));
	}
}
