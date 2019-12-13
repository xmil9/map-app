package geometry;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


public class GeometryUtilTest {

	@Test
	public void calcBoundingBox_ForNoPoints() {
		Rect2D bounds = GeometryUtil.calcBoundingBox(new ArrayList<Point2D>());
		assertEquals(0, bounds.left(), 0.0);
		assertEquals(0, bounds.top(), 0.0);
		assertEquals(0, bounds.right(), 0.0);
		assertEquals(0, bounds.bottom(), 0.0);
	}

	@Test
	public void calcBoundingBox_ForOnePoint() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(2, 3));
		Rect2D bounds = GeometryUtil.calcBoundingBox(points);
		assertEquals(2, bounds.left(), 0.0);
		assertEquals(3, bounds.top(), 0.0);
		assertEquals(2, bounds.right(), 0.0);
		assertEquals(3, bounds.bottom(), 0.0);
	}

	@Test
	public void calcBoundingBox_ForMultiplePoints() {
		List<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(2, 3));
		points.add(new Point2D(2.3, 3.6));
		points.add(new Point2D(-3.4, 7.8));
		points.add(new Point2D(0, 2));
		points.add(new Point2D(1.9, 9.1));
		Rect2D bounds = GeometryUtil.calcBoundingBox(points);
		assertEquals(-3.4, bounds.left(), 0.0);
		assertEquals(2.0, bounds.top(), 0.0);
		assertEquals(2.3, bounds.right(), 0.0);
		assertEquals(9.1, bounds.bottom(), 0.0);
	}

	@Test
	public void calcBoundingBox_ForPointArray() {
		Point2D[] points = new Point2D[] {
				new Point2D(2, 3),
				new Point2D(2.3, 3.6),
				new Point2D(-3.4, 7.8),
				new Point2D(0, 2),
				new Point2D(1.9, 9.1),
		};
		Rect2D bounds = GeometryUtil.calcBoundingBox(points);
		assertEquals(-3.4, bounds.left(), 0.0);
		assertEquals(2.0, bounds.top(), 0.0);
		assertEquals(2.3, bounds.right(), 0.0);
		assertEquals(9.1, bounds.bottom(), 0.0);
	}

	@Test
	public void isConvexPath_ForNoPoints() {
		assertTrue(GeometryUtil.isConvexPath(new ArrayList<Point2D>()));
	}

	@Test
	public void isConvexPath_ForOnePoints() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		assertTrue(GeometryUtil.isConvexPath(path));
	}

	@Test
	public void isConvexPath_ForTwoPoints() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(5, 3));
		assertTrue(GeometryUtil.isConvexPath(path));
	}

	@Test
	public void isConvexPath_ForThreePoints() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(5, 3));
		path.add(new Point2D(3, 2));
		assertTrue(GeometryUtil.isConvexPath(path));
	}
	
	@Test
	public void isConvexPath_ForCwPoints() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(3, 1));
		path.add(new Point2D(4, 4));
		path.add(new Point2D(2, 3));
		assertTrue(GeometryUtil.isConvexPath(path));
	}

	@Test
	public void isConvexPath_ForCcwPoints() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 2));
		path.add(new Point2D(3, 0));
		path.add(new Point2D(4, -2));
		path.add(new Point2D(2, -3));
		assertTrue(GeometryUtil.isConvexPath(path));
	}

	@Test
	public void areConvex_ForPointsWithCrossingEdges() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 0));
		path.add(new Point2D(3, 0));
		path.add(new Point2D(4, -2));
		path.add(new Point2D(5, 2));
		assertFalse(GeometryUtil.isConvexPath(path));
	}

	@Test
	public void areConvex_ForPointsWithConcaveEdges() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 0));
		path.add(new Point2D(3, 1));
		path.add(new Point2D(4, 3));
		path.add(new Point2D(2, 2));
		path.add(new Point2D(0, 5));
		assertFalse(GeometryUtil.isConvexPath(path));
	}

	@Test
	public void areConvex_ForPointsInStraightLine() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 0));
		path.add(new Point2D(2, 1));
		path.add(new Point2D(3, 2));
		path.add(new Point2D(4, 3));
		assertTrue(GeometryUtil.isConvexPath(path));
	}

	@Test
	public void areConvex_ForPathWithDuplicateConsecutivePoints() {
		List<Point2D> path = new ArrayList<Point2D>();
		path.add(new Point2D(1, 0));
		path.add(new Point2D(2, 1));
		path.add(new Point2D(2, 1));
		path.add(new Point2D(3, 2));
		path.add(new Point2D(4, 3));
		assertTrue(GeometryUtil.isConvexPath(path));
	}
}
