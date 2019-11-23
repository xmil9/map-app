package geometry;

import static org.junit.Assert.*;

import org.junit.Test;


public class DelauneyTriangleTest {

	@Test
	public void equals_WithSelf() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			assertTrue(dt.equals(dt));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void equals_WithNull() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			assertFalse(dt.equals(null));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void equals_WithOtherClass() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			assertFalse(dt.equals(Double.valueOf(2.0)));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void equals_WhenEqual() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt1 = new DelauneyTriangle(t);
			DelauneyTriangle dt2 = new DelauneyTriangle(t.copy());
			assertTrue(dt1.equals(dt2));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void equals_WhenInequal() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt1 = new DelauneyTriangle(t);
			DelauneyTriangle dt2 = new DelauneyTriangle(new Triangle2D(b, a, c));
			assertFalse(dt1.equals(dt2));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void copy() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			DelauneyTriangle copy = dt.copy();
			assertFalse(dt == copy);
			assertEquals(dt.triangle(), copy.triangle());
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void triangle() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			Triangle2D tr = dt.triangle();

			assertEquals(t, tr);
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void vertex() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			assertEquals(a, dt.vertex(0));
			assertEquals(b, dt.vertex(1));
			assertEquals(c, dt.vertex(2));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void findVertex_WhenVertexInTriangle() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			assertEquals(0, dt.findVertex(a));
			assertEquals(1, dt.findVertex(b));
			assertEquals(2, dt.findVertex(c));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void findVertex_WhenVertexNotInTriangle() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			assertEquals(-1, dt.findVertex(new Point2D(7, 8)));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void edge() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			assertEquals(new LineSegment2D(a, b), dt.edge(0));
			assertEquals(new LineSegment2D(b, c), dt.edge(1));
			assertEquals(new LineSegment2D(c, a), dt.edge(2));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void isPointInCircumcircle_ForPointInCircumcircle() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			assertTrue(dt.isPointInCircumcircle(new Point2D(6, 2)));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void isPointInCircumcircle_ForPointOutsideCircumcircle() {
		try {
			Point2D a = new Point2D(1, 2);
			Point2D b = new Point2D(3, 4);
			Point2D c = new Point2D(7, 1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			assertFalse(dt.isPointInCircumcircle(new Point2D(10, 2)));
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void circumcenter() {
		try {
			Circle2D circle = new Circle2D(new Point2D(1, 2), 3);
			Point2D a = circle.pointAtRadian(1);
			Point2D b = circle.pointAtRadian(1.3);
			Point2D c = circle.pointAtRadian(2.1);
			Triangle2D t = new Triangle2D(a, b, c);
			DelauneyTriangle dt = new DelauneyTriangle(t);
			
			assertEquals(circle.center, dt.circumcenter());
		} catch (GeometryException e) {
			fail();
		}
	}
}
