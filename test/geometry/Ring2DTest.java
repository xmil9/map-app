package geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Ring2DTest {

	@Test
	public void construct_WithLargerInnerRadius() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 5, 4);
		assertEquals(4, r.innerRadius(), 0);
		assertEquals(5, r.outerRadius(), 0);
	}

	@Test
	public void equals_WithSelf() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertTrue(r.equals(r));
	}

	@Test
	public void equals_WithNull() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertFalse(r.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertFalse(r.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Ring2D r1 = new Ring2D(new Point2D(1, 2), 3, 4);
		Ring2D r2 = new Ring2D(new Point2D(1, 2), 3, 4);
		assertTrue(r1.equals(r2));
	}

	@Test
	public void equals_WhenInequal() {
		Ring2D r1 = new Ring2D(new Point2D(1, 2), 3, 4);
		Ring2D r2 = new Ring2D(new Point2D(0, 3), 2, 4);
		assertFalse(r1.equals(r2));
	}

	@Test
	public void copy() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		Ring2D copy = r.copy();
		assertEquals(new Point2D(1, 2), copy.center());
		assertEquals(3, copy.innerRadius(), 0.0);
		assertEquals(4, copy.outerRadius(), 0.0);
	}

	@Test
	public void center() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertEquals(new Point2D(1, 2), r.center());
	}

	@Test
	public void innerRadius() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertEquals(3, r.innerRadius(), 0);
	}

	@Test
	public void outerRadius() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertEquals(4, r.outerRadius(), 0);
	}

	@Test
	public void bounds() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertEquals(new Rect2D(-3, -2, 5, 6), r.bounds());
	}

	@Test
	public void offset() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		Ring2D off = r.offset(new Vector2D(2, 1));
		assertEquals(new Point2D(3, 3), off.center());
		assertEquals(r.innerRadius(), off.innerRadius(), 0.0);
		assertEquals(r.outerRadius(), off.outerRadius(), 0.0);
	}

	@Test
	public void isPointInRing_ForPointInsideInnerCircle() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertFalse(r.isPointInRing(new Point2D(2, 2)));
	}

	@Test
	public void isPointInRing_ForPointBetweenCircles() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertTrue(r.isPointInRing(new Point2D(-2.5, 2.2)));
	}

	@Test
	public void isPointInRing_ForPointOutsideOuterCircle() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertFalse(r.isPointInRing(new Point2D(6, -3)));
	}

	@Test
	public void isPointInRing_ForPointExactlyOnInnerCircle() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertTrue(r.isPointInRing(new Point2D(1, 5)));
	}

	@Test
	public void isPointInRing_ForPointExactlyOnOuterCircle() {
		Ring2D r = new Ring2D(new Point2D(1, 2), 3, 4);
		assertTrue(r.isPointInRing(new Point2D(1, -2)));
	}
}
