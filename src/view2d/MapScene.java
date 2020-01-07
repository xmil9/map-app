package view2d;

import java.util.ArrayList;
import java.util.List;
import geometry.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
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
	
	public enum ElevationRendering {
		TILE_BASED,
		NODE_BASED
	}
	
	public static class Spec {
		public final int width;
		public final int height;
		public final double seaLevel;
		public final double scaleFactor;
		public final ElevationRendering elevRendering;
		public final boolean showWaterDepth;
		public final boolean hasFirmShoreline;
		
		public Spec(int width, int height, double seaLevel, double scaleFactor,
				ElevationRendering elevRendering, boolean showWaterDepth,
				boolean haveFirmShoreline) {
			this.width = width;
			this.height = height;
			this.seaLevel = seaLevel;
			this.scaleFactor = scaleFactor;
			this.elevRendering = elevRendering;
			this.showWaterDepth = showWaterDepth;
			this.hasFirmShoreline = haveFirmShoreline;
		}
	}
	
	///////////////
	
	private final Spec spec;
	private Scene map;
	private Group content;
	
	public MapScene(Spec spec) {
		this.spec = spec;
		this.content = new Group();
		this.map = setupScene(spec.width, spec.height, content);
	}
	
	private static Scene setupScene(int width, int height, Group content) {
		GridPane grid = new GridPane();
		grid.add(content, 0, 0);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setCache(true);
		grid.setCacheHint(CacheHint.SPEED);
		
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
	
	private Paint makeTileFill(MapTile tile) {
		switch (spec.elevRendering) {
			case TILE_BASED: {
				return makeTileBasedTileFill(tile);
			}
			case NODE_BASED: {
				return makeNodeBasedTileFill(tile);
			}
		}
		return makeNodeBasedTileFill(tile);
	}
	
	private Paint makeTileBasedTileFill(MapTile tile) {
		return getFill(tile.elevation(), spec.seaLevel, spec.showWaterDepth);
	}
	
	private Paint makeNodeBasedTileFill(MapTile tile) {
		MinMaxElevation minMaxElev = new MinMaxElevation();
		minMaxElev.find(tile);	
		double seedElev = tile.elevation();
		
		// Create firm shore line. Don't interpolate between land and sea colors.
		if (spec.hasFirmShoreline) {
			if (minMaxElev.min < spec.seaLevel && minMaxElev.max >= spec.seaLevel)
				minMaxElev.min = spec.seaLevel;
			if (seedElev < spec.seaLevel && minMaxElev.max >= spec.seaLevel)
				seedElev = spec.seaLevel;
		}			
		
		Color maxFill = getFill(minMaxElev.max, spec.seaLevel, spec.showWaterDepth);
		Color minFill = getFill(minMaxElev.min, spec.seaLevel, spec.showWaterDepth);
		Color seedFill = getFill(seedElev, spec.seaLevel, spec.showWaterDepth);
		Stop[] stops = new Stop[] {
				new Stop(0, maxFill),
				new Stop(.5, seedFill),
				new Stop(1, minFill) };
		return new LinearGradient(minMaxElev.maxPos.x, minMaxElev.maxPos.y,
				minMaxElev.minPos.x, minMaxElev.minPos.y, false,
				CycleMethod.NO_CYCLE, stops);
	}
	
	// Returns a color that should be used for a given elevation value.  
	private static Color getFill(double elev, double seaLevel, boolean showWaterDepth) {
		// Lighter to darker
		List<Color> landFills = new ArrayList<Color>();
		// Greens
		landFills.add(Color.web("00A52D"));
		landFills.add(Color.web("00A52D"));
		landFills.add(Color.web("008922"));
		landFills.add(Color.web("008922"));
		landFills.add(Color.web("006519"));
		landFills.add(Color.web("006519"));
		landFills.add(Color.web("00542D"));
		landFills.add(Color.web("00542D"));
		// Browns
		landFills.add(Color.web("A16708"));
		landFills.add(Color.web("835406"));
		// Grays
		landFills.add(Color.web("4B4B4B"));
		landFills.add(Color.web("787878"));
		landFills.add(Color.web("AAAAAA"));
		Color mountainTops = Color.web("CCCCCC");
		// Darker to lighter
		List<Color> waterFills = new ArrayList<Color>();
		waterFills.add(Color.web("001965"));
		waterFills.add(Color.web("062883"));
		waterFills.add(Color.web("0837B3"));
		waterFills.add(Color.web("0646DF"));

		double maxLevel = 1.0;
		double minLevel = -1.0;
		
		if (elev < seaLevel) {
			if (showWaterDepth)
				return interpolateFill(elev, minLevel, seaLevel, waterFills);
			return waterFills.get(0);
		} else {
			// Max elevation is colored in unique mountain top color.
			if (elev == 1)
				return mountainTops;
			return interpolateFill(elev, seaLevel, maxLevel, landFills);
		}
	}
	
	// Returns a color from a given ordered color list propotional to a given value
	// in a given range.
	private static Color interpolateFill(double val, double min, double max,
			List<Color> fills) {
		double intervalSize = (max - min) / (double) fills.size();
		int idx = (int) ((val - min) / (double) intervalSize);
		if (idx >= fills.size())
			idx = fills.size() - 1;
		return fills.get(idx);
	}
	
	///////////////
	
	// Finds the min and max elevations of a given tile's shape.
	private static class MinMaxElevation {
		public double max = -2;
		public Point2D maxPos = null;
		public double min = 2;
		public Point2D minPos = null;
		
		public void find(MapTile tile) {
			max = -2;
			maxPos = null;
			min = 2;
			minPos = null;
			
			int numNodes = tile.countNodes();
			for (int i = 0; i < numNodes; ++i) {
				MapNode node = tile.node(i);
				double elev = node.elevation();
				if (elev > max) {
					max = elev;
					maxPos = node.pos;
				}
				if (elev < min) {
					min = elev;
					minPos = node.pos;
				}
			}
			
			if (max > 1)
				max = 1;
			if (min < -1)
				min = -1;
		}
	}
}
