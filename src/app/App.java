package app;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import geometry.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import map.Map;
import view2d.MapScene;


public class App extends Application {
	public static void main(String passes[]) {
		launch(passes);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("The Map App");
		primaryStage.setScene(makeScene());
		primaryStage.show();
	}
	
	private Scene makeScene() {
//		return makePoissonDiscSampleScene();
//		return makeVoronoiScene();
		return makeMapScene();
	}
	
	private Scene makeMapScene() {
		final double strokeWidth = 0.01;
		MapScene scene = new MapScene(1100, 1100);

		Rect2D bounds = new Rect2D(0, 0, 100, 100);
		Map map = new Map(bounds);
		map.generate();
		
		scene.addPolygons(map.tileShapes(), Color.web("359BFF"),
				Color.web("black", 1.0), strokeWidth);

		scene.scale(10);
		return scene.scene();
	}

	private Scene makeVoronoiScene() {
		final boolean showSamples = false;
		final boolean showVoronoi = true;
		final boolean showDelauney = false;
		final boolean showBounds = true;
		
		final double strokeWidth = 0.05;
		MapScene scene = new MapScene(1100, 1100);

		Rect2D bounds = new Rect2D(0, 0, 100, 100);
//		List<Point2D> samples = genSamplePoints(10, bounds);
		List<Point2D> samples = genPoissonSamplePoints(bounds);

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
		PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist);
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
	
	private static List<Point2D> genPoissonSamplePoints(Rect2D bounds) {
		PoissonDiscSampling sampler = new PoissonDiscSampling(bounds, 0.5);
		return sampler.generate();
	}
	
	private static List<Point2D> genUniformSamplePoints(int num, Rect2D bounds) {
		// Collect samples in set to prevent duplicates. 
		Set<Point2D> pts = new HashSet<Point2D>();
		while (pts.size() < num)
			pts.add(genSamplePoint(bounds));
		return new ArrayList<Point2D>(pts);
	}
	
	private static Point2D genSamplePoint(Rect2D bounds) {
		return new Point2D(
				genValue(bounds.left(), bounds.right()),
				genValue(bounds.top(), bounds.bottom()));
	}
	
	private static double genValue(double min, double max) {
		return min + (max - min) * Math.random();
	}
}
