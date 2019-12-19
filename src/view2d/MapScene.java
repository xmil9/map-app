package view2d;

import java.util.List;
import geometry.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class MapScene {

	private Scene map;
	private Group content;
	
	public MapScene(int width, int height) {
		this.content = new Group();
		this.map = setupScene(width, height, content);
	}
	
	private static Scene setupScene(int width, int height, Group content) {
		GridPane grid = new GridPane();
		grid.add(content, 0, 0);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		
		return new Scene(grid, width, height, Color.BEIGE);
	}

	public Scene scene() {
		return map;
	}
	
	public void scale(double factor) {
	    content.setScaleX(factor);
	    content.setScaleY(factor);
	}
	
	public void addPoints(List<Point2D> points, Color clr, double strokeWidth) {
		for (Point2D pt : points) {
			Circle c = new Circle(pt.x, pt.y, strokeWidth);
		    c.setFill(clr);
		    content.getChildren().add(c);
		}
	}
	
	public void addLines(List<LineSegment2D> lines, Color clr, double strokeWidth) {
		for (LineSegment2D l : lines) {
			Line viewLine = new Line(l.startPoint().x, l.startPoint().y,
					l.endPoint().x, l.endPoint().y);  
			viewLine.setStroke(clr);
			viewLine.setStrokeWidth(strokeWidth);
		    content.getChildren().add(viewLine);
		}
	}
	
	public void addTriangles(List<Triangle2D> triangles, Color clr, double strokeWidth) {
		for (Triangle2D t : triangles) {
			Polygon poly = new Polygon();
			poly.getPoints().add(t.vertex(0).x);
			poly.getPoints().add(t.vertex(0).y);
			poly.getPoints().add(t.vertex(1).x);
			poly.getPoints().add(t.vertex(1).y);
			poly.getPoints().add(t.vertex(2).x);
			poly.getPoints().add(t.vertex(2).y);
		    poly.setStrokeType(StrokeType.INSIDE);
		    poly.setStroke(clr);
		    poly.setStrokeWidth(strokeWidth);
		    poly.setFill(null);
		    content.getChildren().add(poly);
		}
	}
	
	public void addPolygons(List<Polygon2D> polys, Color clr, double strokeWidth) {
		for (Polygon2D poly : polys) {
			Polygon viewPoly = new Polygon();
			for (int i = 0; i < poly.countVertices(); ++i) {
				viewPoly.getPoints().add(poly.vertex(i).x);
				viewPoly.getPoints().add(poly.vertex(i).y);
			}
		    viewPoly.setStrokeType(StrokeType.INSIDE);
		    viewPoly.setStroke(clr);
		    viewPoly.setStrokeWidth(strokeWidth);
		    viewPoly.setFill(null);
		    content.getChildren().add(viewPoly);
		}
	}
	
	public void addRects(List<Rect2D> rects, Color clr, double strokeWidth) {
		for (Rect2D r : rects) {
			Polygon viewPoly = new Polygon();
			viewPoly.getPoints().add(r.left());
			viewPoly.getPoints().add(r.top());
			viewPoly.getPoints().add(r.right());
			viewPoly.getPoints().add(r.top());
			viewPoly.getPoints().add(r.right());
			viewPoly.getPoints().add(r.bottom());
			viewPoly.getPoints().add(r.left());
			viewPoly.getPoints().add(r.bottom());
		    viewPoly.setStrokeType(StrokeType.INSIDE);
		    viewPoly.setStroke(clr);
		    viewPoly.setStrokeWidth(strokeWidth);
		    viewPoly.setFill(null);
		    content.getChildren().add(viewPoly);
		}
	}
	
	public void addCircles(List<Circle2D> circles, Color clr, double strokeWidth) {
		for (Circle2D c : circles) {
			Circle viewCircle = new Circle(c.center.x, c.center.y, c.radius);
			viewCircle.setStrokeType(StrokeType.INSIDE);
			viewCircle.setStroke(clr);
			viewCircle.setStrokeWidth(strokeWidth);
			viewCircle.setFill(null);
		    content.getChildren().add(viewCircle);
		}
	}
}
