package view2d;

import javafx.scene.Node;

public interface MapView {

	public abstract Node node();
	public abstract void addMap(map.Map mapModel);
}
