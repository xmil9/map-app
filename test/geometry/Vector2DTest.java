package geometry;

import static org.junit.Assert.*;
import org.junit.Test;


public class Vector2DTest {

	@Test
	public void construct_FromPoints() {
		Vector2D v = new Vector2D(new Point2D(1, 2), new Point2D(3, 5));
		assertEquals(2.0, v.x, 0.0);
		assertEquals(3.0, v.y, 0.0);
	}

	@Test
	public void copy() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D copy = v.copy();
		assertEquals(copy, v);
		assertFalse(copy == v);
	}

	@Test
	public void equals_WithSelf() {
		Vector2D v = new Vector2D(2, 3);
		assertTrue(v.equals(v));
	}

	@Test
	public void equals_WithNull() {
		Vector2D v = new Vector2D(2, 3);
		assertFalse(v.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Vector2D v = new Vector2D(2, 3);
		assertFalse(v.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(2, 3);
		assertTrue(v.equals(w));
	}

	@Test
	public void equals_WhenInequal() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(3, 4);
		assertFalse(v.equals(w));
	}

	@Test
	public void isZeroVector_ForZeroVector() {
		Vector2D zero = new Vector2D(0, 0);
		assertTrue(zero.isZeroVector());
	}

	@Test
	public void isZeroVector_ForNonZeroVector() {
		Vector2D zero = new Vector2D(1, 2);
		assertFalse(zero.isZeroVector());
	}

	@Test
	public void lengthSquared() {
		Vector2D v = new Vector2D(2, 3);
		assertEquals(13.0, v.lengthSquared(), 0.0);
	}

	@Test
	public void lengthSquared_ForZeroVector() {
		Vector2D zero = new Vector2D();
		assertEquals(0.0, zero.lengthSquared(), 0.0);
	}

	@Test
	public void length() {
		Vector2D v = new Vector2D(2, 3);
		assertEquals(Math.sqrt(13.0), v.length(), 0.0);
	}

	@Test
	public void length_ForZeroVector() {
		Vector2D zero = new Vector2D();
		assertEquals(0.0, zero.length(), 0.0);
	}

	@Test
	public void normalize() {
		Vector2D v = new Vector2D(4, 3);
		Vector2D n = v.normalize();
		assertEquals(4.0/5.0, n.x, 0.000001);
		assertEquals(3.0/5.0, n.y, 0.000001);
	}

	@Test
	public void normalize_ZeroVector() {
		Vector2D zero = new Vector2D();
		Vector2D n = zero.normalize();
		assertEquals(zero, n);
	}

	@Test
	public void scale() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = v.scale(2.0);
		assertEquals(4.0, w.x, 0.0);
		assertEquals(6.0, w.y, 0.0);
	}

	@Test
	public void dot_ForVectorInSameDirection() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(3, 4.5);
		double res = v.dot(w);
		assertEquals(19.5, res, 0.0);
	}

	@Test
	public void dot_ForVectorInOppositeDirection() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(-2, -3);
		double res = v.dot(w);
		assertEquals(-13.0, res, 0.0);
	}

	@Test
	public void dot_ForVectorPerpendicularToLeft() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(-3, 2);
		double res = v.dot(w);
		assertEquals(0.0, res, 0.0);
	}

	@Test
	public void dot_ForVectorPerpendicularToRight() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(3, -2);
		double res = v.dot(w);
		assertEquals(0.0, res, 0.0);
	}

	@Test
	public void dot_ForVectorToRightAtAcuteAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(4, 3);
		double res = v.dot(w);
		assertEquals(21.0, res, 0.0);
	}

	@Test
	public void dot_ForVectorToLeftAtAcuteAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(3, 4);
		double res = v.dot(w);
		assertEquals(21.0, res, 0.0);
	}

	@Test
	public void dot_ForVectorToRightAtObtuseAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(-3, -4);
		double res = v.dot(w);
		assertEquals(-21.0, res, 0.0);
	}

	@Test
	public void dot_ForVectorToLeftAtObtuseAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(-2, 1);
		double res = v.dot(w);
		assertEquals(-3.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorInSameDirection() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(3, 4.5);
		double res = v.perpDot(w);
		assertEquals(0.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorInOppositeDirection() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(-2, -3);
		double res = v.perpDot(w);
		assertEquals(0.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorPerpendicularToLeft() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(-3, 2);
		double res = v.perpDot(w);
		assertEquals(13.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorPerpendicularToRight() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(3, -2);
		double res = v.perpDot(w);
		assertEquals(-13.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorToRightAtAcuteAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(4, 3);
		double res = v.perpDot(w);
		assertEquals(-3.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorToLeftAtAcuteAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(3, 4);
		double res = v.perpDot(w);
		assertEquals(3.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorToRightAtObtuseAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(-3, -4);
		double res = v.perpDot(w);
		assertEquals(-3.0, res, 0.0);
	}

	@Test
	public void perpDot_ForVectorToLeftAtObtuseAngle() {
		Vector2D v = new Vector2D(3, 3);
		Vector2D w = new Vector2D(-2, 1);
		double res = v.perpDot(w);
		assertEquals(9.0, res, 0.0);
	}

	@Test
	public void isPerpendicular_ForPerpendicularVector() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(-3, 2);
		assertTrue(v.isPerpendicular(w));
	}

	@Test
	public void isPerpendicular_ForNonPerpendicularVector() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(2, 4);
		assertFalse(v.isPerpendicular(w));
	}

	@Test
	public void isOrthogonal_ForOrthogonalVector() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(-3, 2);
		assertTrue(v.isOrthogonal(w));
	}

	@Test
	public void isOrthogonal_ForNonOrthogonalVector() {
		Vector2D v = new Vector2D(2, 3);
		Vector2D w = new Vector2D(2, 4);
		assertFalse(v.isOrthogonal(w));
	}

	@Test
	public void hasSameDirection_ForVectorWithSameDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(4, 4);
		assertTrue(v.hasSameDirection(w));
	}

	@Test
	public void hasSameDirection_ForVectorWithOppositeDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-4, -4);
		assertFalse(v.hasSameDirection(w));
	}

	@Test
	public void hasSameDirection_ForVectorWithDifferentDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-3, -4);
		assertFalse(v.hasSameDirection(w));
	}

	@Test
	public void isParallel_ForVectorWithSameDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(4, 4);
		assertTrue(v.isParallel(w));
	}

	@Test
	public void isParallel_ForVectorWithOppositeDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-4, -4);
		assertTrue(v.isParallel(w));
	}

	@Test
	public void isParallel_ForVectorWithDifferentDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-3, -4);
		assertFalse(v.isParallel(w));
	}

	@Test
	public void hasAcuteAngle_ForVectorWithAcuteAngle() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(3, 4);
		assertTrue(v.hasAcuteAngle(w));
	}

	@Test
	public void hasAcuteAngle_ForVectorWithObtuseAngle() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-3, -4);
		assertFalse(v.hasAcuteAngle(w));
	}

	@Test
	public void hasAcuteAngle_ForVectorWithRightAngle() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-2, 2);
		assertFalse(v.hasAcuteAngle(w));
	}

	@Test
	public void hasObtuseAngle_ForVectorWithAcuteAngle() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(3, 4);
		assertFalse(v.hasObtuseAngle(w));
	}

	@Test
	public void hasObtuseAngle_ForVectorWithObtuseAngle() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-3, -4);
		assertTrue(v.hasObtuseAngle(w));
	}

	@Test
	public void hasObtuseAngle_ForVectorWithRightAngle() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(-2, 2);
		assertFalse(v.hasObtuseAngle(w));
	}

	@Test
	public void isCcw_ForVectorCcw() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(2, 1);
		// Screen CS.
		assertTrue(v.isCcw(w, CoordSystem.SCREEN));
		// Cartesian CS.
		assertFalse(v.isCcw(w, CoordSystem.CARTESIAN));
	}

	@Test
	public void isCcw_ForVectorCw() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(1, 2);
		// Screen CS.
		assertFalse(v.isCcw(w, CoordSystem.SCREEN));
		// Cartesian CS.
		assertTrue(v.isCcw(w, CoordSystem.CARTESIAN));
	}

	@Test
	public void isCcw_ForVectorInSameDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(3, 3);
		// Screen CS.
		assertFalse(v.isCcw(w, CoordSystem.SCREEN));
		// Cartesian CS.
		assertFalse(v.isCcw(w, CoordSystem.CARTESIAN));
	}

	@Test
	public void isCw_ForVectorCcw() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(2, 1);
		// Screen CS.
		assertFalse(v.isCw(w, CoordSystem.SCREEN));
		// Cartesian CS.
		assertTrue(v.isCw(w, CoordSystem.CARTESIAN));
	}

	@Test
	public void isCw_ForVectorCw() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(1, 2);
		// Screen CS.
		assertTrue(v.isCw(w, CoordSystem.SCREEN));
		// Cartesian CS.
		assertFalse(v.isCw(w, CoordSystem.CARTESIAN));
	}

	@Test
	public void isCw_ForVectorInSameDirection() {
		Vector2D v = new Vector2D(2, 2);
		Vector2D w = new Vector2D(3, 3);
		// Screen CS.
		assertFalse(v.isCw(w, CoordSystem.SCREEN));
		// Cartesian CS.
		assertFalse(v.isCw(w, CoordSystem.CARTESIAN));
	}

	@Test
	public void cwNormal() {
		Vector2D v = new Vector2D(2, 3);
		// Screen CS.
		Vector2D cwScreen = v.cwNormal(CoordSystem.SCREEN);
		assertTrue(v.isCw(cwScreen, CoordSystem.SCREEN));
		// Cartesian CS.
		Vector2D cwCart = v.cwNormal(CoordSystem.CARTESIAN);
		assertTrue(v.isCw(cwCart, CoordSystem.CARTESIAN));
	}

	@Test
	public void ccwNormal() {
		Vector2D v = new Vector2D(2, 3);
		// Screen CS.
		Vector2D ccwScreen = v.ccwNormal(CoordSystem.SCREEN);
		assertTrue(v.isCcw(ccwScreen, CoordSystem.SCREEN));
		// Cartesian CS.
		Vector2D ccwCart = v.ccwNormal(CoordSystem.CARTESIAN);
		assertTrue(v.isCcw(ccwCart, CoordSystem.CARTESIAN));
	}
}
