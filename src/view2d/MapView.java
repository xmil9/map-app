package view2d;

import javafx.scene.Node;

public interface MapView {

	public abstract Node node();
	public abstract void setMap(map.Map mapModel);
	public abstract void setScale(double factor);
}
