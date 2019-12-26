package map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import geometry.Point2D;
import geometry.Polygon2D;
import geometry.Rect2D;


public class MapRepresentationTest {

	@Test
	public void addTile_SingleTile() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var pos = new Point2D(2, 0);
		List<Point2D> shapePts = new ArrayList<Point2D>();
		shapePts.add(new Point2D(1, 2));
		shapePts.add(new Point2D(2, -2));
		shapePts.add(new Point2D(3, 2));
		shapePts.add(new Point2D(4, -2));
		var shape = new Polygon2D(shapePts);
		var tile = new MapTile(pos, shape, map);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tile);
		
		assertEquals(1, rep.countTiles());
	}
	
	@Test
	public void addTile_MultipleTiles() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA, map);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB, map);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		assertEquals(2, rep.countTiles());
	}
	
	@Test
	public void tile() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA, map);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB, map);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		assertEquals(tileA, rep.tile(0));
		assertEquals(tileB, rep.tile(1));
	}
	
	@Test
	public void findTileAt_ForAvailableLocation() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA, map);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB, map);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		var result = rep.findTileAt(posA);
		assertEquals(tileA, result.a);
		assertEquals(Integer.valueOf(0), result.b);
		
		result = rep.findTileAt(posB);
		assertEquals(tileB, result.a);
		assertEquals(Integer.valueOf(1), result.b);
	}
	
	@Test
	public void findTileAt_ForUnavailableLocation() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var posA = new Point2D(2, 0);
		List<Point2D> shapePtsA = new ArrayList<Point2D>();
		shapePtsA.add(new Point2D(1, 2));
		shapePtsA.add(new Point2D(2, -2));
		shapePtsA.add(new Point2D(3, 2));
		shapePtsA.add(new Point2D(4, -2));
		var shapeA = new Polygon2D(shapePtsA);
		var tileA = new MapTile(posA, shapeA, map);
		
		var posB = new Point2D(4, 2);
		List<Point2D> shapePtsB = new ArrayList<Point2D>();
		shapePtsB.add(new Point2D(3, 0));
		shapePtsB.add(new Point2D(5, -1));
		shapePtsB.add(new Point2D(5, 4));
		shapePtsB.add(new Point2D(3, 5));
		var shapeB = new Polygon2D(shapePtsB);
		var tileB = new MapTile(posB, shapeB, map);
		
		Map.Representation rep = new Map.Representation();
		rep.addTile(tileA);
		rep.addTile(tileB);
		
		var result = rep.findTileAt(new Point2D(2, 3));
		assertNull(result);
	}

	@Test
	public void addNode_SingleNode() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var pos = new Point2D(2, 0);
		var node = new MapNode(pos, map);
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(node);
		
		assertEquals(1, rep.countNodes());
	}

	@Test
	public void addNode_MultipleNodes() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(new MapNode(new Point2D(2, 0), map));
		rep.addNode(new MapNode(new Point2D(3, 2), map));
		rep.addNode(new MapNode(new Point2D(-1, 3), map));
		
		assertEquals(3, rep.countNodes());
	}

	@Test
	public void node() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var nodeA = new MapNode(new Point2D(2, 0), map);
		var nodeB = new MapNode(new Point2D(3, 2), map);
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(nodeA);
		rep.addNode(nodeB);
		
		assertEquals(nodeA, rep.node(0));
		assertEquals(nodeB, rep.node(1));
	}

	@Test
	public void findNodeAt_ForAvailableNode() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var nodeA = new MapNode(new Point2D(2, 0), map);
		var nodeB = new MapNode(new Point2D(3, 2), map);
		var nodeC = new MapNode(new Point2D(-1, 3), map);
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(nodeA);
		rep.addNode(nodeB);
		rep.addNode(nodeC);
		
		var result = rep.findNodeAt(new Point2D(2, 0));
		assertEquals(nodeA, result.a);
		assertEquals(Integer.valueOf(0), result.b);
		
		result = rep.findNodeAt(new Point2D(3, 2));
		assertEquals(nodeB, result.a);
		assertEquals(Integer.valueOf(1), result.b);
		
		result = rep.findNodeAt(new Point2D(-1, 3));
		assertEquals(nodeC, result.a);
		assertEquals(Integer.valueOf(2), result.b);
	}

	@Test
	public void findNodeAt_ForUnavailableNode() {
		var mapBounds = new Rect2D(-100, -100, 100, 100);
		var map = new Map(mapBounds, new Random());
		
		var nodeA = new MapNode(new Point2D(2, 0), map);
		var nodeB = new MapNode(new Point2D(3, 2), map);
		var nodeC = new MapNode(new Point2D(-1, 3), map);
		
		Map.Representation rep = new Map.Representation();
		rep.addNode(nodeA);
		rep.addNode(nodeB);
		rep.addNode(nodeC);
		
		var result = rep.findNodeAt(new Point2D(0, 1));
		assertNull(result);
	}
}
