package geometry;

import static org.junit.Assert.*;
import org.junit.Test;


public class Circle2DTest {

	@Test
	public void equals_WithSelf() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertTrue(c.equals(c));
	}

	@Test
	public void equals_WithNull() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertFalse(c.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertFalse(c.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Circle2D c1 = new Circle2D(new Point2D(1, 2), 3);
		Circle2D c2 = new Circle2D(new Point2D(1, 2), 3);
		assertTrue(c1.equals(c2));
	}

	@Test
	public void equals_WhenInequal() {
		Circle2D c1 = new Circle2D(new Point2D(1, 2), 3);
		Circle2D c2 = new Circle2D(new Point2D(0, 3), 2);
		assertFalse(c1.equals(c2));
	}

	@Test
	public void copy() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		Circle2D copy = c.copy();
		assertEquals(new Point2D(1, 2), copy.center);
		assertEquals(3, copy.radius, 0.0);
	}

	@Test
	public void isPoint_WhenCircleIsPoint() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 0);
		assertTrue(c.isPoint());
	}

	@Test
	public void isPoint_WhenCircleIsNotPoint() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 1);
		assertFalse(c.isPoint());
	}

	@Test
	public void offset() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		Circle2D off = c.offset(new Vector2D(2, 1));
		assertEquals(new Point2D(3, 3), off.center);
		assertEquals(c.radius, off.radius, 0.0);
	}

	@Test
	public void isPointInCircle_ForPointInside() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertTrue(c.isPointInCircle(new Point2D(2, 2)));
	}

	@Test
	public void isPointInCircle_ForPointOutside() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertFalse(c.isPointInCircle(new Point2D(2, 7)));
	}

	@Test
	public void isPointInCircle_ForPointExactlyOnCircle() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertTrue(c.isPointInCircle(new Point2D(1, 5)));
	}

	@Test
	public void isPointOnCircle_ForPointInside() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertFalse(c.isPointOnCircle(new Point2D(2, 2)));
	}

	@Test
	public void isPointOnCircle_ForPointOutside() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertFalse(c.isPointOnCircle(new Point2D(2, 7)));
	}

	@Test
	public void isPointOnCircle_ForPointExactlyOnCircle() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertTrue(c.isPointOnCircle(new Point2D(1, 5)));
	}

	@Test
	public void isPointInsideCircle_ForPointOutside() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertFalse(c.isPointInsideCircle(new Point2D(2, 7)));
	}

	@Test
	public void isPointInsideCircle_ForPointInside() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertTrue(c.isPointInsideCircle(new Point2D(2, 2)));
	}

	@Test
	public void isPointInsideCircle_ForPointExactlyOnCircle() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertFalse(c.isPointInsideCircle(new Point2D(1, 5)));
	}

	@Test
	public void pointAtRadian_ForZeroRadians() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertEquals(new Point2D(4, 2), c.pointAtRadian(0.0));
	}

	@Test
	public void pointAtRadian_ForHalfPiRadians() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertEquals(new Point2D(1, 5), c.pointAtRadian(0.5 * Math.PI));
	}

	@Test
	public void pointAtRadian_ForPiRadians() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertEquals(new Point2D(-2, 2), c.pointAtRadian(Math.PI));
	}

	@Test
	public void pointAtRadian_ForThreeHalfPiRadians() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertEquals(new Point2D(1, -1), c.pointAtRadian(1.5 * Math.PI));
	}

	@Test
	public void pointAtRadian_ForTwoPiRadians() {
		Circle2D c = new Circle2D(new Point2D(1, 2), 3);
		assertEquals(new Point2D(4, 2), c.pointAtRadian(2.0 * Math.PI));
	}
}
