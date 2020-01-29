package app;

import java.util.Random;

import javafx.application.*;
import javafx.stage.*;

import geometry.*;
import map.Map;
import map.MapGeometryGenerator;
import map.PerlinTopography;
import view2d.MapScene;
import view2d.Texture;


public class App extends Application {
	
	public class Spec {
		public Long randSeed = null;//1234567890L;
		// View specs.
		public int viewWidth = 2100;
		public int viewHeight = 1100;
		public double scaleFactor = 1.0;
		public double seaLevel = 0.2;
		public Texture.ElevationRendering elevRendering =
				Texture.ElevationRendering.NODE_BASED;
		public boolean showWaterDepth = true;
		public boolean hasFirmShoreline = false;
		// Flag to turn on JavaFX node caching.
		// Turn on for shape-based view of map. 
		// Turn off for canvas-based view of map. 
		public boolean cacheMap = false;
		// Model specs.
		public int mapWidth = 500;
		public int mapHeight = 200;
		// Smaller distance => smaller and more tiles.
		public double minSampleDistance = .5;
		// More candidates => more evenly spaced sample points but slower generation.
		public int numSampleCandidates = 30;
		// More octaves => Wider and wider areas are affected by values of
		// individual noise values of higher octave passes. Leads to zoomed in
		// appearance on features of the map.
		public int numOctaves = 7;
		// Larger persistence => Larger and smoother features.
		// Smaller persistence => Smaller and choppier features.
		public double persistence = 2;
	}

	// Creates a view spec from an app-wide spec.
	private static MapScene.Spec makeViewSpec(Spec appSpec) {
		return new MapScene.Spec(appSpec.viewWidth, appSpec.viewHeight,
				appSpec.scaleFactor, appSpec.seaLevel, appSpec.elevRendering,
				appSpec.showWaterDepth, appSpec.hasFirmShoreline);
	}
	
	// Creates a model spec from an app-wide spec.
	private static Map.Spec makeModelSpec(Spec appSpec) {
		Rect2D bounds = new Rect2D(0, 0, appSpec.mapWidth, appSpec.mapHeight);
		return new Map.Spec(
				new MapGeometryGenerator.Spec(bounds, appSpec.minSampleDistance,
						appSpec.numSampleCandidates),
				new PerlinTopography.Spec(bounds, appSpec.numOctaves,
						appSpec.persistence));
	}
	
	///////////////
	
	Spec spec;
	// App-wide random number generator. Must be used everywhere to guarantee
	// deterministic map generation.
	private Random rand;
	private MapScene mapScene;
	
	public static void main(String passes[]) {
		launch(passes);
	}

	@Override
	public void start(Stage primaryStage) {
		spec = new Spec();
		rand = (spec.randSeed != null) ? new Random(spec.randSeed) : new Random();
		mapScene = makeMapScene();
		
		primaryStage.setTitle("The Map App");
		primaryStage.setScene(mapScene.scene());
		primaryStage.setOnCloseRequest(e -> {
	        Platform.exit();
	        System.exit(0);
	    });
		
		primaryStage.show();
	}
	
	private MapScene makeMapScene() {
		Map map = new Map(makeModelSpec(spec), rand);
		map.generate();

		MapScene scene = new MapScene(makeViewSpec(spec));
		scene.setMap(map);
		scene.scale(spec.scaleFactor);
		scene.enableCaching(spec.cacheMap);
		return scene;
	}
}
