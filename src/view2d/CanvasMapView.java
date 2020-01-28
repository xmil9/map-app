package view2d;

import java.util.ArrayList;
import java.util.List;

import geometry.Point2D;
import geometry.Rect2D;
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
	// Canvas that map is drawn into.
	private Canvas canvas;
	private map.Map map;
	// User defined zoom factor of map.
	private double zoom;
	// Offset of top-left map corner from top-left corner of canvas.
	private double originX;
	private double originY;
	// Coordinate arrays for each tile's shape canvas space. The actual coordinate
	// values still need to change for each render operation but having the arrays
	// as class fields avoids allocating them for each tile for each render operation.
	List<double[]> tileXCoords;
	List<double[]> tileYCoords;
	
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
		
		initTileCoords();
		render();
	}
	
	private void initTileCoords() {
		int numTiles = map.countTiles();

		tileXCoords = new ArrayList<double[]>(numTiles);
		tileYCoords = new ArrayList<double[]>(numTiles);

		for (int i = 0; i < numTiles; ++i) {
			int numNodes = map.tile(i).countNodes();
			tileXCoords.add(new double[numNodes]);
			tileYCoords.add(new double[numNodes]);
		}
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
		
		long startTime = System.currentTimeMillis();
		int numRenderedTiles = 0;
		
		double scale = compositeScale();
		int numTiles = map.countTiles();
		for (int i = 0; i < numTiles; ++i) {
			MapTile tile = map.tile(i);
			if (isTileVisible(tile, scale)) {
				renderTile(gc, tile, tileXCoords.get(i), tileYCoords.get(i), scale);
				numRenderedTiles++;
			}
		}
		
		System.out.println("Rendered tiles: " + numRenderedTiles);
		System.out.println("Render time: " + (System.currentTimeMillis() - startTime));
	}
	
	// Returns whether a given tile is on the canvas.
	private boolean isTileVisible(MapTile tile, double scale) {
		Rect2D cvTileBounds = tile.bounds();
		cvTileBounds.setLeft(canvasXFromMap(cvTileBounds.left(), scale));
		cvTileBounds.setRight(canvasXFromMap(cvTileBounds.right(), scale));
		cvTileBounds.setTop(canvasYFromMap(cvTileBounds.top(), scale));
		cvTileBounds.setBottom(canvasYFromMap(cvTileBounds.bottom(), scale));
		
		return !(cvTileBounds.right() < 0 ||
				cvTileBounds.left() > canvas.getWidth() ||
				cvTileBounds.bottom() < 0 ||
				cvTileBounds.top() > canvas.getHeight());
	}
	
	private void renderTile(GraphicsContext gc, MapTile tile, double[] xCoords,
			double[] yCoords, double scale) {
		updateTileCoordinates(tile, xCoords, yCoords, scale);
		gc.setFill(tex.tileFill(tile));
		gc.fillPolygon(xCoords, yCoords, tile.countNodes());
	}
	
	private void updateTileCoordinates(MapTile tile, double[] xCoords, double[] yCoords,
			double scale) {
		int numNodes = tile.countNodes();
		for (int i = 0; i < numNodes; ++i) {
			MapNode node = tile.node(i);
			xCoords[i] = canvasXFromMap(node.pos.x, scale);
			yCoords[i] = canvasYFromMap(node.pos.y, scale);
		}
	}
	
	// Returns the scaling factor needed to fit the entire map onto the canvas.
	private double fittingScale() {
		double xScale = canvas.getWidth() / map.width();
		double yScale = canvas.getHeight() / map.height();
		return Math.min(xScale,  yScale);
	}
	
	// Returns the total scaling factor for the map.
	private double compositeScale() {
		return fittingScale() * zoom;
	}
	
	// Returns the point of the map that is centered on the screen. The point
	// is normalized to the dimensions of the map, e.g. the map's origin would
	// be (0, 0), the map's right-bottom corner would be (1.0, 1.0).
	private Point2D normalizedMapCenter() {
		double scale = compositeScale();
		double centerX = mapXFromCanvas(canvas.getWidth() / 2, scale);
		double centerY = mapYFromCanvas(canvas.getHeight() / 2, scale);
		return new Point2D(centerX / map.width(), centerY / map.height());
	}

	// Positions the map so that the given point is in the center of the screen.
	// The given point has to be normalized to the dimensions of the map.
	private void setNormalizedMapCenter(Point2D normedCenter) {
		double scale = compositeScale();
		double centerX = normedCenter.x * map.width();
		double centerY = normedCenter.y * map.height();
		originX =  canvas.getWidth() / 2 - (centerX * scale);
		originY =  canvas.getHeight() / 2 - (centerY * scale);
	}
	
	private double canvasXFromMap(double mapX, double scale) {
		return originX + mapX * scale;
	}
	
	private double canvasYFromMap(double mapY, double scale) {
		return originY + mapY * scale;
	}
	
	private double mapXFromCanvas(double canvasX, double scale) {
		return (canvasX - originX) / scale;
	}
	
	private double mapYFromCanvas(double canvasY, double scale) {
		return (canvasY - originY) / scale;
	}
}
