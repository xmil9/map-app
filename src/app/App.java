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
		MapScene map = new MapScene(500, 400);

		Set<Point2D> points = new HashSet<Point2D>();
		points.add(new Point2D(1, 2));
		points.add(new Point2D(2, 4));
		points.add(new Point2D(-2, 1.4));
//		points.add(new Point2D(10, 10));
//		points.add(new Point2D(100, 100));
//		points.add(new Point2D(27, 67));
//		points.add(new Point2D(44, 23));
//		points.add(new Point2D(78, 51));
//		points.add(new Point2D(15, 44));
//		points.add(new Point2D(78, 89));
//		points.add(new Point2D(75, 56));
//		points.add(new Point2D(22, 93));

//		DelauneyTriangulation delauney = new DelauneyTriangulation(points);
//		List<Triangle2D> triangles = delauney.triangulate();
//		map.addTriangles(triangles, Color.web("orange", 1.0));

		Rect2D bounds = new Rect2D(-2, 1.4, 2, 4);
		//Rect2D bounds = new Rect2D(-3, -1, 4, 5);
		List<Rect2D> rects = new ArrayList<Rect2D>();
		rects.add(bounds);
		map.addRects(rects, Color.web("blue", 1.0));
		
		VoronoiTesselation voronoi = new VoronoiTesselation(points/*, bounds*/);
		List<VoronoiRegion> regions = voronoi.tesselate();
		
		List<Polygon2D> polys = new ArrayList<Polygon2D>();
		for (VoronoiRegion vr : regions)
			polys.add(vr.border());
		map.addPolygons(polys, Color.web("red", 1.0));

		List<Point2D> ptList = new ArrayList<Point2D>();
		for (Point2D pt : points)
			ptList.add(pt);
		map.addPoints(ptList, Color.web("red", 1.0));
		
		if (voronoi.getTriangulation() != null)
			map.addTriangles(voronoi.getTriangulation(), Color.web("black", 1.0));
		
		map.scale(30);
		return map.scene();
	}
}
