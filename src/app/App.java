package app;

import java.util.Random;

import javafx.application.*;
import javafx.stage.*;

import geometry.*;
import map.Map;
import map.MapGeometryGenerator;
import map.PerlinTopography;
import view2d.MapScene;
import view2d.Texture;


public class App extends Application {
	
	public class Spec {
		public Long randSeed = 1234567890L;
		// View specs.
		public int viewWidth = 2100;
		public int viewHeight = 1100;
		public double scaleFactor = 10.0;
		public double seaLevel = 0.2;
		public Texture.ElevationRendering elevRendering =
				Texture.ElevationRendering.NODE_BASED;
		public boolean showWaterDepth = true;
		public boolean hasFirmShoreline = false;
		public boolean cacheMap = true;
		// Model specs.
		public int mapWidth = 200;
		public int mapHeight = 75;
		// Smaller distance => smaller and more tiles.
		public double minSampleDistance = .8;
		// More candidates => more evenly spaced sample points but slower generation.
		public int numSampleCandidates = 30;
		// More octaves => Wider and wider areas are affected by values of
		// individual noise values of higher octave passes. Leads to zoomed in
		// appearance on features of the map.
		public int numOctaves = 7;
		// Larger persistence => Larger and smoother features.
		// Smaller persistence => Smaller and choppier features.
		public double persistence = 2;
	}

	// Creates a view spec from an app-wide spec.
	private static MapScene.Spec makeViewSpec(Spec appSpec) {
		return new MapScene.Spec(appSpec.viewWidth, appSpec.viewHeight,
				appSpec.scaleFactor, appSpec.seaLevel, appSpec.elevRendering,
				appSpec.showWaterDepth, appSpec.hasFirmShoreline);
	}
	
	// Creates a model spec from an app-wide spec.
	private static Map.Spec makeModelSpec(Spec appSpec) {
		Rect2D bounds = new Rect2D(0, 0, appSpec.mapWidth, appSpec.mapHeight);
		return new Map.Spec(
				new MapGeometryGenerator.Spec(bounds, appSpec.minSampleDistance,
						appSpec.numSampleCandidates),
				new PerlinTopography.Spec(bounds, appSpec.numOctaves,
						appSpec.persistence));
	}
	
	///////////////
	
	Spec spec;
	// App-wide random number generator. Must be used everywhere to guarantee
	// deterministic map generation.
	private Random rand;
	private MapScene mapScene;
	
	public static void main(String passes[]) {
		launch(passes);
	}

	@Override
	public void start(Stage primaryStage) {
		spec = new Spec();
		rand = (spec.randSeed != null) ? new Random(spec.randSeed) : new Random();
		mapScene = makeMapScene();
		
		primaryStage.setTitle("The Map App");
		primaryStage.setScene(mapScene.scene());
		primaryStage.show();
	}
	
	private MapScene makeMapScene() {
		Map map = new Map(makeModelSpec(spec), rand);
		map.generate();

		MapScene scene = new MapScene(makeViewSpec(spec));
		scene.addMap(map);
		scene.scale(spec.scaleFactor);
		scene.enableCaching(spec.cacheMap);
		return scene;
	}
	
//	private MapScene makeMapSceneWithShapes() {
//		Map map = new Map(makeModelSpec(spec), rand);
//		map.generate();
//		
//		final double strokeWidth = 0.03;
//		MapScene mapScene = new MapScene(makeViewSpec(spec));
//		mapScene.addPolygons(map.tileShapes(), Color.web("359BAF"),
//				Color.web("black", 1.0), strokeWidth);
//		mapScene.scale(spec.scaleFactor);
//		return mapScene;
//	}
//	
//	private MapScene makeTestMapScene() {
//		final double strokeWidth = 0.03;
//		MapScene mapScene = new MapScene(makeViewSpec(spec));
//
//		var bounds = new Rect2D(10, 10, 15, 15);
//		var map = new Map(null, rand);
//		var geomSpec = new MapGeometryGenerator.Spec(bounds, 1, 30);
//		var gen = new MapGeometryGenerator(map, geomSpec);
//		
//		List<Point2D> samples = new ArrayList<Point2D>();
//		samples.add(new Point2D(12.066096133148083, 14.952071865649085));
//		samples.add(new Point2D(10.359849814108108, 14.049893453244641));
//		samples.add(new Point2D(13.653193719274324, 14.339350751718165));
//		samples.add(new Point2D(11.84474718538761, 13.181846436135407));
//		samples.add(new Point2D(13.456819983470801, 12.846725798122872));
//		samples.add(new Point2D(14.594267081916872, 12.774468009475912));
//		samples.add(new Point2D(11.47279168687508, 11.606484000770104));
//		samples.add(new Point2D(10.198911427928012, 12.149143937412557));
//		samples.add(new Point2D(13.483368093737289, 11.083084116266384));
//		samples.add(new Point2D(14.849570135024054, 10.946546502471405));
//		samples.add(new Point2D(11.557253434315392, 10.25812270703529));
//		samples.add(new Point2D(10.175640410810594, 10.537547103975443));
//
//		Map.Representation rep = gen.generate(samples);
//		
//		List<Polygon2D> shapes = new ArrayList<Polygon2D>(rep.countTiles());
//		for (int i = 0; i < rep.countTiles(); ++i)
//			shapes.add(rep.tile(i).shape);
//		
//		mapScene.addPolygons(shapes, Color.web("359BAF"),
//				Color.web("black", 1.0), strokeWidth);
//		mapScene.scale(spec.scaleFactor);
//		return mapScene;
//	}
//
//	private MapScene makeVoronoiScene() {
//		final boolean showSamples = false;
//		final boolean showVoronoi = true;
//		final boolean showDelauney = true;
//		final boolean showBounds = true;
//		
//		final double strokeWidth = 0.05;
//		MapScene mapScene = new MapScene(makeViewSpec(spec));
//
//		Rect2D bounds = new Rect2D(0, 0, 100, 100);
////		List<Point2D> samples = genSamplePoints(10, bounds, rand);
////		List<Point2D> samples = genPoissonSamplePoints(bounds, rand);
//		List<Point2D> samples = new ArrayList<Point2D>();
//		samples.add(new Point2D(12.066096133148083, 14.952071865649085));
//		samples.add(new Point2D(10.359849814108108, 14.049893453244641));
//		samples.add(new Point2D(13.653193719274324, 14.339350751718165));
//		samples.add(new Point2D(11.84474718538761, 13.181846436135407));
//		samples.add(new Point2D(13.456819983470801, 12.846725798122872));
//		samples.add(new Point2D(14.594267081916872, 12.774468009475912));
//		samples.add(new Point2D(11.47279168687508, 11.606484000770104));
//		samples.add(new Point2D(10.198911427928012, 12.149143937412557));
//		samples.add(new Point2D(13.483368093737289, 11.083084116266384));
//		samples.add(new Point2D(14.849570135024054, 10.946546502471405));
//		samples.add(new Point2D(11.557253434315392, 10.25812270703529));
//		samples.add(new Point2D(10.175640410810594, 10.537547103975443));
//
//		VoronoiTesselation voronoi = new VoronoiTesselation(samples, bounds);
//		List<VoronoiTile> regions = voronoi.tesselate();
//
//		if (showSamples) {
//			List<Point2D> ptList = new ArrayList<Point2D>();
//			for (Point2D pt : samples)
//				ptList.add(pt);
//			mapScene.addPoints(ptList, Color.web("red", 1.0), strokeWidth * 5);
//		}		
//		
//		if (showBounds) {
//			List<Rect2D> rects = new ArrayList<Rect2D>();
//			rects.add(bounds);
//			mapScene.addRects(rects, Color.web("blue", 1.0), strokeWidth);
//		}		
//		
//		if (showVoronoi) {
//			List<Polygon2D> polys = new ArrayList<Polygon2D>();
//			for (VoronoiTile vr : regions)
//				polys.add(vr.outline);
//			mapScene.addPolygons(polys, null, Color.web("red", 1.0), strokeWidth);
//		}
//
//		if (showDelauney) {
//			if (voronoi.getTriangulation() != null)
//				mapScene.addTriangles(voronoi.getTriangulation(), Color.web("black", 1.0),
//						strokeWidth);
//		}		
//		
//		mapScene.scale(spec.scaleFactor);
//		return mapScene;
//	}
//	
//	private MapScene makePoissonDiscSampleScene() {
//		final boolean showSamples = true;
//		final boolean showDomain = true;
//		final boolean showMinDistance = true;
//		
//		final double strokeWidth = 0.05;
//		MapScene mapScene = new MapScene(makeViewSpec(spec));
//
//		Rect2D domain = new Rect2D(0, 0, 100, 100);
//		double minDist = 3;
//		int numCandidatePts = 30;
//		PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist,
//				numCandidatePts, rand);
//		List<Point2D> samples = sampler.generate();
//		
//		if (showSamples) {
//			mapScene.addPoints(samples, Color.web("red", 1.0), strokeWidth * 5);
//		}		
//
//		if (showMinDistance) {
//			List<Circle2D> circleList = new ArrayList<Circle2D>();
//			for (Point2D pt : samples)
//				circleList.add(new Circle2D(pt, minDist));
//			mapScene.addCircles(circleList, Color.web("black", 1.0), strokeWidth);
//		}		
//		
//		if (showDomain) {
//			List<Rect2D> rects = new ArrayList<Rect2D>();
//			rects.add(domain);
//			mapScene.addRects(rects, Color.web("blue", 1.0), strokeWidth);
//		}		
//		
//		mapScene.scale(spec.scaleFactor);
//		return mapScene;
//	}
//	
//	private static List<Point2D> genPoissonSamplePoints(Rect2D bounds, Random rand) {
//		PoissonDiscSampling sampler = new PoissonDiscSampling(bounds, 0.5, 30, rand);
//		return sampler.generate();
//	}
//	
//	private static List<Point2D> genUniformSamplePoints(int num, Rect2D bounds,
//			Random rand) {
//		// Collect samples in set to prevent duplicates. 
//		List<Point2D> pts = new ArrayList<Point2D>(num);
//		while (pts.size() < num) {
//			Point2D pt = genSamplePoint(bounds, rand);
//			if (!pts.contains(pt))
//				pts.add(pt);
//		}
//		return pts;
//	}
//	
//	private static Point2D genSamplePoint(Rect2D bounds, Random rand) {
//		return new Point2D(
//				genValue(bounds.left(), bounds.right(), rand),
//				genValue(bounds.top(), bounds.bottom(), rand));
//	}
//	
//	private static double genValue(double min, double max, Random rand) {
//		return min + (max - min) * rand.nextDouble();
//	}
}
