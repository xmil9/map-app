package map;

import java.util.Random;

import geometry.Point2D;
import geometry.Rect2D;
import math.PerlinNoise;

public class PerlinTopography implements TopographyGenerator {

	private final int left;
	private final int top;
	private final int width;
	private final int height;
	private final Random rand;
	
	public PerlinTopography(Rect2D bounds, Random rand) {
		this.left = (int) scale(bounds.left());
		this.top = (int) scale(bounds.top());
		this.width = (int) scale(bounds.right() - left) + 1;
		this.height = (int) scale(bounds.bottom() - top) + 1;
		this.rand = rand;
	}
	
	@Override
	public void generate(Map.Representation rep) {
		PerlinNoise perlinGen = new PerlinNoise(width, height, rand);
		
		for (int i = 0; i < rep.countNodes(); ++i) {
			MapNode node = rep.node(i);
			double noise = perlinGen.calcNoise(scale(node.pos));
			node.setElevation(10.0 * noise);
		}
		
		for (int i = 0; i < rep.countTiles(); ++i) {
			MapTile tile = rep.tile(i);
			double noise = perlinGen.calcNoise(scale(tile.seed));
			tile.setElevation(noise);
		}
	}
	
	public static double scale(double val) {
		return val / 50.0;
	}
	
	public static Point2D scale(Point2D pt) {
		return new Point2D(scale(pt.x), scale(pt.y));
	}
}
