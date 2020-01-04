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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
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
		boolean showTileBasedElevation = false;
		boolean showWaterDepth = true;
		
		if (showTileBasedElevation) {
			return getFill(tile.elevation(), showWaterDepth);
		} else {
			double maxElev = -2;
			Point2D maxPt = null;
			double minElev = 2;
			Point2D minPt = null;
			int numNodes = tile.countNodes();
			for (int i = 0; i < numNodes; ++i) {
				MapNode node = tile.node(i);
				double elev = node.elevation();
				if (elev > maxElev) {
					maxElev = elev;
					maxPt = node.pos;
				}
				if (elev < minElev) {
					minElev = elev;
					minPt = node.pos;
				}
			}
			if (maxElev > 1)
				maxElev = 1;
			if (minElev < -1)
				minElev = -1;
			
			Color maxFill = getFill(maxElev, showWaterDepth);
			Color minFill = getFill(minElev, showWaterDepth);
			Stop[] stops = new Stop[] { new Stop(0, maxFill), new Stop(1, minFill) };
			return new LinearGradient(maxPt.x, maxPt.y, minPt.x, minPt.y, false,
					CycleMethod.NO_CYCLE, stops);
		}
	}
	
	private static Color getFill(double elev, boolean showWaterDepth) {
		// Lighter to darker
		List<Color> landFills = new ArrayList<Color>();
		landFills.add(Color.web("00D72D"));
		landFills.add(Color.web("00C22D"));
		landFills.add(Color.web("00A52D"));
		landFills.add(Color.web("00762D"));
		landFills.add(Color.web("00542D"));
		landFills.add(Color.web("51542D"));
		landFills.add(Color.web("AA8300"));
//		landFills.add(Color.web("AA5400"));
		landFills.add(Color.web("835406"));
		landFills.add(Color.web("4B4B4B"));
		landFills.add(Color.web("787878"));
		landFills.add(Color.web("AAAAAA"));
//		landFills.add(Color.web("CCCCCC"));
//		landFills.add(Color.web("EEEEEE"));
		// Darker to lighter
		List<Color> waterFills = new ArrayList<Color>();
		waterFills.add(Color.web("203EFF"));
		waterFills.add(Color.web("2070FF"));
		waterFills.add(Color.web("20A5FF"));
		waterFills.add(Color.web("20CDFF"));

		double seaLevel = 0.1;
		double maxLevel = 1.0;
		double minLevel = -1.0;
		
		if (elev < seaLevel) {
			if (showWaterDepth)
				return interpolateFill(elev, minLevel, seaLevel, waterFills);
			return waterFills.get(waterFills.size() - 1);
		} else {
			if (elev == 1)
				return Color.web("CCCCCC");
			return interpolateFill(elev, seaLevel, maxLevel, landFills);
		}
	}
	
	private static Color interpolateFill(double val, double min, double max,
			List<Color> fills) {
		double intervalSize = (max - min) / (double) fills.size();
		int idx = (int) ((val - min) / (double) intervalSize);
		if (idx >= fills.size())
			idx = fills.size() - 1;
		return fills.get(idx);
	}
}
