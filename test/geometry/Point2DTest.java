package geometry;

import static org.junit.Assert.*;
import org.junit.Test;


public class Point2DTest {

	@Test
	public void copy() {
		Point2D p = new Point2D(2, 3);
		Point2D copy = p.copy();
		assertEquals(copy, p);
		assertFalse(copy == p);
	}

	@Test
	public void equals_WithSelf() {
		Point2D p = new Point2D(2, 3);
		assertTrue(p.equals(p));
	}

	@Test
	public void equals_WithNull() {
		Point2D p = new Point2D(2, 3);
		assertFalse(p.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Point2D p = new Point2D(2, 3);
		assertFalse(p.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Point2D p = new Point2D(2, 3);
		Point2D q = new Point2D(2, 3);
		assertTrue(p.equals(q));
	}

	@Test
	public void equals_WhenInequal() {
		Point2D p = new Point2D(2, 3);
		Point2D q = new Point2D(3, 4);
		assertFalse(p.equals(q));
	}

	@Test
	public void offset_ByCoordinates() {
		Point2D p = new Point2D(2, 3);
		Point2D q = p.offset(2, 3);
		assertEquals(4.0, q.x, 0.0);
		assertEquals(6.0, q.y, 0.0);
	}

	@Test
	public void offset_ByVector() {
		Point2D p = new Point2D(2, 3);
		Point2D q = p.offset(new Vector2D(2, 3));
		assertEquals(4.0, q.x, 0.0);
		assertEquals(6.0, q.y, 0.0);
	}

	@Test
	public void scale() {
		Point2D p = new Point2D(2, 3);
		Point2D q = p.scale(2);
		assertEquals(4.0, q.x, 0.0);
		assertEquals(6.0, q.y, 0.0);
	}
	
	@Test
	public void distance() {
		assertEquals(Math.sqrt(5),
				Point2D.distance(new Point2D(1, 3), new Point2D(3, 2)), 0.00001);
		assertEquals(Math.sqrt(41),
				Point2D.distance(new Point2D(-1, 3), new Point2D(3, -2)), 0.00001);
	}
	
	@Test
	public void distance_ForSamePoint() {
		assertEquals(0,
				Point2D.distance(new Point2D(1, 3), new Point2D(1, 3)), 0.0);
	}
	
	@Test
	public void distanceSquared() {
		assertEquals(5,
				Point2D.distanceSquared(new Point2D(1, 3), new Point2D(3, 2)), 0.0);
		assertEquals(41,
				Point2D.distanceSquared(new Point2D(-1, 3), new Point2D(3, -2)), 0.0);
	}
	
	@Test
	public void distanceSquared_ForSamePoint() {
		assertEquals(0,
				Point2D.distanceSquared(new Point2D(1, 3), new Point2D(1, 3)), 0.0);
	}
}
