package geometry;

import static org.junit.Assert.*;
import org.junit.Test;


public class Triangle2DTest {

	@Test
	public void construct_WithCcwPoints() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		assertEquals(a, t.vertex(0));
		assertEquals(b, t.vertex(1));
		assertEquals(c, t.vertex(2));
	}

	@Test
	public void construct_WithCwPoints() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(2, 7);
		Triangle2D t = new Triangle2D(a, b, c);
		assertEquals(a, t.vertex(0));
		assertEquals(c, t.vertex(1));
		assertEquals(b, t.vertex(2));
	}

	@Test
	public void equals_WithSelf() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		assertTrue(t.equals(t));
	}

	@Test
	public void equals_WithNull() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		assertFalse(t.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		assertFalse(t.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t1 = new Triangle2D(a, b, c);
		Triangle2D t2 = new Triangle2D(a.copy(), b.copy(), c.copy());
		assertTrue(t1.equals(t2));
	}

	@Test
	public void equals_WhenInequal() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t1 = new Triangle2D(a, b, c);
		Triangle2D t2 = new Triangle2D(c.copy(), b.copy(), a.copy());
		assertFalse(t1.equals(t2));
	}

	@Test
	public void copy() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		Triangle2D copy = t.copy();
		assertEquals(a, copy.vertex(0));
		assertEquals(b, copy.vertex(1));
		assertEquals(c, copy.vertex(2));
	}

	@Test
	public void vertex() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		assertEquals(a, t.vertex(0));
		assertEquals(b, t.vertex(1));
		assertEquals(c, t.vertex(2));
	}

	@Test
	public void hasVertex_WhenTriangleHasThePoint() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		
		assertTrue(t.hasVertex(a.copy()));
		assertTrue(t.hasVertex(b.copy()));
		assertTrue(t.hasVertex(c.copy()));
	}

	@Test
	public void hasVertex_WhenTriangleDoesNotHaveThePoint() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		
		assertFalse(t.hasVertex(new Point2D(4, 2)));
	}
	
	@Test
	public void edge_WithCcwPoints() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(7, 1);
		Triangle2D t = new Triangle2D(a, b, c);
		
		assertEquals(new LineSegment2D(a, b), t.edge(0));
		assertEquals(new LineSegment2D(b, c), t.edge(1));
		assertEquals(new LineSegment2D(c, a), t.edge(2));
	}
	
	@Test
	public void edge_WithCwPoints() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(2, 7);
		Triangle2D t = new Triangle2D(a, b, c);
		
		assertEquals(new LineSegment2D(a, c), t.edge(0));
		assertEquals(new LineSegment2D(c, b), t.edge(1));
		assertEquals(new LineSegment2D(b, a), t.edge(2));
	}
	
	@Test
	public void isPoint_WhenTriangleIsPoint() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(1, 2);
		Point2D c = new Point2D(1, 2);
		Triangle2D t = new Triangle2D(a, b, c);
		assertTrue(t.isPoint());
	}

	@Test
	public void isPoint_WhenTriangleIsNotPoint() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(10, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		assertFalse(t.isPoint());
	}

	@Test
	public void isLine_WhenTriangleIsLine() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(5, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		assertTrue(t.isLine());
	}

	@Test
	public void isLine_WhenTriangleIsLineWithTwoEqualPoints() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(1, 2);
		Point2D c = new Point2D(5, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		assertTrue(t.isLine());
	}

	@Test
	public void isLine_WhenTriangleIsNotLine() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(10, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		assertFalse(t.isLine());
	}

	@Test
	public void isLine_WhenTriangleIsPoint() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(1, 2);
		Point2D c = new Point2D(1, 2);
		Triangle2D t = new Triangle2D(a, b, c);
		assertFalse(t.isLine());
	}

	@Test
	public void isDegenerate_WhenTriangleIsLine() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(5, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		assertTrue(t.isDegenerate());
	}

	@Test
	public void isDegenerate_WhenTriangleIsNotDegenerate() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(10, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		assertFalse(t.isDegenerate());
	}

	@Test
	public void isDegenerate_WhenTriangleIsPoint() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(1, 2);
		Point2D c = new Point2D(1, 2);
		Triangle2D t = new Triangle2D(a, b, c);
		assertTrue(t.isDegenerate());
	}

	@Test
	public void area_ForRightAngledTriangle() {
		Point2D a = new Point2D(1, 1);
		Point2D b = new Point2D(4, 1);
		Point2D c = new Point2D(1, 3);
		Triangle2D t = new Triangle2D(a, b, c);
		assertEquals(3.0, t.area(), 0.0);
	}

	@Test
	public void area_ForNonRightAngledTriangle() {
		Point2D a = new Point2D(1, 1);
		Point2D b = new Point2D(4, 1);
		Point2D c = new Point2D(2, 7);
		Triangle2D t = new Triangle2D(a, b, c);
		assertEquals(9.0, t.area(), 0.0);
	}

	@Test
	public void area_ForDegenerateTriangle() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(5, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		assertEquals(0.0, t.area(), 0.0);
	}

	@Test
	public void calcCircumcircle_ForTriangleThatIsPoint() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(1, 2);
		Point2D c = new Point2D(1, 2);
		Triangle2D t = new Triangle2D(a, b, c);
		
		try {
			Circle2D ccircle = t.calcCircumcircle();
			assertEquals(new Circle2D(a, 0), ccircle);
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void calcCircumcircle_ForTriangleThatIsLine() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(3, 4);
		Point2D c = new Point2D(5, 6);
		Triangle2D t = new Triangle2D(a, b, c);
		
		try {
			Circle2D ccircle = t.calcCircumcircle();
			assertNull(ccircle);
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void calcCircumcircle_ForEquilateralTriangleWithBaseMidpointAtOrigin() {
		Point2D a = new Point2D(2, 0);
		Point2D b = new Point2D(-2, 0);
		Point2D c = new Point2D(0, 2);
		Triangle2D t = new Triangle2D(a, b, c);
		
		try {
			Circle2D ccircle = t.calcCircumcircle();
			assertEquals(new Point2D(0, 0), ccircle.center);
			assertEquals(2.0, ccircle.radius, 0.0);
		} catch (GeometryException e) {
			fail();
		}
	}

	@Test
	public void calcCircumcircle() {
		Point2D center = new Point2D(3, 8);
		double radius = 4;
		Circle2D c = new Circle2D(center, radius);
		
		Point2D p1 = c.pointAtRadian(1.0);
		Point2D p2 = c.pointAtRadian(3.0);
		Point2D p3 = c.pointAtRadian(5.0);
		
		Triangle2D t = new Triangle2D(p1, p2, p3);
		
		try {
			Circle2D ccircle = t.calcCircumcircle();
			assertEquals(center, ccircle.center);
			assertEquals(radius, ccircle.radius, 0.0);
		} catch (GeometryException e) {
			fail();
		}
	}
}
