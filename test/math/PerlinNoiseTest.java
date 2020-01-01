package math;

import java.util.Random;

import static org.junit.Assert.*;
import org.junit.Test;

import geometry.Point2D;

public class PerlinNoiseTest {

	@Test
	public void calcNoise_NoiseInRange() {
		Random rand = new Random(7777L);
		int width = 100;
		int height = 100;
		PerlinNoise gen = new PerlinNoise(width, height, rand);
		
		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j) {
				double noise = gen.calcNoise(new Point2D(i, j));
				assertTrue(-1 <= noise && noise < 1);
			}
	}

	@Test
	public void calcOctaveNoise_NoiseInRange() {
		Random rand = new Random(6666L);
		int width = 100;
		int height = 100;
		PerlinNoise gen = new PerlinNoise(width, height, rand);
		
		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j) {
				double noise = gen.calcOctaveNoise(new Point2D(i, j), 5, 2);
				assertTrue(-1 <= noise && noise < 1);
			}
	}

	@Test
	public void calcOctaveNoise_DifferentNumberOfOctaves() {
		Random rand = new Random(6667L);
		int width = 100;
		int height = 100;
		PerlinNoise gen = new PerlinNoise(width, height, rand);
		
		for (int numOctaves = 1; numOctaves < 15; ++numOctaves)
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j) {
					double noise = gen.calcOctaveNoise(new Point2D(i, j), numOctaves, 2);
					assertTrue(-1 <= noise && noise < 1);
				}
	}

	@Test
	public void calcOctaveNoise_DifferentPersistenceValues() {
		Random rand = new Random(6667L);
		int width = 100;
		int height = 100;
		PerlinNoise gen = new PerlinNoise(width, height, rand);

		double persistence = 1;
		while (persistence <= 5) {
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j) {
					double noise = gen.calcOctaveNoise(new Point2D(i, j), 6, persistence);
					assertTrue(-1 <= noise && noise < 1);
				}
			persistence += .5;
		}
	}
}
