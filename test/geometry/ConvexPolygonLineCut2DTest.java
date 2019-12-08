package geometry;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ConvexPolygonLineCut2DTest {

	@Test
	public void convexPolygonLine_ForEmptyPoly() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 1);
		assertTrue(res.get(0).countVertices() == 0);
	}

	@Test
	public void intersect_ForPolyWithSinglePoint() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(1, 1));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 1);
		assertTrue(res.get(0).countVertices() == 1);
		assertEquals(new Point2D(1, 1), res.get(0).vertex(0));
	}

	@Test
	public void intersect_ForPolyWithSinglePointOnLine() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(1, 2));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 1);
		assertTrue(res.get(0).countVertices() == 1);
		assertEquals(new Point2D(1, 2), res.get(0).vertex(0));
	}

	@Test
	public void intersect_ForPolyWithTwoPointsCrossingTheLine() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(1, 1));
		p.addVertex(new Point2D(2, 5));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 2);
		assertTrue(res.get(0).countVertices() == 2);
		assertTrue(res.get(1).countVertices() == 2);
	}

	@Test
	public void intersect_ForPolyWithTwoPointsOnSameSideOfLine() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(3, 4));
		p.addVertex(new Point2D(2, 5));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 1);
		assertTrue(res.get(0).countVertices() == 2);
	}

	@Test
	public void intersect_ForPolyWithTwoPointsOnLine() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(3, 3));
		p.addVertex(new Point2D(5, 4));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 1);
		assertTrue(res.get(0).countVertices() == 2);
	}

	@Test
	public void intersect_ForPolyIntersectedByLine() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(1, 2), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(1, -1));
		p.addVertex(new Point2D(7, 1));
		p.addVertex(new Point2D(6, 2));
		p.addVertex(new Point2D(4, 6));
		p.addVertex(new Point2D(2, 5));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 2);
		assertTrue(res.get(0).countVertices() == 5);
		assertTrue(res.get(1).countVertices() == 4);
	}

	@Test
	public void intersect_ForPolyNotIntersectedByLine() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(10, 20), new Vector2D(2, 1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(1, -1));
		p.addVertex(new Point2D(7, 1));
		p.addVertex(new Point2D(6, 2));
		p.addVertex(new Point2D(4, 6));
		p.addVertex(new Point2D(2, 5));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 1);
		assertTrue(res.get(0).countVertices() == 5);
	}

	@Test
	public void intersect_ForPolyTouchedByLine() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(7, 1), new Vector2D(-1, -1));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(1, -1));
		p.addVertex(new Point2D(7, 1));
		p.addVertex(new Point2D(6, 2));
		p.addVertex(new Point2D(4, 6));
		p.addVertex(new Point2D(2, 5));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 1);
		assertTrue(res.get(0).countVertices() == 5);
	}

	@Test
	public void intersect_ForPolyIntersectedByLineWithAllButOnePointOnSameSide() {
		InfiniteLine2D l = new InfiniteLine2D(new Point2D(6, -2), new Vector2D(1, 4));
		Polygon2D p = new Polygon2D();
		p.addVertex(new Point2D(1, -1));
		p.addVertex(new Point2D(8, 1));
		p.addVertex(new Point2D(6, 2));
		p.addVertex(new Point2D(4, 6));
		p.addVertex(new Point2D(2, 5));

		List<Polygon2D> res = ConvexPolygonLineCut2D.cut(p, l);

		assertTrue(res.size() == 2);
		assertTrue(res.get(0).countVertices() == 3);
		assertTrue(res.get(1).countVertices() == 6);
	}
}
