package math;

import java.util.Random;

import geometry.Point2D;
import geometry.Vector2D;

// Implementation of Perlin noise algorithm.
// Sources:
//   https://mzucker.github.io/html/perlin-noise-math-faq.html
//   https://flafla2.github.io/2014/08/09/perlinnoise.html
public class PerlinNoise {

	// Data needs for a grid node during the calculation.
	private static class GridNode {
		public final Point2D pos;
		public final Vector2D gradient;
		
		public GridNode(Point2D pos, Vector2D gradient) {
			this.pos = pos;
			this.gradient = gradient;
		}
	}
	
	///////////////
	
	// Random gradients for each grid point.
	private final Vector2D gradients[][];
	
	public PerlinNoise(int width, int height, Random rand) {
		this.gradients = makeGradients(width + 1, height + 1, rand);
	}
	
	// Calculates Perlin noise at a given point in 2D range ([0, width], [0, height]).
	public double calcNoise(Point2D at) {
		// Determine grid cell that point falls into.
		int gridLeft = (int) at.x; 
		int gridRight = gridLeft + 1; 
		int gridTop = (int) at.y; 
		int gridBottom = gridTop + 1;
		
		// Collect data for each grid node that is involved in calculation.
		final int numGridNodes = 4;
		GridNode gridNode[] = new GridNode[] {
			new GridNode(new Point2D(gridLeft, gridTop),
					gradients[gridTop][gridLeft]),
			new GridNode(new Point2D(gridRight, gridTop),
					gradients[gridTop][gridRight]),
			new GridNode(new Point2D(gridLeft, gridBottom),
					gradients[gridBottom][gridLeft]),
			new GridNode(new Point2D(gridRight, gridBottom),
					gradients[gridBottom][gridRight])
		};
		
		// Calculate the influence that each grid node has on the result.
		double[] influence = new double[numGridNodes];
		for (int i = 0; i < numGridNodes; ++i) {
			GridNode corner = gridNode[i];
			Vector2D influenceVec = new Vector2D(corner.pos, at);
			influence[i] = corner.gradient.dot(influenceVec);
		}
		
		// Average/interpolate the influences of each grid node.
		// Use a fade function to smooth the interpolation.
		// First average horizontally.
		double weightX = fade(at.x - gridLeft);
		double avgTop = linearInterpolate(influence[0], influence[1], weightX);
		double avgBottom = linearInterpolate(influence[2], influence[3], weightX);
		// Then average the horizontal averages vertically.
		double weightY = fade(at.y - gridTop);
		double avg = linearInterpolate(avgTop, avgBottom, weightY);
		
		return avg;
	}
	
	// S-shaped fade curve for weighted interpolation.
	private static double fade(double t) {
		// f(t) = 6t^5-15t^4+10t^3
		return t * t * t * (t * (t * 6 - 15) + 10);
		// f(t) = 3t^2 - 2t^3
//		return t * t * (3 - 2 * t);
	}
	
	// Linear interpolation between two values at a given ratio.
	private static double linearInterpolate(double a, double b, double ratio) {
		return a + ratio * (b - a);
	}
	
	// Generates random gradient vectors at each grid point.
	private static Vector2D[][] makeGradients(int width, int height, Random rand) {
		var grads = new Vector2D[height][width];
		for (int r = 0; r < height; ++r)
			for (int c = 0; c < width; ++c)
				grads[r][c] = makeGradient(rand);
		return grads;
	}
	
	// Generates a normalized non-zero gradient vector.
	private static Vector2D makeGradient(Random rand) {
		Vector2D grad = new Vector2D(coord(rand), coord(rand));
		while (grad.lengthSquared() == 0)
			grad = new Vector2D(rand.nextDouble(), rand.nextDouble());
		return grad.normalize();
	}
	
	// Returns a random value in range [-1, 1).
	private static double coord(Random rand) {
		return -1 + rand.nextDouble() * 2;
	}
}