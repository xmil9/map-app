package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.*;

import geometry.*;
import map.Map;
import map.MapGeometryGenerator;
import map.PerlinTopography;
import view2d.MapScene;


public class App extends Application {
	
	public class Spec {
		public Long randSeed = 1234567890L;
		// View specs.
		public int viewWidth = 2100;
		public int viewHeight = 1100;
		public double seaLevel = 0.2;
		public double scaleFactor = 10.0;
		public MapScene.ElevationRendering elevRendering =
				MapScene.ElevationRendering.NODE_BASED;
		public boolean showWaterDepth = true;
		public boolean hasFirmShoreline = false;
		// Model specs.
		public int mapWidth = 300;
		public int mapHeight = 100;
		// Smaller distance => smaller and more tiles.
		public double minSampleDistance = .5;
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
				appSpec.seaLevel, appSpec.scaleFactor, appSpec.elevRendering,
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
	private double sceneScale;
	private MapScene mapScene;
	private boolean isPanning;
	private double mapPosAtMouseDownX;
	private double mapPosAtMouseDownY;
	private double mouseDownX;
	private double mouseDownY;
	private Timer zoomTimer = new Timer();
	
	public static void main(String passes[]) {
		launch(passes);
	}

	@Override
	public void start(Stage primaryStage) {
		spec = new Spec();
		rand = (spec.randSeed != null) ? new Random(spec.randSeed) : new Random();
		sceneScale = spec.scaleFactor;
		mapScene = makeScene();
		
		primaryStage.setTitle("The Map App");
		primaryStage.setScene(mapScene.scene());
		primaryStage.show();
	}
	
	private MapScene makeScene() {
//		return makePoissonDiscSampleScene();
//		return makeVoronoiScene();
//		return makeMapSceneWithShapes();
//		return makeTestMapScene();
		MapScene mapScene = makeMapScene();
		Scene scene = mapScene.scene();
		Node mapNode = mapScene.mapNode();
		mapNode.setCache(true);
		mapNode.setCacheHint(CacheHint.SPEED);
		// Mouse wheel zooming.
		mapNode.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                sceneScale *= calcZoomFactor(sceneScale, event.getDeltaY() > 0);
                scheduleZoomTask();
                event.consume();
            }
        });
		// Mouse panning.
		mapNode.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event)
            {
                mouseDownX = event.getSceneX();
                mouseDownY = event.getSceneY();
                mapPosAtMouseDownX = mapNode.getTranslateX();
                mapPosAtMouseDownY = mapNode.getTranslateY();
            }
        });
		mapNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event)
            {
            	mapNode.setTranslateX(
            			mapPosAtMouseDownX + event.getSceneX() - mouseDownX);
            	mapNode.setTranslateY(
            			mapPosAtMouseDownY + event.getSceneY() - mouseDownY);
                event.consume();
            }
        });
		
		return mapScene;
	}
	
	private static double calcZoomFactor(double curScale, boolean zoomIn) {
		// Zoom faster when zoomed in deeper.
		double delta = (curScale > 20.0) ? .2 : .1;
		if (!zoomIn)
			delta *= -1;
		return 1.0 + delta;
	}
	
	// Schedules the processing of a zoom event for a brief interval in the
	// future. This aggregates the processing of multiple quick zoom events
	// into one operation avoiding unnecessary updates.
	private void scheduleZoomTask() {
		// Clear previously scheduled task.
        zoomTimer.cancel();
        zoomTimer.purge();
        
        // Schedule new task.
        final int ZOOM_DELAY = 50;
        zoomTimer = new Timer();
        zoomTimer.schedule(new TimerTask() {
    		@Override
    		public void run() {
    			// Run the scaling of the scene on the UI thread.
    			Platform.runLater(() -> {
                    mapScene.scale(sceneScale);
    			});
    		}
    	}, ZOOM_DELAY);
	}
	
	private MapScene makeMapScene() {
		Map map = new Map(makeModelSpec(spec), rand);
		map.generate();

		MapScene scene = new MapScene(makeViewSpec(spec));
		scene.addMap(map);
		scene.scale(spec.scaleFactor);
		return scene;
	}
	
	private MapScene makeMapSceneWithShapes() {
		Map map = new Map(makeModelSpec(spec), rand);
		map.generate();
		
		final double strokeWidth = 0.03;
		MapScene mapScene = new MapScene(makeViewSpec(spec));
		mapScene.addPolygons(map.tileShapes(), Color.web("359BAF"),
				Color.web("black", 1.0), strokeWidth);
		mapScene.scale(spec.scaleFactor);
		return mapScene;
	}
	
	private MapScene makeTestMapScene() {
		final double strokeWidth = 0.03;
		MapScene mapScene = new MapScene(makeViewSpec(spec));

		var bounds = new Rect2D(10, 10, 15, 15);
		var map = new Map(null, rand);
		var geomSpec = new MapGeometryGenerator.Spec(bounds, 1, 30);
		var gen = new MapGeometryGenerator(map, geomSpec);
		
		List<Point2D> samples = new ArrayList<Point2D>();
		samples.add(new Point2D(12.066096133148083, 14.952071865649085));
		samples.add(new Point2D(10.359849814108108, 14.049893453244641));
		samples.add(new Point2D(13.653193719274324, 14.339350751718165));
		samples.add(new Point2D(11.84474718538761, 13.181846436135407));
		samples.add(new Point2D(13.456819983470801, 12.846725798122872));
		samples.add(new Point2D(14.594267081916872, 12.774468009475912));
		samples.add(new Point2D(11.47279168687508, 11.606484000770104));
		samples.add(new Point2D(10.198911427928012, 12.149143937412557));
		samples.add(new Point2D(13.483368093737289, 11.083084116266384));
		samples.add(new Point2D(14.849570135024054, 10.946546502471405));
		samples.add(new Point2D(11.557253434315392, 10.25812270703529));
		samples.add(new Point2D(10.175640410810594, 10.537547103975443));

		Map.Representation rep = gen.generate(samples);
		
		List<Polygon2D> shapes = new ArrayList<Polygon2D>(rep.countTiles());
		for (int i = 0; i < rep.countTiles(); ++i)
			shapes.add(rep.tile(i).shape);
		
		mapScene.addPolygons(shapes, Color.web("359BAF"),
				Color.web("black", 1.0), strokeWidth);
		mapScene.scale(spec.scaleFactor);
		return mapScene;
	}

	private MapScene makeVoronoiScene() {
		final boolean showSamples = false;
		final boolean showVoronoi = true;
		final boolean showDelauney = true;
		final boolean showBounds = true;
		
		final double strokeWidth = 0.05;
		MapScene mapScene = new MapScene(makeViewSpec(spec));

		Rect2D bounds = new Rect2D(0, 0, 100, 100);
//		List<Point2D> samples = genSamplePoints(10, bounds, rand);
//		List<Point2D> samples = genPoissonSamplePoints(bounds, rand);
		List<Point2D> samples = new ArrayList<Point2D>();
		samples.add(new Point2D(12.066096133148083, 14.952071865649085));
		samples.add(new Point2D(10.359849814108108, 14.049893453244641));
		samples.add(new Point2D(13.653193719274324, 14.339350751718165));
		samples.add(new Point2D(11.84474718538761, 13.181846436135407));
		samples.add(new Point2D(13.456819983470801, 12.846725798122872));
		samples.add(new Point2D(14.594267081916872, 12.774468009475912));
		samples.add(new Point2D(11.47279168687508, 11.606484000770104));
		samples.add(new Point2D(10.198911427928012, 12.149143937412557));
		samples.add(new Point2D(13.483368093737289, 11.083084116266384));
		samples.add(new Point2D(14.849570135024054, 10.946546502471405));
		samples.add(new Point2D(11.557253434315392, 10.25812270703529));
		samples.add(new Point2D(10.175640410810594, 10.537547103975443));

		VoronoiTesselation voronoi = new VoronoiTesselation(samples, bounds);
		List<VoronoiTile> regions = voronoi.tesselate();

		if (showSamples) {
			List<Point2D> ptList = new ArrayList<Point2D>();
			for (Point2D pt : samples)
				ptList.add(pt);
			mapScene.addPoints(ptList, Color.web("red", 1.0), strokeWidth * 5);
		}		
		
		if (showBounds) {
			List<Rect2D> rects = new ArrayList<Rect2D>();
			rects.add(bounds);
			mapScene.addRects(rects, Color.web("blue", 1.0), strokeWidth);
		}		
		
		if (showVoronoi) {
			List<Polygon2D> polys = new ArrayList<Polygon2D>();
			for (VoronoiTile vr : regions)
				polys.add(vr.outline);
			mapScene.addPolygons(polys, null, Color.web("red", 1.0), strokeWidth);
		}

		if (showDelauney) {
			if (voronoi.getTriangulation() != null)
				mapScene.addTriangles(voronoi.getTriangulation(), Color.web("black", 1.0),
						strokeWidth);
		}		
		
		mapScene.scale(spec.scaleFactor);
		return mapScene;
	}
	
	private MapScene makePoissonDiscSampleScene() {
		final boolean showSamples = true;
		final boolean showDomain = true;
		final boolean showMinDistance = true;
		
		final double strokeWidth = 0.05;
		MapScene mapScene = new MapScene(makeViewSpec(spec));

		Rect2D domain = new Rect2D(0, 0, 100, 100);
		double minDist = 3;
		int numCandidatePts = 30;
		PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist,
				numCandidatePts, rand);
		List<Point2D> samples = sampler.generate();
		
		if (showSamples) {
			mapScene.addPoints(samples, Color.web("red", 1.0), strokeWidth * 5);
		}		

		if (showMinDistance) {
			List<Circle2D> circleList = new ArrayList<Circle2D>();
			for (Point2D pt : samples)
				circleList.add(new Circle2D(pt, minDist));
			mapScene.addCircles(circleList, Color.web("black", 1.0), strokeWidth);
		}		
		
		if (showDomain) {
			List<Rect2D> rects = new ArrayList<Rect2D>();
			rects.add(domain);
			mapScene.addRects(rects, Color.web("blue", 1.0), strokeWidth);
		}		
		
		mapScene.scale(spec.scaleFactor);
		return mapScene;
	}
	
	private static List<Point2D> genPoissonSamplePoints(Rect2D bounds, Random rand) {
		PoissonDiscSampling sampler = new PoissonDiscSampling(bounds, 0.5, 30, rand);
		return sampler.generate();
	}
	
	private static List<Point2D> genUniformSamplePoints(int num, Rect2D bounds,
			Random rand) {
		// Collect samples in set to prevent duplicates. 
		List<Point2D> pts = new ArrayList<Point2D>(num);
		while (pts.size() < num) {
			Point2D pt = genSamplePoint(bounds, rand);
			if (!pts.contains(pt))
				pts.add(pt);
		}
		return pts;
	}
	
	private static Point2D genSamplePoint(Rect2D bounds, Random rand) {
		return new Point2D(
				genValue(bounds.left(), bounds.right(), rand),
				genValue(bounds.top(), bounds.bottom(), rand));
	}
	
	private static double genValue(double min, double max, Random rand) {
		return min + (max - min) * rand.nextDouble();
	}
}
