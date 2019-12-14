package geometry;

import static org.junit.Assert.*;
import org.junit.Test;

import math.FpUtil;


public class LineIntersection2DTest {

	private boolean haveSameSlope(Vector2D a, Vector2D b) {
		if (a.x == 0)
			return b.x == 0;
		return FpUtil.fpEqual(a.y / a.x, b.y / b.x); 
	}
	
	///////////////
	
	@Test
	public void intersect_SegSeg_BothSeparatePoints() {
		Point2D p = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(p, p);
		Point2D q = new Point2D(2, 1);
		LineSegment2D b = new LineSegment2D(q, q);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegSeg_BothTheSamePoint() {
		Point2D p = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(p, p);
		LineSegment2D b = new LineSegment2D(p, p);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegSeg_OneIsAPoint_PointIsOnLine() {
		LineSegment2D a = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 3));
		Point2D p = a.calcPointAt(0.6);
		LineSegment2D b = new LineSegment2D(p, p);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
		
		// Switch order.
		res = LineIntersection2D.intersect(b, a);
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegSeg_CoincidentAndOverlapping_SameDirection() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(-0.8));
		Point2D endB = a.startPoint().offset(a.direction().scale(0.4));
		LineSegment2D b = new LineSegment2D(startB, endB);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(startA, endB), res.intersection);
	}

	@Test
	public void intersect_SegSeg_CoincidentAndOverlapping_OppositeDirection() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		Point2D endB = a.startPoint().offset(a.direction().scale(-2.4));
		LineSegment2D b = new LineSegment2D(startB, endB);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(startA, startB), res.intersection);
	}

	@Test
	public void intersect_SegSeg_CoincidentButDisjoint() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(-3.8));
		Point2D endB = a.startPoint().offset(a.direction().scale(-0.5));
		LineSegment2D b = new LineSegment2D(startB, endB);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegSeg_TheSame() {
		Point2D start = new Point2D(1, 2);
		Point2D end = new Point2D(3, 3);
		LineSegment2D a = new LineSegment2D(start, end);
		LineSegment2D b = new LineSegment2D(start, end);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(start, end), res.intersection);
	}

	@Test
	public void intersect_SegSeg_ParallelButNotCoincident() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Vector2D(3, 3));
		LineSegment2D b = new LineSegment2D(new Point2D(4, 2), a.direction());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegSeg_Intersecting() {
		Point2D ip = new Point2D(3, 4);
		// Build the lines so that they intersect at the intersection point.
		Vector2D dirA = new Vector2D(1, 2);
		Point2D startA = ip.offset(dirA.scale(-1.0));
		Point2D endA = ip.offset(dirA.scale(0.7));
		LineSegment2D a = new LineSegment2D(startA, endA);
		Vector2D dirB = new Vector2D(5, 3);
		Point2D startB = ip.offset(dirB.scale(-1.2));
		Point2D endB = ip.offset(dirB.scale(1.7));
		LineSegment2D b = new LineSegment2D(startB, endB);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(ip, res.intersection);
	}

	@Test
	public void intersect_SegSeg_IntersectingAtOrigin() {
		LineSegment2D a = new LineSegment2D(new Point2D(-1, 0), new Point2D(3, 0));
		LineSegment2D b = new LineSegment2D(new Point2D(0, 2), new Point2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(new Point2D(), res.intersection);
	}

	@Test
	public void intersect_SegSeg_NotIntersecting() {
		LineSegment2D a = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 3));
		LineSegment2D b = new LineSegment2D(new Point2D(0, 2), new Point2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	///////////////
	
	@Test
	public void intersect_SegRay_BothSeparatePoints() {
		Point2D p = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(p, p);
		Point2D q = new Point2D(2, 1);
		LineRay2D b = new LineRay2D(q, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegRay_BothTheSamePoint() {
		Point2D p = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(p, p);
		LineRay2D b = new LineRay2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegRay_RayIsAPoint_PointIsOnLine() {
		LineSegment2D a = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 3));
		Point2D p = a.calcPointAt(0.6);
		LineRay2D b = new LineRay2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegRay_SegIsAPoint_PointIsOnLine() {
		LineRay2D b = new LineRay2D(new Point2D(1, 2), new Vector2D(2, 3));
		Point2D p = b.calcPointAt(0.6);
		LineSegment2D a = new LineSegment2D(p, p);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegRay_SegIsAPoint_PointIsNotOnLine() {
		LineRay2D b = new LineRay2D(new Point2D(1, 2), new Vector2D(2, 3));
		Point2D p = new Point2D(0, 0);
		LineSegment2D a = new LineSegment2D(p, p);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegRay_CoincidentAndOverlapping_SameDirection() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		LineRay2D b = new LineRay2D(startB, a.direction().scale(2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(startB, a.endPoint()), res.intersection);
	}

	@Test
	public void intersect_SegRay_CoincidentAndOverlapping_OppositeDirection() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		LineRay2D b = new LineRay2D(startB, a.direction().scale(-2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(startA, startB), res.intersection);
	}

	@Test
	public void intersect_SegRay_CoincidentButDisjoint() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(1.8));
		LineRay2D b = new LineRay2D(startB, a.direction().scale(2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegRay_SameStartPoints() {
		Point2D start = new Point2D(1, 2);
		Point2D end = new Point2D(3, 3);
		LineSegment2D a = new LineSegment2D(start, end);
		LineRay2D b = new LineRay2D(start, a.direction());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(start, end), res.intersection);
	}

	@Test
	public void intersect_SegRay_ParallelButNotCoincident() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Vector2D(3, 3));
		LineRay2D b = new LineRay2D(new Point2D(4, 2), a.direction());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegRay_Intersecting() {
		Point2D ip = new Point2D(3, 4);
		// Build the lines so that they intersect at the intersection point.
		Vector2D dirA = new Vector2D(1, 2);
		Point2D startA = ip.offset(dirA.scale(-1.0));
		Point2D endA = ip.offset(dirA.scale(0.7));
		LineSegment2D a = new LineSegment2D(startA, endA);
		Vector2D dirB = new Vector2D(5, 3);
		Point2D startB = ip.offset(dirB.scale(-1.2));
		Point2D ptOnB = ip.offset(dirB.scale(1.7));
		LineRay2D b = new LineRay2D(startB, new Vector2D(startB, ptOnB));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(ip, res.intersection);
	}

	@Test
	public void intersect_SegRay_IntersectingAtOrigin() {
		LineSegment2D a = new LineSegment2D(new Point2D(-1, 0), new Point2D(3, 0));
		LineRay2D b = new LineRay2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(new Point2D(), res.intersection);
	}

	@Test
	public void intersect_SegRay_NotIntersecting() {
		LineSegment2D a = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 3));
		LineRay2D b = new LineRay2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	///////////////
	
	@Test
	public void intersect_SegInf_BothSeparatePoints() {
		Point2D p = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(p, p);
		Point2D q = new Point2D(2, 1);
		InfiniteLine2D b = new InfiniteLine2D(q, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegInf_BothTheSamePoint() {
		Point2D p = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(p, p);
		InfiniteLine2D b = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegInf_InfIsAPoint_PointIsOnLine() {
		LineSegment2D a = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 3));
		Point2D p = a.calcPointAt(0.6);
		InfiniteLine2D b = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegInf_SegIsAPoint_PointIsOnLine() {
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 3));
		Point2D p = b.calcPointAt(0.6);
		LineSegment2D a = new LineSegment2D(p, p);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_SegInf_SegIsAPoint_PointIsNotOnLine() {
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 3));
		Point2D p = new Point2D(0, 0);
		LineSegment2D a = new LineSegment2D(p, p);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegInf_Coincident_SameDirection() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		InfiniteLine2D b = new InfiniteLine2D(startB, a.direction().scale(2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(a.startPoint(), a.endPoint()), res.intersection);
	}

	@Test
	public void intersect_SegInf_Coincident_OppositeDirection() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Point2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		InfiniteLine2D b = new InfiniteLine2D(startB, a.direction().scale(-2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(a.startPoint(), a.endPoint()), res.intersection);
	}

	@Test
	public void intersect_SegInf_ParallelButNotCoincident() {
		Point2D startA = new Point2D(1, 2);
		LineSegment2D a = new LineSegment2D(startA, new Vector2D(3, 3));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(4, 2), a.direction());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_SegInf_Intersecting() {
		Point2D ip = new Point2D(3, 4);
		// Build the lines so that they intersect at the intersection point.
		Vector2D dirA = new Vector2D(1, 2);
		Point2D startA = ip.offset(dirA.scale(-1.0));
		Point2D endA = ip.offset(dirA.scale(0.7));
		LineSegment2D a = new LineSegment2D(startA, endA);
		Vector2D dirB = new Vector2D(5, 3);
		Point2D startB = ip.offset(dirB.scale(-1.2));
		Point2D ptOnB = ip.offset(dirB.scale(1.7));
		InfiniteLine2D b = new InfiniteLine2D(startB, new Vector2D(startB, ptOnB));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(ip, res.intersection);
	}

	@Test
	public void intersect_SegInf_IntersectingAtOrigin() {
		LineSegment2D a = new LineSegment2D(new Point2D(-1, 0), new Point2D(3, 0));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(new Point2D(), res.intersection);
	}

	@Test
	public void intersect_SegInf_NotIntersecting() {
		LineSegment2D a = new LineSegment2D(new Point2D(1, 2), new Point2D(3, 3));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	///////////////
	
	@Test
	public void intersect_RayRay_BothSeparatePoints() {
		Point2D p = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(p, new Vector2D());
		Point2D q = new Point2D(2, 1);
		LineRay2D b = new LineRay2D(q, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_RayRay_BothTheSamePoint() {
		Point2D p = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(p, new Vector2D());
		LineRay2D b = new LineRay2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_RayRay_OneIsAPoint_PointIsOnLine() {
		LineRay2D a = new LineRay2D(new Point2D(1, 2), new Vector2D(3, 3));
		Point2D p = a.calcPointAt(0.6);
		LineRay2D b = new LineRay2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_RayRay_OneIsAPoint_PointIsNotOnLine() {
		LineRay2D b = new LineRay2D(new Point2D(1, 2), new Vector2D(2, 3));
		Point2D p = new Point2D(0, 0);
		LineRay2D a = new LineRay2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_RayRay_CoincidentAndOverlapping_OppositeDirection() {
		Point2D startA = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(startA, new Vector2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		LineRay2D b = new LineRay2D(startB, a.direction().scale(-2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_SEGMENT);
		assertEquals(new LineSegment2D(startA, startB), res.intersection);
	}

	@Test
	public void intersect_RayRay_CoincidentAndOverlappingAtStartPoints() {
		Point2D start = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(start, new Vector2D(3, 3));
		LineRay2D b = new LineRay2D(start, a.direction().scale(-2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(start, res.intersection);
	}

	@Test
	public void intersect_RayRay_CoincidentAndOverlapping_SameDirection() {
		Point2D startA = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(startA, new Vector2D(3, 3));
		Point2D startB = startA.offset(a.direction().scale(0.8));
		LineRay2D b = new LineRay2D(startB, a.direction().scale(2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_RAY);
		LineRay2D isectRay = (LineRay2D)res.intersection;
		assertEquals(startB, isectRay.startPoint());
		assertTrue(haveSameSlope(a.direction(), isectRay.direction()));
	}

	@Test
	public void intersect_RayRay_CoincidentButDisjoint() {
		Point2D startA = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(startA, new Vector2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(-1.0));
		LineRay2D b = new LineRay2D(startB, a.direction().scale(-2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_RayRay_ParallelButNotCoincident() {
		Point2D startA = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(startA, new Vector2D(3, 3));
		LineRay2D b = new LineRay2D(new Point2D(4, 2), a.direction());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_RayRay_Intersecting() {
		Point2D ip = new Point2D(3, 4);
		// Build the lines so that they intersect at the intersection point.
		Vector2D dirA = new Vector2D(1, 2);
		Point2D startA = ip.offset(dirA.scale(-1.0));
		LineRay2D a = new LineRay2D(startA, dirA);
		Vector2D dirB = new Vector2D(5, 3);
		Point2D startB = ip.offset(dirB.scale(-1.2));
		LineRay2D b = new LineRay2D(startB, dirB);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(ip, res.intersection);
	}

	@Test
	public void intersect_RayRay_IntersectingAtOrigin() {
		LineRay2D a = new LineRay2D(new Point2D(-1, 0), new Vector2D(3, 0));
		LineRay2D b = new LineRay2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(new Point2D(), res.intersection);
	}

	@Test
	public void intersect_RayRay_NotIntersecting() {
		LineRay2D a = new LineRay2D(new Point2D(1, 2), new Vector2D(3, 3));
		LineRay2D b = new LineRay2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	///////////////
	
	@Test
	public void intersect_RayInf_BothSeparatePoints() {
		Point2D p = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(p, new Vector2D());
		Point2D q = new Point2D(2, 1);
		InfiniteLine2D b = new InfiniteLine2D(q, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_RayInf_BothTheSamePoint() {
		Point2D p = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(p, new Vector2D());
		InfiniteLine2D b = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_RayInf_OneIsAPoint_PointIsOnLine() {
		LineRay2D a = new LineRay2D(new Point2D(1, 2), new Vector2D(3, 3));
		Point2D p = a.calcPointAt(0.6);
		InfiniteLine2D b = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_RayInf_OneIsAPoint_PointIsNotOnLine() {
		LineRay2D b = new LineRay2D(new Point2D(1, 2), new Vector2D(2, 3));
		Point2D p = new Point2D(0, 0);
		InfiniteLine2D a = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(b, a);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_RayInf_Coincident_SameDirection() {
		Point2D startA = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(startA, new Vector2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		InfiniteLine2D b = new InfiniteLine2D(startB, a.direction().scale(2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_RAY);
		LineRay2D isectRay = (LineRay2D)res.intersection;
		assertEquals(a.startPoint(), isectRay.startPoint());
		assertTrue(haveSameSlope(a.direction(), isectRay.direction()));
	}

	@Test
	public void intersect_RayInf_Coincident_OppositeDirection() {
		Point2D startA = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(startA, new Vector2D(3, 3));
		Point2D startB = a.startPoint().offset(a.direction().scale(0.8));
		InfiniteLine2D b = new InfiniteLine2D(startB, a.direction().scale(-2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.LINE_RAY);
		LineRay2D isectRay = (LineRay2D)res.intersection;
		assertEquals(a.startPoint(), isectRay.startPoint());
		assertTrue(haveSameSlope(a.direction(), isectRay.direction()));
	}

	@Test
	public void intersect_RayInf_ParallelButNotCoincident() {
		Point2D startA = new Point2D(1, 2);
		LineRay2D a = new LineRay2D(startA, new Vector2D(3, 3));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(4, 2), a.direction());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_RayInf_Intersecting() {
		Point2D ip = new Point2D(3, 4);
		// Build the lines so that they intersect at the intersection point.
		Vector2D dirA = new Vector2D(1, 2);
		Point2D startA = ip.offset(dirA.scale(-1.0));
		LineRay2D a = new LineRay2D(startA, dirA);
		Vector2D dirB = new Vector2D(5, 3);
		Point2D startB = ip.offset(dirB.scale(-1.2));
		InfiniteLine2D b = new InfiniteLine2D(startB, dirB);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(ip, res.intersection);
	}

	@Test
	public void intersect_RayInf_IntersectingAtOrigin() {
		LineRay2D a = new LineRay2D(new Point2D(-1, 0), new Vector2D(3, 0));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(new Point2D(), res.intersection);
	}

	@Test
	public void intersect_RayInf_NotIntersecting() {
		LineRay2D a = new LineRay2D(new Point2D(1, 2), new Vector2D(3, 3));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(0, 2), new Vector2D(1, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	///////////////
	
	@Test
	public void intersect_InfInf_BothSeparatePoints() {
		Point2D p = new Point2D(1, 2);
		InfiniteLine2D a = new InfiniteLine2D(p, new Vector2D());
		Point2D q = new Point2D(2, 1);
		InfiniteLine2D b = new InfiniteLine2D(q, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_InfInf_BothTheSamePoint() {
		Point2D p = new Point2D(1, 2);
		InfiniteLine2D a = new InfiniteLine2D(p, new Vector2D());
		InfiniteLine2D b = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_InfInf_OneIsAPoint_PointIsOnLine() {
		InfiniteLine2D a = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(3, 3));
		Point2D p = a.calcPointAt(0.6);
		InfiniteLine2D b = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(p, res.intersection);
	}

	@Test
	public void intersect_InfInf_OneIsAPoint_PointIsNotOnLine() {
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 3));
		Point2D p = new Point2D(0, 0);
		InfiniteLine2D a = new InfiniteLine2D(p, new Vector2D());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(b, a);

		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_InfInf_Coincident_SameDirection() {
		Point2D startA = new Point2D(1, 2);
		InfiniteLine2D a = new InfiniteLine2D(startA, new Vector2D(3, 3));
		Point2D startB = a.anchorPoint().offset(a.direction().scale(0.8));
		InfiniteLine2D b = new InfiniteLine2D(startB, a.direction().scale(2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.INFINITE_LINE);
		InfiniteLine2D isectLine = (InfiniteLine2D)res.intersection;
		assertEquals(a.anchorPoint(), isectLine.anchorPoint());
		assertTrue(haveSameSlope(a.direction(), isectLine.direction()));
	}

	@Test
	public void intersect_InfInf_Coincident_OppositeDirection() {
		Point2D startA = new Point2D(1, 2);
		InfiniteLine2D a = new InfiniteLine2D(startA, new Vector2D(3, 3));
		Point2D startB = a.anchorPoint().offset(a.direction().scale(0.8));
		InfiniteLine2D b = new InfiniteLine2D(startB, a.direction().scale(-2.5));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.INFINITE_LINE);
		InfiniteLine2D isectLine = (InfiniteLine2D)res.intersection;
		assertEquals(a.anchorPoint(), isectLine.anchorPoint());
		assertTrue(haveSameSlope(a.direction(), isectLine.direction()));
	}

	@Test
	public void intersect_InfInf_ParallelButNotCoincident() {
		Point2D startA = new Point2D(1, 2);
		InfiniteLine2D a = new InfiniteLine2D(startA, new Vector2D(3, 3));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(4, 2), a.direction());
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);
		
		assertTrue(res.type == LineIntersection2D.IntersectionType.NONE);
	}

	@Test
	public void intersect_InfInf_Intersecting() {
		Point2D ip = new Point2D(3, 4);
		// Build the lines so that they intersect at the intersection point.
		Vector2D dirA = new Vector2D(1, 2);
		Point2D startA = ip.offset(dirA.scale(-1.0));
		InfiniteLine2D a = new InfiniteLine2D(startA, dirA);
		Vector2D dirB = new Vector2D(5, 3);
		Point2D startB = ip.offset(dirB.scale(-1.2));
		InfiniteLine2D b = new InfiniteLine2D(startB, dirB);
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(ip, res.intersection);
	}

	@Test
	public void intersect_InfInf_IntersectingAtOrigin() {
		InfiniteLine2D a = new InfiniteLine2D(new Point2D(-1, 0), new Vector2D(3, 0));
		InfiniteLine2D b = new InfiniteLine2D(new Point2D(0, 2), new Vector2D(0, -2));
		
		LineIntersection2D.Result res = LineIntersection2D.intersect(a, b);

		assertTrue(res.type == LineIntersection2D.IntersectionType.POINT);
		assertEquals(new Point2D(), res.intersection);
	}
}
