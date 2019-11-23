package geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;


public class DelauneyTriangulationTest {

	@Test
	public void triangulate_ForNoPoints() {
		DelauneyTriangulation dt = new DelauneyTriangulation(new HashSet<Point2D>());
		List<Triangle2D> triangles = dt.triangulate();
		assertTrue(triangles.isEmpty());
	}

	@Test
	public void triangulate_ForOnePoint() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		DelauneyTriangulation dt = new DelauneyTriangulation(samples);
		List<Triangle2D> triangles = dt.triangulate();
		
		assertTrue(triangles.isEmpty());
	}

	@Test
	public void triangulate_ForTwoPoints() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(6, -3));
		DelauneyTriangulation dt = new DelauneyTriangulation(samples);
		List<Triangle2D> triangles = dt.triangulate();
		
		assertTrue(triangles.isEmpty());
	}

	@Test
	public void triangulate_ForThreePoints() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(6, -3);
		Point2D c = new Point2D(-2, -1);
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(a);
		samples.add(b);
		samples.add(c);
		DelauneyTriangulation dt = new DelauneyTriangulation(samples);
		List<Triangle2D> triangles = dt.triangulate();
		
		assertTrue(triangles.size() == 1);
		if (triangles.size() == 1) {
			Triangle2D t = triangles.get(0);
			assertTrue(t.hasVertex(a));
			assertTrue(t.hasVertex(b));
			assertTrue(t.hasVertex(c));
		}
	}

	@Test
	public void triangulate_ForFourPointsAsRect() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 10));
		samples.add(new Point2D(5, 10));
		samples.add(new Point2D(5, 1));
		samples.add(new Point2D(1, 1));
		DelauneyTriangulation dt = new DelauneyTriangulation(samples);
		List<Triangle2D> triangles = dt.triangulate();
		
		assertTrue(triangles.size() == 2);
		assertTrue(DelauneyTriangulation.isDelauneyConditionSatisfied(triangles));
	}

	@Test
	public void triangulate_ForTenPoints() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(2, 1));
		samples.add(new Point2D(5, 2));
		samples.add(new Point2D(2, 4));
		samples.add(new Point2D(5, 4));
		samples.add(new Point2D(9, 5));
		samples.add(new Point2D(5, 7));
		samples.add(new Point2D(10, 7));
		samples.add(new Point2D(3, 8));
		samples.add(new Point2D(1, 10));
		DelauneyTriangulation dt = new DelauneyTriangulation(samples);
		List<Triangle2D> triangles = dt.triangulate();
		
		assertTrue(DelauneyTriangulation.isDelauneyConditionSatisfied(triangles));
	}

	@Test
	public void delauneyTriangles() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(2, 1));
		samples.add(new Point2D(5, 2));
		samples.add(new Point2D(2, 4));
		samples.add(new Point2D(5, 4));
		samples.add(new Point2D(9, 5));
		DelauneyTriangulation dt = new DelauneyTriangulation(samples);
		List<Triangle2D> triangles = dt.triangulate();
		List<DelauneyTriangle> extTriangles = dt.delauneyTriangles();
		
		assertEquals(triangles.size(), extTriangles.size());
	}

	@Test
	public void isDelauneyConditionSatisfied_ForOneTriangle() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(6, -3);
		Point2D c = new Point2D(-2, -1);
		List<Triangle2D> triangles = new ArrayList<Triangle2D>();
		triangles.add(new Triangle2D(a, b, c));
		assertTrue(DelauneyTriangulation.isDelauneyConditionSatisfied(triangles));
	}

	@Test
	public void isDelauneyConditionSatisfied_ForTrianglesThatFullfillTheCondition() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(6, -3);
		Point2D c = new Point2D(7, 5);
		List<Triangle2D> triangles = new ArrayList<Triangle2D>();
		triangles.add(new Triangle2D(a, b, c));
		triangles.add(new Triangle2D(a, new Point2D(-5, 4), new Point2D(-3, -2)));
		assertTrue(DelauneyTriangulation.isDelauneyConditionSatisfied(triangles));
	}

	@Test
	public void isDelauneyConditionSatisfied_ForTrianglesThatDontFullfillTheCondition() {
		Point2D a = new Point2D(1, 2);
		Point2D b = new Point2D(6, -3);
		Point2D c = new Point2D(7, 5);
		List<Triangle2D> triangles = new ArrayList<Triangle2D>();
		triangles.add(new Triangle2D(a, b, c));
		triangles.add(new Triangle2D(a, new Point2D(7, 4), new Point2D(5, 10)));
		assertFalse(DelauneyTriangulation.isDelauneyConditionSatisfied(triangles));
	}
}
