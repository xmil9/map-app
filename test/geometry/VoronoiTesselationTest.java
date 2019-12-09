package geometry;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


public class VoronoiTesselationTest {

	@Test
	public void tesselate_ForNoPoints() {
		VoronoiTesselation vt = new VoronoiTesselation(new HashSet<Point2D>());
		List<VoronoiRegion> regions = vt.tesselate();
		assertTrue(regions.isEmpty());
	}

	@Test
	public void tesselate_ForOnePointWithBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		Rect2D border = new Rect2D(0, -1, 3, 6);
		VoronoiTesselation vt = new VoronoiTesselation(samples, border);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 1);
		assertTrue(regions.get(0).hasVertex(border.leftTop()));
		assertTrue(regions.get(0).hasVertex(border.rightTop()));
		assertTrue(regions.get(0).hasVertex(border.rightBottom()));
		assertTrue(regions.get(0).hasVertex(border.leftBottom()));
	}

	@Test
	public void tesselate_ForOnePointWithBoundingBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 1);
		assertTrue(regions.get(0).countVertices() == 1);
		assertTrue(regions.get(0).hasVertex(new Point2D(1, 2)));
	}

	@Test
	public void tesselate_ForOnePointWithOffsetBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		Point2D pt = new Point2D(1, 2); 
		samples.add(pt);
		VoronoiTesselation vt = new VoronoiTesselation(samples, 2);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 1);
		assertTrue(regions.get(0).hasVertex(pt.offset(-2, -2)));
		assertTrue(regions.get(0).hasVertex(pt.offset(-2, 2)));
		assertTrue(regions.get(0).hasVertex(pt.offset(2, -2)));
		assertTrue(regions.get(0).hasVertex(pt.offset(2, 2)));
	}

	@Test
	public void tesselate_ForTwoPointsWithBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(2, 4));
		Rect2D border = new Rect2D(0, -1, 3, 6);
		VoronoiTesselation vt = new VoronoiTesselation(samples, border);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 2);
		assertTrue(regions.get(0).countVertices() == 4);
		assertTrue(regions.get(1).countVertices() == 4);
	}

	@Test
	public void tesselate_ForTwoPointsWithBoundingBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(2, 4));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 2);
		assertTrue(regions.get(0).countVertices() == 4);
		assertTrue(regions.get(1).countVertices() == 4);
	}

	@Test
	public void tesselate_ForTwoPointsWithOffsetBoundingBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(2, 4));
		VoronoiTesselation vt = new VoronoiTesselation(samples, 2);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 2);
		assertTrue(regions.get(0).countVertices() == 4);
		assertTrue(regions.get(1).countVertices() == 4);
	}

	@Test
	public void tesselate_ForThreePoints() {
		// Very special case! The circum-center of the single Delauney
		// triangle lies outside the border bounds. This causes some
		// special situations in the code that find the Voronoi edges.
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(2, 4));
		samples.add(new Point2D(-2, 1.4));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 3);
		assertTrue(regions.get(0).countVertices() == 5);
		assertTrue(regions.get(1).countVertices() == 3);
		assertTrue(regions.get(2).countVertices() == 4);
	}

	@Test
	public void tesselate_ForThreePointsWithBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(2, 4));
		samples.add(new Point2D(-2, 1.4));
		Rect2D border = new Rect2D(-3, -1, 4, 5);
		VoronoiTesselation vt = new VoronoiTesselation(samples, border);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 3);
		assertTrue(regions.get(0).countVertices() == 4);
		assertTrue(regions.get(1).countVertices() == 4);
		assertTrue(regions.get(2).countVertices() == 5);
	}

	@Test
	public void tesselate_ForRect() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(-1, -2));
		samples.add(new Point2D(-1, 3));
		samples.add(new Point2D(5, 3));
		samples.add(new Point2D(5, -2));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 4);
		assertTrue(regions.get(0).countVertices() == 4);
		assertTrue(regions.get(1).countVertices() == 4);
		assertTrue(regions.get(2).countVertices() == 4);
		assertTrue(regions.get(3).countVertices() == 4);
	}

	@Test
	public void tesselate_ForRectWithBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(-1, -2));
		samples.add(new Point2D(-1, 3));
		samples.add(new Point2D(5, 3));
		samples.add(new Point2D(5, -2));
		Rect2D border = new Rect2D(-10, -10, 10, 10);
		VoronoiTesselation vt = new VoronoiTesselation(samples, border);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 4);
		assertTrue(regions.get(0).countVertices() == 4);
		assertTrue(regions.get(1).countVertices() == 4);
		assertTrue(regions.get(2).countVertices() == 4);
		assertTrue(regions.get(3).countVertices() == 4);
	}
}
