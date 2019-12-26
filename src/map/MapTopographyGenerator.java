package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import math.MathUtil;

// Generates the surface profile of a map. 
public class MapTopographyGenerator {

	public static class Spec {
		public final double landRatio;
		public final int numContinents;
		
		public Spec(Random rand) {
			landRatio = MathUtil.randGaussian(rand, 0,  1);
			numContinents = (int) MathUtil.randGaussian(rand, 0,  10);
		}
	}
	
	private static class Continent {
		
		// Number of map nodes in continent.
		public final int size;
		private List<MapNode> nodes;
		
		public Continent(int size) {
			this.size = size;
			this.nodes = new ArrayList<MapNode>(size);
		}
	}
	
	///////////////
	
	private Map.Representation rep;
	private Spec spec;
	private Random rand;
	private List<Continent> continents;

	public MapTopographyGenerator(Map.Representation rep, Spec spec, Random rand) {
		this.rep = rep;
		this.spec = spec;
		this.rand = rand;
	}
	
	public Map.Representation generate() {
		generateContinents();
		return rep;
	}
	
	public void generateContinents() {
		initContinents();
		
		for (var continent : continents)
			generateContinent(continent);
	}
	
	// Initializes the internal data structures according to the given spec.
	private void initContinents() {
		continents = new ArrayList<Continent>(spec.numContinents);
		
//		final int totalLandNodes = (int) (rep.countNodes() * spec.landRatio);
//		int landNodesRemaining = totalLandNodes;
//		
//		for (int i = 0; i < spec.numContinents - 1; ++i) {
//			int continentsRemaining = spec.numContinents - i; 
//			int continentSize = rand.nextInt(landNodesRemaining - continentsRemaining);
//			landNodesRemaining -= continentSize;
//			continents.add(new Continent(continentSize));
//		}
//		
//		// Last continent.
//		continents.add(new Continent(landNodesRemaining));
	}
	
	private void generateContinent(Continent continent) {
		// todo
	}
}
