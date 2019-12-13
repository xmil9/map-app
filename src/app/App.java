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
import view2d.MapScene;


public class App extends Application {
	public static void main(String passes[]) {
		launch(passes);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("The Map App");
		primaryStage.setScene(makeMapScene());
		primaryStage.show();
	}
	
	private Scene makeMapScene() {
		final boolean showSamples = true;
		final boolean showVoronoi = true;
		final boolean showDelauney = false;
		final boolean showBounds = true;
		
		final double strokeWidth = 0.05;
		MapScene map = new MapScene(500, 400);

		Rect2D bounds = new Rect2D(0, 0, 100, 100);
		Set<Point2D> samples = genSamplePoints(200, bounds);

		VoronoiTesselation voronoi = new VoronoiTesselation(samples, bounds);
		List<VoronoiRegion> regions = voronoi.tesselate();

		if (showSamples) {
			List<Point2D> ptList = new ArrayList<Point2D>();
			for (Point2D pt : samples)
				ptList.add(pt);
			map.addPoints(ptList, Color.web("red", 1.0), strokeWidth * 5);
		}		
		
		if (showBounds) {
			List<Rect2D> rects = new ArrayList<Rect2D>();
			rects.add(bounds);
			map.addRects(rects, Color.web("blue", 1.0), strokeWidth);
		}		
		
		if (showVoronoi) {
			List<Polygon2D> polys = new ArrayList<Polygon2D>();
			for (VoronoiRegion vr : regions)
				polys.add(vr.border());
			map.addPolygons(polys, Color.web("red", 1.0), strokeWidth);
		}

		if (showDelauney) {
			if (voronoi.getTriangulation() != null)
				map.addTriangles(voronoi.getTriangulation(), Color.web("black", 1.0),
						strokeWidth);
		}		
		
		map.scale(10);
		return map.scene();
	}
	
	private static Set<Point2D> genSamplePoints(int num, Rect2D bounds) {
		Set<Point2D> pts = new HashSet<Point2D>();
		while (pts.size() < num)
			pts.add(genSamplePoint(bounds));
		return pts;
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
