package map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import geometry.Point2D;
import geometry.Polygon2D;


public class MapRepresentationTest {

	@Test
	public void addTile_SingleTile() {
		var pos = new Point2D(2, 0);
		List<Point2D> shapePts = new ArrayList<Point2D>();
		shapePts.add(new Point2D(1, 2));
		shapePts.add(new Point2D(2, -2));
		shapePts.add(new Point2D(3, 2));
		shapePts.add(new Point2D(4, -2));
		var shape = new Polygon2D(shapePts);
		var tile = new MapTile(pos, shape);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tile);
		
		assertEquals(1, rep.countTiles());
	}
	
	@Test
	public void addTile_MultipleTiles() {
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		assertEquals(2, rep.countTiles());
	}
	
	@Test
	public void tile() {
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		assertEquals(tileA, rep.tile(0));
		assertEquals(tileB, rep.tile(1));
	}
	
	@Test
	public void findTileAt_ForAvailableLocation() {
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		var result = rep.findTileAt(posA);
		assertEquals(tileA, result);
		
		result = rep.findTileAt(posB);
		assertEquals(tileB, result);
	}
	
	@Test
	public void findTileAt_ForUnavailableLocation() {
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		var result = rep.findTileAt(new Point2D(2, 3));
		assertNull(result);
	}

	@Test
	public void addNode_SingleNode() {
		var pos = new Point2D(2, 0);
		var node = new MapNode(pos);
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(node);
		
		assertEquals(1, rep.countNodes());
	}

	@Test
	public void addNode_MultipleNodes() {
		Map.Representation rep = new Map.Representation();
		rep.addNode(new MapNode(new Point2D(2, 0)));
		rep.addNode(new MapNode(new Point2D(3, 2)));
		rep.addNode(new MapNode(new Point2D(-1, 3)));
		
		assertEquals(3, rep.countNodes());
	}

	@Test
	public void node() {
		var nodeA = new MapNode(new Point2D(2, 0));
		var nodeB = new MapNode(new Point2D(3, 2));
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(nodeA);
		rep.addNode(nodeB);
		
		assertEquals(nodeA, rep.node(0));
		assertEquals(nodeB, rep.node(1));
	}

	@Test
	public void findNodeAt_ForAvailableNode() {
		var nodeA = new MapNode(new Point2D(2, 0));
		var nodeB = new MapNode(new Point2D(3, 2));
		var nodeC = new MapNode(new Point2D(-1, 3));
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(nodeA);
		rep.addNode(nodeB);
		rep.addNode(nodeC);
		
		var result = rep.findNodeAt(new Point2D(2, 0));
		assertEquals(nodeA, result);
		
		result = rep.findNodeAt(new Point2D(3, 2));
		assertEquals(nodeB, result);
		
		result = rep.findNodeAt(new Point2D(-1, 3));
		assertEquals(nodeC, result);
	}

	@Test
	public void findNodeAt_ForUnavailableNode() {
		var nodeA = new MapNode(new Point2D(2, 0));
		var nodeB = new MapNode(new Point2D(3, 2));
		var nodeC = new MapNode(new Point2D(-1, 3));
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(nodeA);
		rep.addNode(nodeB);
		rep.addNode(nodeC);
		
		var result = rep.findNodeAt(new Point2D(0, 1));
		assertNull(result);
	}
}
