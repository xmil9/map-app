package view2d;

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
		zoom = scale;
		centerMap();
		render();
	}

	private void centerMap() {
		double scale = compositeScale();
		
		double deltaX = canvas.getWidth() - scale * map.width();
		originX = deltaX / 2;
		
		double deltaY = canvas.getHeight() - scale * map.height();
		originY = deltaY / 2;
	}
	
	private void render() {
		double scale = compositeScale();
		
		var gc = canvas.getGraphicsContext2D();
		
		gc.setFill(Color.BEIGE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		gc.setStroke(null);
		
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
}
