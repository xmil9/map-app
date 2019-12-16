package geometry;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class PoissonDiscSamplingTest {

	private boolean verifyMinDistance(List<Point2D> samples, double minDist) {
		double distSquared = minDist * minDist;
		
		for (var sample : samples) {
			for (var other : samples) {
				if (!sample.equals(other) &&
						Point2D.distanceSquared(sample, other) < distSquared) {
							return false;
				}
			}
		}

		return true;
	}
	
	@Test
	public void generate_ForRandomInitialSample1() {
		Rect2D domain = new Rect2D(0, 0, 20, 20);
		double minDist = 3;
		
		final int numRuns = 10;
		for (int i = 0; i < numRuns; ++i) {
			PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist);
			List<Point2D> samples = sampler.generate();
			
			assertTrue(!samples.isEmpty());
			assertTrue(verifyMinDistance(samples, minDist));
		}
	}
	
	@Test
	public void generate_ForRandomInitialSample2() {
		Rect2D domain = new Rect2D(-30, -50, 100, 200);
		double minDist = 11.5;

		final int numRuns = 10;
		for (int i = 0; i < numRuns; ++i) {
			PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist);
			List<Point2D> samples = sampler.generate();
			
			assertTrue(!samples.isEmpty());
			assertTrue(verifyMinDistance(samples, minDist));
		}
	}
	
	@Test
	public void generate_ForGivenInitialSample1() {
		Rect2D domain = new Rect2D(0, 0, 20, 20);
		double minDist = 3;
		
		final int numRuns = 10;
		for (int i = 0; i < numRuns; ++i) {
			PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist);
			List<Point2D> samples = sampler.generate(new Point2D(3, 6));
			
			assertTrue(!samples.isEmpty());
			assertTrue(verifyMinDistance(samples, minDist));
		}
	}
	
	@Test
	public void generate_test2() {
		Rect2D domain = new Rect2D(-30, -50, 100, 200);
		double minDist = 11.5;

		final int numRuns = 10;
		for (int i = 0; i < numRuns; ++i) {
			PoissonDiscSampling sampler = new PoissonDiscSampling(domain, minDist);
			List<Point2D> samples = sampler.generate(new Point2D(37, -3));
			
			assertTrue(!samples.isEmpty());
			assertTrue(verifyMinDistance(samples, minDist));
		}
	}
}
