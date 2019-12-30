package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

import geometry.*;
import map.Map;
import map.MapGeometryGenerator;
import map.BlobContinentGenerator;
import map.ContinentBasedTopography;
import view2d.MapScene;


public class App extends Application {
	
	private Random rand;
	
	public static void main(String passes[]) {
		launch(passes);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("The Map App");
		primaryStage.setScene(makeScene(null));//123456L));
		primaryStage.show();
	}
	
	private Scene makeScene(Long randSeed) {
		rand = (randSeed != null) ? new Random(randSeed) : new Random();
		
//		return makePoissonDiscSampleScene();
//		return makeVoronoiScene();
//		return makeMapSceneWithShapes();
//		return makeTestMapScene();
		return makeMapScene();
	}
	
	private Scene makeMapScene() {
		MapScene scene = new MapScene(1100, 1100);

		Rect2D bounds = new Rect2D(0, 0, 100, 100);
		Map.Spec spec = new Map.Spec(
				new MapGeometryGenerator.Spec(bounds, 0.5, 30),
				new ContinentBasedTopography.Spec(
						rand, new BlobContinentGenerator(rand)));
		Map map = new Map(spec, rand);
		map.generate();

		scene.addMap(map);

		scene.scale(10);
		return scene.scene();
	}
	
	private Scene makeMapSceneWithShapes() {
		final double strokeWidth = 0.03;
		MapScene scene = new MapScene(1100, 1100);

		Rect2D bounds = new Rect2D(0, 0, 100, 100);
		Map.Spec spec = new Map.Spec(
				new MapGeometryGenerator.Spec(bounds, 1, 30),
				new ContinentBasedTopography.Spec(
						rand, new BlobContinentGenerator(rand)));
		Map map = new Map(spec, rand);
		map.generate();
		
		scene.addPolygons(map.tileShapes(), Color.web("359BAF"),
				Color.web("black", 1.0), strokeWidth);

		scene.scale(10);
		return scene.scene();
	}
	
	private Scene makeTestMapScene() {
		final double strokeWidth = 0.03;
		MapScene scene = new MapScene(1100, 1100);

		var bounds = new Rect2D(10, 10, 15, 15);
		var map = new Map(null, rand);
		var spec = new MapGeometryGenerator.Spec(bounds, 1, 30);
		var gen = new MapGeometryGenerator(map, spec);
		
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
		
		scene.addPolygons(shapes, Color.web("359BAF"),
				Color.web("black", 1.0), strokeWidth);

		scene.scale(10);
		return scene.scene();
	}

	private Scene makeVoronoiScene() {
		final boolean showSamples = false;
		final boolean showVoronoi = true;
		final boolean showDelauney = true;
		final boolean showBounds = true;
		
		final double strokeWidth = 0.05;
		MapScene scene = new MapScene(1100, 1100);

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
			scene.addPoints(ptList, Color.web("red", 1.0), strokeWidth * 5);
		}		
		
		if (showBounds) {
			List<Rect2D> rects = new ArrayList<Rect2D>();
			rects.add(bounds);
			scene.addRects(rects, Color.web("blue", 1.0), strokeWidth);
		}		
		
		if (showVoronoi) {
			List<Polygon2D> polys = new ArrayList<Polygon2D>();
			for (VoronoiTile vr : regions)
				polys.add(vr.outline);
			scene.addPolygons(polys, null, Color.web("red", 1.0), strokeWidth);
		}

		if (showDelauney) {
			if (voronoi.getTriangulation() != null)
				scene.addTriangles(voronoi.getTriangulation(), Color.web("black", 1.0),
						strokeWidth);
		}		
		
		scene.scale(10);
		return scene.scene();
	}
	
	private Scene makePoissonDiscSampleScene() {
		final boolean showSamples = true;
		final boolean showDomain = true;
		final boolean showMinDistance = true;
		
		final double strokeWidth = 0.05;
		MapScene scene = new MapScene(1100, 1100);

		Rect2D domain = new Rect2D(0, 0, 100, 100);
		double minDist = 3;
		int numCandidatePts = 30;
		PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist,
				numCandidatePts, rand);
		List<Point2D> samples = sampler.generate();
		
		if (showSamples) {
			scene.addPoints(samples, Color.web("red", 1.0), strokeWidth * 5);
		}		

		if (showMinDistance) {
			List<Circle2D> circleList = new ArrayList<Circle2D>();
			for (Point2D pt : samples)
				circleList.add(new Circle2D(pt, minDist));
			scene.addCircles(circleList, Color.web("black", 1.0), strokeWidth);
		}		
		
		if (showDomain) {
			List<Rect2D> rects = new ArrayList<Rect2D>();
			rects.add(domain);
			scene.addRects(rects, Color.web("blue", 1.0), strokeWidth);
		}		
		
		scene.scale(10);
		return scene.scene();
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
