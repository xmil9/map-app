package view2d;

import geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.MapNode;
import map.MapTile;

public class CanvasMapView implements MapView {

	private Texture tex;
	private Group mapNode;
	private Canvas canvas;
	private map.Map map;
	private double zoom;
	private double originX;
	private double originY;
	
	public CanvasMapView(Texture tex, int width, int height, double scale) {
		this.tex = tex;
		this.canvas = new Canvas(width, height);
		this.mapNode = new Group();
		mapNode.getChildren().add(canvas);
		this.zoom = scale;
		this.originX = 0;
		this.originY = 0;
	}
	
	@Override
	public Node node() {
		return mapNode;
	}
	
	@Override
	public void setMap(map.Map mapModel) {
		map = mapModel;
		
		double scale = fittingScale();
		canvas.setWidth(scale * map.width());
		canvas.setHeight(scale * map.height());
		
		render();
	}
	
	@Override
	public void setScale(double scale) {
		Point2D center = normalizedMapCenter();
		zoom = scale;
		setNormalizedMapCenter(center);
		render();
	}
	
	@Override
	public void move(double dx, double dy) {
		originX += dx;
		originY += dy;
		render();
	}
	
	private void render() {
		var gc = canvas.getGraphicsContext2D();
		renderBackground(gc);
		renderMap(gc);
	}
	
	private void renderBackground(GraphicsContext gc) {
		gc.setFill(Color.web("#FFFFDC"));
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.setStroke(Color.BLACK);
		gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	private void renderMap(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);
		
		double scale = compositeScale();
		int numTiles = map.countTiles();
		for (int i = 0; i < numTiles; ++i)
			renderTile(gc, map.tile(i), scale);
	}
	
	private void renderTile(GraphicsContext gc, MapTile tile, double scale) {
		int numNodes = tile.countNodes();
		double xCoords[] = new double[numNodes];
		double yCoords[] = new double[numNodes];
		
		for (int i = 0; i < numNodes; ++i) {
			MapNode node = tile.node(i);
			xCoords[i] = originX + node.pos.x * scale;
			yCoords[i] = originY + node.pos.y * scale;
		}
		
		
		gc.setFill(tex.tileFill(tile));
		gc.fillPolygon(xCoords, yCoords, numNodes);
	}
	
	private double fittingScale() {
		double xScale = canvas.getWidth() / map.width();
		double yScale = canvas.getHeight() / map.height();
		return Math.min(xScale,  yScale);
	}
	
	private double compositeScale() {
		return fittingScale() * zoom;
	}
	
	// Returns the normalized point of the map that is centered on the
	// screen. E.g. the map origin would be (0, 0), the map right-bottom
	// would be (1.0, 1.0).
	private Point2D normalizedMapCenter() {
		double centerX = canvas.getWidth() / 2 - originX;
		double centerY = canvas.getHeight() / 2 - originY;
		
		double scale = compositeScale();
		double scaledWidth = scale * map.width();
		double scaledHeight = scale * map.height();
		
		return new Point2D(centerX / scaledWidth, centerY / scaledHeight);
	}

	// Positions the map so that the given normalized point is in the
	// center of the screen.
	private void setNormalizedMapCenter(Point2D relativeCenter) {
		double scale = compositeScale();
		double scaledWidth = scale * map.width();
		double scaledHeight = scale * map.height();
		
		double centerX = relativeCenter.x * scaledWidth;
		double centerY = relativeCenter.y * scaledHeight;
		
		originX =  canvas.getWidth() / 2 - centerX;
		originY =  canvas.getHeight() / 2 - centerY;
	}
}
