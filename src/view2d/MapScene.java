package view2d;

import java.util.ArrayList;
import java.util.List;
import geometry.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import map.MapNode;
import map.MapTile;

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
	
	public void addPolygons(List<Polygon2D> polys, Color fillClr, Color strokeClr, double strokeWidth) {
		for (Polygon2D poly : polys) {
			Polygon viewPoly = new Polygon();
			for (int i = 0; i < poly.countVertices(); ++i) {
				viewPoly.getPoints().add(poly.vertex(i).x);
				viewPoly.getPoints().add(poly.vertex(i).y);
			}
		    viewPoly.setStrokeType(StrokeType.INSIDE);
		    viewPoly.setStroke(strokeClr);
		    viewPoly.setStrokeWidth(strokeWidth);
		    viewPoly.setFill(fillClr);
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
	
	public void addMap(map.Map map) {
		int numTiles = map.countTiles();
		for (int i = 0; i < numTiles; ++i)
			addTile(map.tile(i));
	}
	
	private void addTile(MapTile tile) {
		Polygon poly = new Polygon();
		
		int numNodes = tile.countNodes();
		for (int i = 0; i < numNodes; ++i) {
			MapNode node = tile.node(i);
			poly.getPoints().add(node.pos.x);
			poly.getPoints().add(node.pos.y);
		}

		poly.setStroke(null);
		poly.setFill(makeTileFill(tile));
		
		content.getChildren().add(poly);
	}
	
	private static Paint makeTileFill(MapTile tile) {
//		if (tile.elevation() < 0.1)
//			return Color.web("2060C7");
//		return Color.web("00A52D");
		
		double seaLevel = 0.2;
		double maxLevel = 1.0;
		double minLevel = -1.0;
		List<Paint> landFills = new ArrayList<Paint>();
		landFills.add(Color.web("00D72D"));
		landFills.add(Color.web("00A52D"));
		landFills.add(Color.web("00762D"));
		// landFills.add(Color.web("919191")); // gray
		landFills.add(Color.web("00542D")); // dark green
		List<Paint> waterFills = new ArrayList<Paint>();
		waterFills.add(Color.web("20CDFF"));
		waterFills.add(Color.web("20A5FF")); 
		waterFills.add(Color.web("2070FF"));
		waterFills.add(Color.web("203EFF"));

		if (tile.elevation() < seaLevel) {
			return waterFills.get(waterFills.size() - 1);
//			double intervalSize = (seaLevel - minLevel) / (double) waterFills.size();
//			int fillIdx = (int) ((tile.elevation() - minLevel) / (double) intervalSize);
//			return waterFills.get(waterFills.size() - 1 - fillIdx);
		}
		
		double intervalSize = (maxLevel - seaLevel) / (double) landFills.size();
		int fillIdx = (int) ((maxLevel - tile.elevation()) / (double) intervalSize);
		return landFills.get(landFills.size() - 1 - fillIdx);
	}
}
