package map;

import java.util.Random;

import geometry.Rect2D;
import math.PerlinNoise;

public class PerlinTopography implements TopographyGenerator {

	public static class Spec {
		public final Rect2D bounds;
		public final int numOctaves;
		public final double persistence;
		
		public Spec(Rect2D bounds, int numOctaves, double persistence) {
			this.bounds = bounds;
			this.numOctaves = numOctaves;
			this.persistence = persistence;
		}
	}
	
	///////////////
	
	private final Spec spec;
	private final int left;
	private final int top;
	private final int width;
	private final int height;
	private final Random rand;
	
	public PerlinTopography(Spec spec, Random rand) {
		this.spec = spec;
		this.left = (int) (spec.bounds.left());
		this.top = (int) (spec.bounds.top());
		this.width = (int) (spec.bounds.right() - left) + 1;
		this.height = (int) (spec.bounds.bottom() - top) + 1;
		this.rand = rand;
	}
	
	@Override
	public void generate(Map.Representation rep) {
		PerlinNoise perlinGen = new PerlinNoise(width, height, rand);
		
		for (int i = 0; i < rep.countNodes(); ++i) {
			MapNode node = rep.node(i);
			double noise = perlinGen.calcOctaveNoise(node.pos, spec.numOctaves,
					spec.persistence);
			node.setElevation(10.0 * noise);
		}
		
		for (int i = 0; i < rep.countTiles(); ++i) {
			MapTile tile = rep.tile(i);
			double noise = perlinGen.calcOctaveNoise(tile.seed, spec.numOctaves,
					spec.persistence);
			tile.setElevation(noise);
		}
	}
}
