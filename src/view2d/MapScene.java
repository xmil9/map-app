package view2d;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class MapScene {
	
	public static class Spec {
		public final int width;
		public final int height;
		public final double initialScale;
		public final double seaLevel;
		public final Texture.ElevationRendering elevRendering;
		public final boolean showWaterDepth;
		public final boolean hasFirmShoreline;
		
		public Spec(int width, int height, double scaleFactor, double seaLevel,
				Texture.ElevationRendering elevRendering, boolean showWaterDepth,
				boolean haveFirmShoreline) {
			this.width = width;
			this.height = height;
			this.initialScale = scaleFactor;
			this.seaLevel = seaLevel;
			this.elevRendering = elevRendering;
			this.showWaterDepth = showWaterDepth;
			this.hasFirmShoreline = haveFirmShoreline;
		}
	}
	
	// Creates a texture spec from a view spec.
	private static Texture.Spec makeTextureSpec(Spec viewSpec) {
		return new Texture.Spec(viewSpec.seaLevel, viewSpec.elevRendering,
				viewSpec.showWaterDepth, viewSpec.hasFirmShoreline);
	}
	
	///////////////
	
	private final Spec spec;
	private MapView mapView;
	private GridPane layout;
	private Scene scene;
	// Scaling factor accumulated by edit operations (mouse wheel). Not necessarily
	// the same as the current map scale because multiple edit events can accumulate
	// before they get applied.
	private double editedScale;
	private double mouseDownX;
	private double mouseDownY;
	private Timer zoomTimer = new Timer();
	
	public MapScene(Spec spec) {
		this.spec = spec;
		this.editedScale = spec.initialScale;
		this.mapView = new CanvasMapView(new Texture(makeTextureSpec(spec)), spec.width,
				spec.height, spec.initialScale);
		this.layout = setupLayout(mapView.node());
		this.scene = setupScene(spec.width, spec.height, this.layout);
		setupEditing();
	}
	
	private static GridPane setupLayout(Node mapView) {
		GridPane layout = new GridPane();
		layout.add(mapView, 0, 0);
		layout.setAlignment(Pos.CENTER);
		layout.setHgap(10);
		layout.setVgap(10);
		layout.setPadding(new Insets(25, 25, 25, 25));
		return layout;
	}
	
	private static Scene setupScene(int width, int height, GridPane layout) {
		return new Scene(layout, width, height, Color.BEIGE);
	}

	private void setupEditing() {
		// Mouse wheel zooming.
		mapView.node().setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
            	editedScale *= calcZoomFactor(editedScale, event.getDeltaY() > 0);
                scheduleZoomTask();
                event.consume();
            }
        });
		// Mouse panning.
		mapView.node().setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event)
            {
                mouseDownX = event.getSceneX();
                mouseDownY = event.getSceneY();
            }
        });
		mapView.node().setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
            public void handle(MouseEvent event)
            {
                double mousePosX = event.getSceneX();
                double mousePosY = event.getSceneY();
                move(mousePosX - mouseDownX, mousePosY - mouseDownY);
                mouseDownX = mousePosX;
                mouseDownY = mousePosY;
                
                event.consume();
            }
        });
	}
	
	private static double calcZoomFactor(double curScale, boolean zoomIn) {
		// Zoom faster when zoomed in deeper.
		double delta = (curScale > 20.0) ? .2 : .1;
		if (!zoomIn)
			delta *= -1;
		return 1.0 + delta;
	}
	
	// Schedules the processing of a zoom event for a brief interval in the
	// future. This aggregates the processing of multiple quick zoom events
	// into one operation avoiding unnecessary updates.
	private void scheduleZoomTask() {
		// Clear previously scheduled task.
        zoomTimer.cancel();
        zoomTimer.purge();
        
        // Schedule new task.
        final int ZOOM_DELAY = 50;
        zoomTimer = new Timer();
        zoomTimer.schedule(new TimerTask() {
    		@Override
    		public void run() {
    			// Run the scaling of the scene on the UI thread.
    			Platform.runLater(() -> {
                    scale(editedScale);
    			});
    		}
    	}, ZOOM_DELAY);
	}
	
	public Scene scene() {
		return scene;
	}
	
	public void setMap(map.Map map) {
		mapView.setMap(map);
	}
	
	public void scale(double factor) {
		mapView.setScale(factor);
		editedScale = factor;
	}
	
	public void move(double dx, double dy) {
		mapView.move(dx, dy);
	}
	
	public void enableCaching(boolean enable) {
		Node mapNode = mapView.node();
		mapNode.setCache(true);
		if (enable)
			mapNode.setCacheHint(CacheHint.SPEED);
	}
}
