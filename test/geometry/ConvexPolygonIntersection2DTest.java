package geometry;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ConvexPolygonIntersection2DTest {

	private static List<Point2D> makeOctagon(double x, double y, double size) {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(x, y));
		path.add(new Point2D(x - size, y + size));
		path.add(new Point2D(x - size, y + 2 * size));
		path.add(new Point2D(x, y + 3 * size));
		path.add(new Point2D(x + size, y + 3 * size));
		path.add(new Point2D(x + 2 * size, y + 2 * size));
		path.add(new Point2D(x + 2 * size, y + size));
		path.add(new Point2D(x + size, y));
		return path;
	}

	private static List<Point2D> makeRect(double x, double y, double sizeX,
			double sizeY) {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(x, y));
		path.add(new Point2D(x, y + sizeY));
		path.add(new Point2D(x + sizeX, y + sizeY));
		path.add(new Point2D(x + sizeX, y));
		return path;
	}

	private static List<Point2D> makeDiamond(double x, double y, double size) {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(x, y));
		path.add(new Point2D(x - size, y + size));
		path.add(new Point2D(x, y + 2 * size));
		path.add(new Point2D(x + size, y + size));
		return path;
	}

	@Test
	public void intersect_ForEmptyPolygon() {
		Polygon2D P = new Polygon2D();

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonWithSinglePointOutsideTheOther() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(10, 10));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonWithSinglePointInsideTheOther() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(2, 0));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(2, 0));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonWithSinglePointOnTheOthersPath() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(4, 6));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(4, 6));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonLineOutsideTheOther() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(-1, -5));
		P.addVertex(new Point2D(8, 0));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonLineFullyInsideTheOther() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(1.1, -0.8));
		P.addVertex(new Point2D(2.5, 0.5));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(1.1, -0.8));
		expected.addVertex(new Point2D(2.5, 0.5));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonLineIntersectingTheOtherTwice() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(0, 0));
		P.addVertex(new Point2D(7, 4));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(1.2894736842, 0.736842105));
		expected.addVertex(new Point2D(2.916666667, 1.666666667));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonLineIntersectingTheOtherOnceWithStartInside() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(1.5, 1));
		P.addVertex(new Point2D(7, 4));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(1.5, 1));
		expected.addVertex(new Point2D(2.947368421, 1.7894736842));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonLineIntersectingTheOtherOnceWithEndInside() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(0, 0));
		P.addVertex(new Point2D(3, 5));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(1.6153846153, 2.692307692));
		expected.addVertex(new Point2D(3, 5));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonLineIntersectingTheOtherOnceWithStartOnEdge() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(1, -1));
		P.addVertex(new Point2D(7, 4));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(1, -1));
		expected.addVertex(new Point2D(2.578947368, 0.315789473));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonLineIntersectingTheOtherOnceWithEndOnEdge() {
		Polygon2D P = new Polygon2D();
		P.addVertex(new Point2D(0, 0));
		P.addVertex(new Point2D(3, 2));

		Polygon2D Q = new Polygon2D();
		Q.addVertex(new Point2D(1, -1));
		Q.addVertex(new Point2D(2, 5));
		Q.addVertex(new Point2D(4, 6));
		Q.addVertex(new Point2D(3, 2));
		Q.addVertex(new Point2D(2, -2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(1.3125, 0.875));
		expected.addVertex(new Point2D(3, 2));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForNotIntersectingPolygons() {
		Polygon2D P = new Polygon2D(makeOctagon(2, 2, 2));
		Polygon2D Q = new Polygon2D(makeRect(10, 10, 3, 3));

		Polygon2D expected = new Polygon2D();

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonInsideOther() {
		Polygon2D P = new Polygon2D(makeOctagon(2, 2, 10));
		Polygon2D Q = new Polygon2D(makeRect(4, 4, 2, 2));

		Polygon2D expected = Q;

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForIntersectingPolygonsCase1() {
		Polygon2D P = new Polygon2D(makeOctagon(2, 2, 2));
		Polygon2D Q = new Polygon2D(makeRect(-1, 3, 8, 4));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(1, 7));
		expected.addVertex(new Point2D(5, 7));
		expected.addVertex(new Point2D(6, 6));
		expected.addVertex(new Point2D(6, 4));
		expected.addVertex(new Point2D(5, 3));
		expected.addVertex(new Point2D(1, 3));
		expected.addVertex(new Point2D(0, 4));
		expected.addVertex(new Point2D(0, 6));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForIntersectingPolygonsCase2() {
		Polygon2D P = new Polygon2D(makeOctagon(2, 2, 2));
		Polygon2D Q = new Polygon2D(makeDiamond(2.5, 4, 3));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(0.25, 6.25));
		expected.addVertex(new Point2D(2, 8));
		expected.addVertex(new Point2D(4, 8));
		expected.addVertex(new Point2D(5.25, 6.75));
		expected.addVertex(new Point2D(2.5, 4));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForIntersectingPolygonsCase3() {
		Polygon2D P = new Polygon2D(makeOctagon(2, 2, 2));
		Polygon2D Q = new Polygon2D(makeDiamond(3, 1, 3));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(0, 4));
		expected.addVertex(new Point2D(3, 7));
		expected.addVertex(new Point2D(6, 4));
		expected.addVertex(new Point2D(4, 2));
		expected.addVertex(new Point2D(2, 2));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonsTouchingAtEdge() {
		Polygon2D P = new Polygon2D(makeOctagon(2, 2, 2));
		Polygon2D Q = new Polygon2D(makeDiamond(-1, -1, 3));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(0, 4));
		expected.addVertex(new Point2D(2, 2));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}

	@Test
	public void intersect_ForPolygonsTouchingAtPoint() {
		Polygon2D P = new Polygon2D(makeOctagon(2, 2, 2));
		Polygon2D Q = new Polygon2D(makeRect(5, 1, 2, 2));

		Polygon2D expected = new Polygon2D();
		expected.addVertex(new Point2D(5, 3));

		assertEquals(expected, ConvexPolygonIntersection2D.intersect(P, Q));
		assertEquals(expected, ConvexPolygonIntersection2D.intersect(Q, P));
	}
}
