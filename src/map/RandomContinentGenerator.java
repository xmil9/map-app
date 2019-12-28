package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomContinentGenerator implements ContinentGenerator {

	private Map.Representation rep;
	private Random rand;
	
	public RandomContinentGenerator(Map.Representation rep, Random rand) {
		this.rep = rep;
		this.rand = rand;
	}
	
	public void generate(Continent continent) {
		MapNode nextNode = findWaterNode();
		if (nextNode == null)
			return;
		nextNode.setElevation(1);
		
		List<MapNode> landNodes = new ArrayList<MapNode>();
		landNodes.add(nextNode);
		List<MapNode> growthPool = new ArrayList<MapNode>();
		growthPool.add(nextNode);
		
		while (landNodes.size() < continent.allocatedSize) {
			nextNode = findNeighboringWaterNode(growthPool);
			if (nextNode == null)
				return;
			nextNode.setElevation(1);

			landNodes.add(nextNode);
			growthPool.add(nextNode);
		}
	}
	
	private MapNode findWaterNode() {
		int attemptsLeft = 100;
		while (--attemptsLeft > 0) {
			int nodeIdx = rand.nextInt(rep.countNodes());
			if (rep.node(nodeIdx).elevation() < 0)
				return rep.node(nodeIdx);
		}
		return null;
	}
	
	private MapNode findNeighboringWaterNode(List<MapNode> nodePool) {
		while (!nodePool.isEmpty()) {
			int nodeIdx = rand.nextInt(nodePool.size());
			MapNode node = nodePool.get(nodeIdx);
			for (int i = 0; i < node.countNeighbors(); ++i) {
				MapNode neighbor = rep.node(node.neighbor(i));
				if (neighbor.elevation() < 0)
					return neighbor;
			}
			// No neighboring water nodes. Remove node from pool.
			nodePool.remove(nodeIdx);
		}
		return null;
	}
}
