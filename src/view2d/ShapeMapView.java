package view2d;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import map.MapNode;
import map.MapTile;

public class ShapeMapView implements MapView {

	private Texture tex;
	private Group mapNode;
	
	public ShapeMapView(Texture tex) {
		this.tex = tex;
		this.mapNode = new Group();
	}
	
	@Override
	public Node node() {
		return mapNode;
	}
	
	@Override
	public void setMap(map.Map mapModel) {
		int numTiles = mapModel.countTiles();
		for (int i = 0; i < numTiles; ++i)
			addTile(mapModel.tile(i));
	}
	
	@Override
	public void setScale(double factor) {
	    mapNode.setScaleX(factor);
	    mapNode.setScaleY(factor);
	}
	
	private void addTile(MapTile tile) {
		Polygon poly = new Polygon();
		
		int numNodes = tile.countNodes();
		for (int i = 0; i < numNodes; ++i) {
			MapNode node = tile.node(i);
			poly.getPoints().add(node.pos.x);
			poly.getPoints().add(node.pos.y);
		}

		poly.setStrokeType(StrokeType.OUTSIDE);
		poly.setStroke(null);
		poly.setFill(tex.tileFill(tile));
		
		mapNode.getChildren().add(poly);
	}
}
