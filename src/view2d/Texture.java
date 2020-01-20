package view2d;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import map.ExtremeElevationFinder;
import map.MapTile;

public class Texture {
	
	public enum ElevationRendering {
		TILE_BASED,
		NODE_BASED
	}

	public static class Spec {
		public final double seaLevel;
		public final Texture.ElevationRendering elevRendering;
		public final boolean showWaterDepth;
		public final boolean hasFirmShoreline;
		
		public Spec(double seaLevel, ElevationRendering elevRendering,
				boolean showWaterDepth, boolean haveFirmShoreline) {
			this.seaLevel = seaLevel;
			this.elevRendering = elevRendering;
			this.showWaterDepth = showWaterDepth;
			this.hasFirmShoreline = haveFirmShoreline;
		}
	}
	
	///////////////
	
	private Spec spec;
	private List<Color> landColors;
	private List<Color> waterColors;
	private Color mountainTops = Color.web("CCCCCC");;
	
	public Texture(Spec spec) {
		this.spec = spec;
		this.landColors = makeLandColors();
		this.waterColors = makeWaterColors();
	}
	
	private static List<Color> makeLandColors() {
		// Lighter to darker
		List<Color> colors = new ArrayList<Color>();
		// Greens
		colors.add(Color.web("00A52D"));
		colors.add(Color.web("00A52D"));
		colors.add(Color.web("008922"));
		colors.add(Color.web("008922"));
		colors.add(Color.web("006519"));
		colors.add(Color.web("006519"));
		colors.add(Color.web("00542D"));
		colors.add(Color.web("00542D"));
		// Browns
		colors.add(Color.web("A16708"));
		colors.add(Color.web("835406"));
		// Grays
		colors.add(Color.web("4B4B4B"));
		colors.add(Color.web("787878"));
		colors.add(Color.web("AAAAAA"));
		return colors;
	}
	
	private static List<Color> makeWaterColors() {
		// Darker to lighter
		List<Color> colors = new ArrayList<Color>();
		colors.add(Color.web("001965"));
		colors.add(Color.web("062883"));
		colors.add(Color.web("0837B3"));
		colors.add(Color.web("0646DF"));
		return colors;
	}
	
	public Paint tileFill(MapTile tile) {
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
		var elevExtremes = new ExtremeElevationFinder();
		elevExtremes.find(tile);	
		double seedElev = tile.elevation();
		
		// Create firm shore line. Don't interpolate between land and sea colors.
		if (spec.hasFirmShoreline) {
			if (elevExtremes.min < spec.seaLevel && elevExtremes.max >= spec.seaLevel)
				elevExtremes.min = spec.seaLevel;
			if (seedElev < spec.seaLevel && elevExtremes.max >= spec.seaLevel)
				seedElev = spec.seaLevel;
		}			
		
		Color maxFill = getFill(elevExtremes.max, spec.seaLevel, spec.showWaterDepth);
		Color minFill = getFill(elevExtremes.min, spec.seaLevel, spec.showWaterDepth);
		Color seedFill = getFill(seedElev, spec.seaLevel, spec.showWaterDepth);
		Stop[] stops = new Stop[] {
				new Stop(0, maxFill),
				new Stop(.5, seedFill),
				new Stop(1, minFill) };
		return new LinearGradient(elevExtremes.maxPos.x, elevExtremes.maxPos.y,
				elevExtremes.minPos.x, elevExtremes.minPos.y, false,
				CycleMethod.NO_CYCLE, stops);
	}
	
	// Returns a color that should be used for a given elevation value.  
	private Color getFill(double elev, double seaLevel, boolean showWaterDepth) {
		double maxLevel = 1.0;
		double minLevel = -1.0;
		
		if (elev < seaLevel) {
			if (showWaterDepth)
				return interpolateFill(elev, minLevel, seaLevel, waterColors);
			return waterColors.get(0);
		} else {
			// Max elevation is colored in unique mountain top color.
			if (elev == 1)
				return mountainTops;
			return interpolateFill(elev, seaLevel, maxLevel, landColors);
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
}
