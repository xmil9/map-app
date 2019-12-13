package geometry;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


public class VoronoiTesselationTest {

	// Verifies that a given region contains given points.
	// Avoids depending on the order of points in the region.
	private static boolean hasVertices(VoronoiRegion r, Point2D[] pts) {
		if (r.countVertices() != pts.length)
			return false;
		
		for (var pt : pts)
			if (!r.hasVertex(pt))
				return false;
		
		return true;
	}
	
	// Verifies that given regions contains one regions with given points.
	// Avoids depending on the order of regions and the order of points in
	// a region.
	private static boolean hasRegionWithVertices(List<VoronoiRegion> tess,
			Point2D[] pts) {
		for (var region : tess)
			if (hasVertices(region, pts))
				return true;
		return false;
	}
	
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
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				border.leftTop(),
				border.rightTop(),
				border.rightBottom(),
				border.leftBottom()
		}));
	}

	@Test
	public void tesselate_ForOnePointWithBoundingBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 1);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1, 2)
		}));
	}

	@Test
	public void tesselate_ForOnePointWithOffsetBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		Point2D pt = new Point2D(1, 2); 
		samples.add(pt);
		VoronoiTesselation vt = new VoronoiTesselation(samples, 2);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 1);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				pt.offset(-2, -2),
				pt.offset(-2, 2),
				pt.offset(2, -2),
				pt.offset(2, 2)
		}));
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
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(0, -1),
				new Point2D(0, 3.75),
				new Point2D(3, 2.25),
				new Point2D(3, -1)
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(0, 3.75),
				new Point2D(0, 6),
				new Point2D(3, 6),
				new Point2D(3, 2.25)
		}));
	}

	@Test
	public void tesselate_ForTwoPoints() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(2, 4));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 2);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1, 2),
				new Point2D(1, 3.25),
				new Point2D(2, 2.75),
				new Point2D(2, 2)
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1, 3.25),
				new Point2D(1, 4),
				new Point2D(2, 4),
				new Point2D(2, 2.75)
		}));
	}

	@Test
	public void tesselate_ForTwoPointsWithOffsetBoundingBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, 2));
		samples.add(new Point2D(2, 4));
		VoronoiTesselation vt = new VoronoiTesselation(samples, 2);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 2);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-1, 0),
				new Point2D(-1, 4.25),
				new Point2D(4, 1.75),
				new Point2D(4, 0)
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-1, 4.25),
				new Point2D(-1, 6),
				new Point2D(4, 6),
				new Point2D(4, 1.75)
		}));
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
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-0.95999999, 4),
				new Point2D(-0.49999999, 4),
				new Point2D(1.999999999, 2.75),
				new Point2D(2, 1.4),
				new Point2D(-0.43999999, 1.3999999),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-0.5, 4),
				new Point2D(2, 4),
				new Point2D(2, 2.75),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-0.95999999, 4),
				new Point2D(-0.43999999, 1.4),
				new Point2D(-2, 1.4),
				new Point2D(-2, 4),
		}));
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
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(4, 1.75),
				new Point2D(4, -1),
				new Point2D(0.04, -1),
				new Point2D(-1.01111111, 4.25555555),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-1.49499999, 5),
				new Point2D(4, 5),
				new Point2D(3.99999999, 1.75),
				new Point2D(-1.01111111, 4.255555555),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-1.494999999, 5),
				new Point2D(-1.011111111, 4.25555555),
				new Point2D(0.04, -1),
				new Point2D(-3, -1),
				new Point2D(-3, 5),
		}));
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
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-1, 0.5),
				new Point2D(2, 0.5),
				new Point2D(2, -2),
				new Point2D(-1, -2),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2, 3),
				new Point2D(5, 3),
				new Point2D(5, 0.5),
				new Point2D(2, 0.5),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2, 3),
				new Point2D(2, 0.5),
				new Point2D(-1, 0.5),
				new Point2D(-1, 3),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(5, 0.5),
				new Point2D(5, -2),
				new Point2D(2, -2),
				new Point2D(2, 0.5),
		}));
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
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-10, 0.5),
				new Point2D(2, 0.5),
				new Point2D(2, -10),
				new Point2D(-10, -10),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2, 10),
				new Point2D(10, 10),
				new Point2D(10, 0.5),
				new Point2D(2, 0.5),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2, 10),
				new Point2D(2, 0.5),
				new Point2D(-10, 0.5),
				new Point2D(-10, 10),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 0.5),
				new Point2D(10, -10),
				new Point2D(2, -10),
				new Point2D(2, 0.5),
		}));
	}

	@Test
	public void tesselate_ForRectWithCenterPoint() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(-1, -2));
		samples.add(new Point2D(-1, 3));
		samples.add(new Point2D(5, 3));
		samples.add(new Point2D(5, -2));
		samples.add(new Point2D(2, 0.5));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 5);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-1, 0.5),
				new Point2D(-0.541666666, 0.5),
				new Point2D(1.5416666666, -2),
				new Point2D(-1, -2),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1.5416666666, 3),
				new Point2D(2.4583333333, 3),
				new Point2D(4.541666666, 0.5),
				new Point2D(2.458333333, -2),
				new Point2D(1.5416666666, -2),
				new Point2D(-0.541666666, 0.5),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2.4583333333, 3),
				new Point2D(5, 3),
				new Point2D(5, 0.5),
				new Point2D(4.5416666666, 0.5),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1.5416666666, 3),
				new Point2D(-0.5416666666, 0.5),
				new Point2D(-1, 0.5),
				new Point2D(-1, 3),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(5, 0.5),
				new Point2D(5, -2),
				new Point2D(2.4583333333, -2),
				new Point2D(4.5416666666, 0.5),
		}));
	}

	@Test
	public void tesselate_ForRectWithCenterPointAndBorder() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(-1, -2));
		samples.add(new Point2D(-1, 3));
		samples.add(new Point2D(5, 3));
		samples.add(new Point2D(5, -2));
		samples.add(new Point2D(2, 0.5));
		Rect2D border = new Rect2D(-10, -10, 10, 10);
		VoronoiTesselation vt = new VoronoiTesselation(samples, border);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 5);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-10, 0.5),
				new Point2D(-0.541666666, 0.5),
				new Point2D(2, -2.55),
				new Point2D(2, -10),
				new Point2D(-10, -10),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-0.5416666666, 0.5),
				new Point2D(2, 3.55),
				new Point2D(4.541666666, 0.5),
				new Point2D(2, -2.55),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2, 10),
				new Point2D(10, 10),
				new Point2D(10, 0.5),
				new Point2D(4.5416666666, 0.5),
				new Point2D(2, 3.55),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2, 10),
				new Point2D(2, 3.55),
				new Point2D(-0.5416666666, 0.5),
				new Point2D(-10, 0.5),
				new Point2D(-10, 10),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 0.5),
				new Point2D(10, -10),
				new Point2D(2, -10),
				new Point2D(2, -2.55),
				new Point2D(4.5416666666, 0.5),
		}));
	}

	@Test
	public void tesselate_ForFivePoints() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(-1, -1));
		samples.add(new Point2D(-2, 1));
		samples.add(new Point2D(0, 3));
		samples.add(new Point2D(2, 2));
		samples.add(new Point2D(2, 0));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 5);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-2, -0.25),
				new Point2D(0.07142857142, 0.7857142857),
				new Point2D(0.6666666666, -0.9999999999),
				new Point2D(-2, -1),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-2, 3),
				new Point2D(0.1, 0.9),
				new Point2D(0.0714285714, 0.785714285),
				new Point2D(-2, -0.25),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1.25, 3),
				new Point2D(2, 3),
				new Point2D(2, 1),
				new Point2D(0.25, 1),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(2, 1),
				new Point2D(2, -1),
				new Point2D(0.66666666, -1),
				new Point2D(0.0714285714, 0.7857142857),
				new Point2D(0.1, 0.9),
				new Point2D(0.25, 1),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(-2, 3),
				new Point2D(1.25, 3),
				new Point2D(0.25, 1),
				new Point2D(0.1, 0.9),
		}));
	}

	@Test
	public void tesselate_ForFourPointsWithThreeCollinear() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(1, -2));
		samples.add(new Point2D(1, 1));
		samples.add(new Point2D(1, 4));
		samples.add(new Point2D(4, 0));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 4);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1, -0.5),
				new Point2D(2.166666666, -0.5),
				new Point2D(3.166666666, -2),
				new Point2D(1, -2),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1, 2.5),
				new Point2D(3.166666666, 2.5),
				new Point2D(2.166666666, -0.5),
				new Point2D(1, -0.5),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(1, 2.5),
				new Point2D(1, 4),
				new Point2D(4, 4),
				new Point2D(4, 3.125),
				new Point2D(3.166666666, 2.5),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(4, 3.125),
				new Point2D(4, -2),
				new Point2D(3.166666666, -2),
				new Point2D(2.166666666, -0.5),
				new Point2D(3.166666666, 2.5),
		}));
	}

	@Test
	public void tesselate_ForSituationWhereRegionWithDistantEndpointsIntersectsBeforeTheEndpoints() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(10, 10));
		samples.add(new Point2D(44, 23));
		samples.add(new Point2D(15, 44));
		samples.add(new Point2D(22, 93));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 4);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(44, 53.523809523),
				new Point2D(44, 10),
				new Point2D(29.485294117, 10),
				new Point2D(23.60999083, 25.366177818),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 69.714285714),
				new Point2D(10, 93),
				new Point2D(44, 93),
				new Point2D(44, 64.857142857),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 27.367647058),
				new Point2D(23.609990834, 25.3661778185),
				new Point2D(29.485294117, 10),
				new Point2D(10, 10),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 69.7142857142),
				new Point2D(44, 64.857142857),
				new Point2D(44, 53.523809523),
				new Point2D(23.60999083, 25.3661778185),
				new Point2D(10, 27.367647058),
		}));
	}

	@Test
	public void tesselate_ForMorePoints() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(10, 10));
		samples.add(new Point2D(100, 100));
		samples.add(new Point2D(27, 67));
		samples.add(new Point2D(44, 23));
		samples.add(new Point2D(78, 51));
		samples.add(new Point2D(15, 44));
		samples.add(new Point2D(78, 89));
		samples.add(new Point2D(75, 56));
		samples.add(new Point2D(22, 93));
		samples.add(new Point2D(53, 40));
		samples.add(new Point2D(49, 71));
		VoronoiTesselation vt = new VoronoiTesselation(samples);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 11);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(83.235294117, 10),
				new Point2D(29.4852941176, 10),
				new Point2D(23.60999083, 25.3661778185),
				new Point2D(33.718475073, 39.325513196),
				new Point2D(78.693251533, 15.515337423),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(100, 67.6),
				new Point2D(65.01265822, 46.607594936),
				new Point2D(57.898123324, 56.390080428),
				new Point2D(67.65614617, 73.30398671),
				new Point2D(100, 70.363636363),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(50.166666666, 100),
				new Point2D(35.6114864864, 82.136824324),
				new Point2D(10, 77.211538461),
				new Point2D(10, 100),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 27.3676470588),
				new Point2D(23.609990834, 25.3661778185),
				new Point2D(29.485294117, 10),
				new Point2D(10, 10),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(86.25, 100),
				new Point2D(100, 100),
				new Point2D(100, 72.5),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(51.086206896, 100),
				new Point2D(86.25, 100),
				new Point2D(100, 72.5),
				new Point2D(100, 70.363636363),
				new Point2D(67.656146179, 73.30398671),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 61.239130434),
				new Point2D(10, 77.211538461),
				new Point2D(35.611486486486, 82.13682432432),
				new Point2D(40.69627507163, 54.17048710601),
				new Point2D(34.670281995661, 48.36767895878),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(100, 67.6),
				new Point2D(100, 10),
				new Point2D(83.23529411765, 10),
				new Point2D(78.69325153374, 15.515337423312),
				new Point2D(65.0126582278, 46.607594936708),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(78.69325153374, 15.51533742331),
				new Point2D(33.71847507331, 39.32551319648),
				new Point2D(34.67028199566, 48.36767895878),
				new Point2D(40.69627507163, 54.17048710601),
				new Point2D(57.89812332439, 56.39008042895),
				new Point2D(65.01265822784, 46.60759493670),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(50.1666666666, 100),
				new Point2D(51.0862068965, 100),
				new Point2D(67.6561461794, 73.3039867109),
				new Point2D(57.8981233243, 56.3900804289),
				new Point2D(40.6962750716, 54.1704871060),
				new Point2D(35.6114864864, 82.1368243243),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(10, 61.2391304347),
				new Point2D(34.67028199566, 48.36767895878),
				new Point2D(33.71847507331, 39.32551319648),
				new Point2D(23.60999083409, 25.36617781851),
				new Point2D(10, 27.367647058823),
		}));
	}

	@Test
	public void tesselate_ForBorderMuchSmallerThanBoundingBox() {
		Set<Point2D> samples = new HashSet<Point2D>();
		samples.add(new Point2D(10, 10));
		samples.add(new Point2D(100, 100));
		samples.add(new Point2D(27, 67));
		samples.add(new Point2D(44, 23));
		samples.add(new Point2D(78, 51));
		samples.add(new Point2D(15, 44));
		samples.add(new Point2D(78, 89));
		samples.add(new Point2D(75, 56));
		samples.add(new Point2D(22, 93));
		samples.add(new Point2D(53, 40));
		samples.add(new Point2D(49, 71));
		Rect2D border = new Rect2D(30, 30, 60, 80);
		VoronoiTesselation vt = new VoronoiTesselation(samples, border);
		List<VoronoiRegion> regions = vt.tesselate();
		
		assertTrue(regions.size() == 6);
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(30, 34.1904761904),
				new Point2D(33.7184750733, 39.3255131964),
				new Point2D(51.333333333, 30),
				new Point2D(30, 30),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(60, 53.5),
				new Point2D(57.89812332439, 56.3900804289),
				new Point2D(60, 60.033333333),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(36, 80),
				new Point2D(40.6962750716, 54.1704871060),
				new Point2D(34.67028199566, 48.367678958),
				new Point2D(30, 50.80434782608),
				new Point2D(30, 80),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(60, 53.5),
				new Point2D(60, 30),
				new Point2D(51.333333333, 30),
				new Point2D(33.7184750733, 39.3255131964),
				new Point2D(34.6702819956, 48.3676789587),
				new Point2D(40.6962750716, 54.1704871060),
				new Point2D(57.8981233243, 56.3900804289),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(36, 80),
				new Point2D(60, 80),
				new Point2D(60, 60.0333333333),
				new Point2D(57.898123324396785, 56.39008042895442),
				new Point2D(40.69627507163324, 54.17048710601719),
		}));
		assertTrue(hasRegionWithVertices(regions, new Point2D[] {
				new Point2D(30, 50.8043478260),
				new Point2D(34.67028199566, 48.3676789587),
				new Point2D(33.71847507331, 39.3255131964),
				new Point2D(30, 34.1904761904),
		}));
	}
}
