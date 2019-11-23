package geometry;

import static org.junit.Assert.*;
import org.junit.Test;


public class InfiniteLine2DTest {

	@Test
	public void equals_WithSelf() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D(-1, 3));
		assertTrue(line.equals(line));
	}

	@Test
	public void equals_WithNull() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D(-1, 3));
		assertFalse(line.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D(-1, 3));
		assertFalse(line.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line1 = new InfiniteLine2D(start, new Vector2D(-1, 3));
		InfiniteLine2D line2 = new InfiniteLine2D(start, new Vector2D(-1, 3));
		assertTrue(line1.equals(line2));
	}

	@Test
	public void equals_WhenInequal() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line1 = new InfiniteLine2D(start, new Vector2D(-1, 3));
		InfiniteLine2D line2 = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(-3, 4));
		assertFalse(line1.equals(line2));
	}

	@Test
	public void copy() {
		Point2D pt = new Point2D(3, 4);
		InfiniteLine2D l = new InfiniteLine2D(pt, new Vector2D(1, 1));
		Line2D copy = l.copy();
		assertTrue(l.isCoincident(copy));
	}
	
	@Test
	public void isPoint() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D(1, 2));
		assertFalse(line.isPoint());
	}

	@Test
	public void isPoint_ForDegenerateLine() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D());
		assertTrue(line.isPoint());
	}

	@Test
	public void anchorPoint() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D(1, 0));
		Point2D pt = line.anchorPoint();
		assertTrue(line.isPointOnLine(pt).isOnLine);
	}

	@Test
	public void hasStartPoint() {
		Point2D anchor = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(anchor, new Vector2D(1, 0));
		assertFalse(line.hasStartPoint());
	}

	@Test
	public void startPoint() {
		Point2D anchor = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(anchor, new Vector2D(1, 0));
		assertTrue(line.startPoint() == null);
	}

	@Test
	public void hasEndPoint() {
		Point2D anchor = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(anchor, new Vector2D(1, 0));
		assertFalse(line.hasEndPoint());
	}

	@Test
	public void endPoint() {
		Point2D anchor = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(anchor, new Vector2D(1, 0));
		assertTrue(line.endPoint() == null);
	}

	@Test
	public void direction() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D(2, 0));
		Vector2D dir = line.direction();
		assertEquals(new Vector2D(2, 0), dir);
	}

	@Test
	public void direction_ForDegenerateLine() {
		Point2D start = new Point2D(3, 4);
		InfiniteLine2D line = new InfiniteLine2D(start, new Vector2D());
		Vector2D dir = line.direction();
		assertEquals(new Vector2D(), dir);
	}

	@Test
	public void isPointOnLine_WhenPointIsOnLine() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(0.7);
		Point2D pt = start.offset(v);
		Line2D.PointOnLineResult res = line.isPointOnLine(pt);
		
		assertTrue(res.isOnLine);
		assertEquals(0.7, res.parametricValue, 0.000001);
	}

	@Test
	public void isPointOnLine_WhenPointIsNextToLine() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Point2D pt = new Point2D(7, 1);
		Line2D.PointOnLineResult res = line.isPointOnLine(pt);
		
		assertFalse(res.isOnLine);
	}

	@Test
	public void isPointOnLine_WhenPointIsOnInfiniteLineExtensionBeforeTheAnchorPoint() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(-0.2);
		Point2D pt = start.offset(v);
		Line2D.PointOnLineResult res = line.isPointOnLine(pt);
		
		assertTrue(res.isOnLine);
		assertEquals(-0.2, res.parametricValue, 0.000001);
	}

	@Test
	public void isPointOnLine_WhenPointIsOnInfiniteRay() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(1.5);
		Point2D pt = start.offset(v);
		Line2D.PointOnLineResult res = line.isPointOnLine(pt);
		
		assertTrue(res.isOnLine);
		assertEquals(1.5, res.parametricValue, 0.000001);
	}

	@Test
	public void isPointOnInfiniteLine_WhenPointIsOnLine() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(0.7);
		Point2D pt = start.offset(v);
		Line2D.PointOnLineResult res = line.isPointOnInfiniteLine(pt);
		
		assertTrue(res.isOnLine);
		assertEquals(0.7, res.parametricValue, 0.000001);
	}

	@Test
	public void isPointOnInfiniteLine_WhenPointIsNextToLine() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Point2D pt = new Point2D(7, 1);
		Line2D.PointOnLineResult res = line.isPointOnInfiniteLine(pt);
		
		assertFalse(res.isOnLine);
	}

	@Test
	public void isPointOnInfiniteLine_WhenPointIsOnInfiniteLineExtensionBeforeTheAnchorPoint() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(-0.2);
		Point2D pt = start.offset(v);
		Line2D.PointOnLineResult res = line.isPointOnInfiniteLine(pt);
		
		assertTrue(res.isOnLine);
		assertEquals(-0.2, res.parametricValue, 0.000001);
	}

	@Test
	public void isPointOnInfiniteLine_WhenPointIsOnInfiniteRay() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(1.5);
		Point2D pt = start.offset(v);
		Line2D.PointOnLineResult res = line.isPointOnInfiniteLine(pt);
		
		assertTrue(res.isOnLine);
		assertEquals(1.5, res.parametricValue, 0.000001);
	}

	@Test
	public void calcParametricValue_ForPointWithinLengthOfDirectionVector() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(0.3);
		Point2D pt = start.offset(v);
		double parametricVal = line.calcParametricValue(pt);
		
		assertEquals(0.3, parametricVal, 0.000001);
	}

	@Test
	public void calcParametricValue_ForPointPastTheLengthOfDirectionVector() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(4.5);
		Point2D pt = start.offset(v);
		double parametricVal = line.calcParametricValue(pt);
		
		assertEquals(4.5, parametricVal, 0.000001);
	}

	@Test
	public void calcParametricValue_ForPointBeforeTheStartPoint() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		Vector2D v = dir.scale(-1.2);
		Point2D pt = start.offset(v);
		double parametricVal = line.calcParametricValue(pt);
		
		assertEquals(-1.2, parametricVal, 0.000001);
	}

	@Test
	public void calcParametricValue_ForPointNotOnTheLine() {
		Point2D start = new Point2D(3, 4);
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line = new InfiniteLine2D(start, dir);
		
		double parametricVal = line.calcParametricValue(new Point2D(1, 1));
		
		assertEquals(Double.MAX_VALUE, parametricVal, 0.000001);
	}

	@Test
	public void isParallel_ForParallelLine() {
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line1 = new InfiniteLine2D(new Point2D(3, 4), dir);
		InfiniteLine2D line2 = new InfiniteLine2D(new Point2D(2, 1), dir);
		
		assertTrue(line1.isParallel(line2));
	}

	@Test
	public void isParallel_ForNonParallelLine() {
		InfiniteLine2D line1 = new InfiniteLine2D(new Point2D(3, 4), new Vector2D(2, 1));
		InfiniteLine2D line2 = new InfiniteLine2D(new Point2D(2, 1), new Vector2D(1, 3));
		assertFalse(line1.isParallel(line2));
	}

	@Test
	public void isCoincident_ForCoincidentLine() {
		Vector2D dir = new Vector2D(2, 1);
		InfiniteLine2D line1 = new InfiniteLine2D(new Point2D(3, 4), dir);
		InfiniteLine2D line2 = new InfiniteLine2D(new Point2D(5, 5), dir);
		
		assertTrue(line1.isCoincident(line2));
	}

	@Test
	public void isCoincident_ForNonCoincidentLine() {
		InfiniteLine2D line1 = new InfiniteLine2D(new Point2D(3, 4), new Vector2D(2, 1));
		InfiniteLine2D line2 = new InfiniteLine2D(new Point2D(2, 1), new Vector2D(1, 3));
		assertFalse(line1.isCoincident(line2));
	}
}
