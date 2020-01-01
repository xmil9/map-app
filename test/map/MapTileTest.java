package map;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import geometry.Point2D;
import geometry.Polygon2D;

public class MapTileTest {

	@Test
	public void equals_WithSelf() {
		Polygon2D shape = new Polygon2D();
		shape.addVertex(new Point2D(3, 4));
		shape.addVertex(new Point2D(1, 1));
		shape.addVertex(new Point2D(2, 6));
		MapTile t = new MapTile(new Point2D(1, 2), shape);
		
		assertTrue(t.equals(t));
	}

	@Test
	public void equals_WithNull() {
		Polygon2D shape = new Polygon2D();
		shape.addVertex(new Point2D(3, 4));
		shape.addVertex(new Point2D(1, 1));
		shape.addVertex(new Point2D(2, 6));
		MapTile t = new MapTile(new Point2D(1, 2), shape);
		
		assertFalse(t.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Polygon2D shape = new Polygon2D();
		shape.addVertex(new Point2D(3, 4));
		shape.addVertex(new Point2D(1, 1));
		shape.addVertex(new Point2D(2, 6));
		MapTile t = new MapTile(new Point2D(1, 2), shape);

		assertFalse(t.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Polygon2D shape = new Polygon2D();
		shape.addVertex(new Point2D(3, 4));
		shape.addVertex(new Point2D(1, 1));
		shape.addVertex(new Point2D(2, 6));
		MapTile t = new MapTile(new Point2D(1, 2), shape);

		assertTrue(t.equals(t));
	}

	@Test
	public void equals_WhenInequal() {
		Polygon2D shapeA = new Polygon2D();
		shapeA.addVertex(new Point2D(3, 4));
		shapeA.addVertex(new Point2D(1, 1));
		shapeA.addVertex(new Point2D(2, 6));
		MapTile ta = new MapTile(new Point2D(1, 2), shapeA);

		Polygon2D shapeB = new Polygon2D();
		shapeB.addVertex(new Point2D(3, 4));
		shapeB.addVertex(new Point2D(1, 1));
		shapeB.addVertex(new Point2D(2, 6));
		MapTile tb = new MapTile(new Point2D(2, 0), shapeB);

		assertFalse(ta.equals(tb));
	}

	@Test
	public void setNodes() {
		var p1 = new Point2D(3, 4);
		var p2 = new Point2D(1, 1);
		var p3 = new Point2D(2, 6);
		Polygon2D shape = new Polygon2D();
		shape.addVertex(p1);
		shape.addVertex(p2);
		shape.addVertex(p3);
		MapTile t = new MapTile(new Point2D(1, 2), shape);

		List<MapNode> nodes = new ArrayList<MapNode>();
		nodes.add(new MapNode(p1));
		nodes.add(new MapNode(p2));
		nodes.add(new MapNode(p3));
		
		t.setNodes(nodes);
		
		assertTrue(t.countNodes() == 3);
	}

	@Test
	public void countNodes() {
		// Covered by setNodes test.
	}

	@Test
	public void node() {
		var p1 = new Point2D(3, 4);
		var p2 = new Point2D(1, 1);
		var p3 = new Point2D(2, 6);
		Polygon2D shape = new Polygon2D();
		shape.addVertex(p1);
		shape.addVertex(p2);
		shape.addVertex(p3);
		MapTile t = new MapTile(new Point2D(1, 2), shape);

		List<MapNode> nodes = new ArrayList<MapNode>();
		MapNode n1 = new MapNode(p1);
		nodes.add(n1);
		MapNode n2 = new MapNode(p2);
		nodes.add(n2);
		MapNode n3 = new MapNode(p3);
		nodes.add(n3);
		
		t.setNodes(nodes);
		
		assertEquals(n1, t.node(0));
		assertEquals(n2, t.node(1));
		assertEquals(n3, t.node(2));
	}

	@Test
	public void addNeighbor() {
		Polygon2D shapeA = new Polygon2D();
		shapeA.addVertex(new Point2D(3, 4));
		shapeA.addVertex(new Point2D(1, 1));
		shapeA.addVertex(new Point2D(2, 6));
		MapTile ta = new MapTile(new Point2D(1, 2), shapeA);

		Polygon2D shapeB = new Polygon2D();
		shapeB.addVertex(new Point2D(3, 4));
		shapeB.addVertex(new Point2D(1, 1));
		shapeB.addVertex(new Point2D(2, 6));
		MapTile tb = new MapTile(new Point2D(2, 0), shapeB);

		Polygon2D shapeC = new Polygon2D();
		shapeC.addVertex(new Point2D(2, 3));
		shapeC.addVertex(new Point2D(1, 0));
		shapeC.addVertex(new Point2D(1.5, 7));
		MapTile tc = new MapTile(new Point2D(2, 2), shapeC);

		ta.addNeighbor(tb);
		ta.addNeighbor(tc);
		
		assertTrue(ta.countNeighbors() == 2);
	}

	@Test
	public void countNeighbors() {
		// Covered by addNeighbor test.
	}

	@Test
	public void neighbor() {
		Polygon2D shapeA = new Polygon2D();
		shapeA.addVertex(new Point2D(3, 4));
		shapeA.addVertex(new Point2D(1, 1));
		shapeA.addVertex(new Point2D(2, 6));
		MapTile ta = new MapTile(new Point2D(1, 2), shapeA);

		Polygon2D shapeB = new Polygon2D();
		shapeB.addVertex(new Point2D(3, 4));
		shapeB.addVertex(new Point2D(1, 1));
		shapeB.addVertex(new Point2D(2, 6));
		MapTile tb = new MapTile(new Point2D(2, 0), shapeB);

		Polygon2D shapeC = new Polygon2D();
		shapeC.addVertex(new Point2D(2, 3));
		shapeC.addVertex(new Point2D(1, 0));
		shapeC.addVertex(new Point2D(1.5, 7));
		MapTile tc = new MapTile(new Point2D(2, 2), shapeC);

		ta.addNeighbor(tb);
		ta.addNeighbor(tc);
		
		assertEquals(tb, ta.neighbor(0));
		assertEquals(tc, ta.neighbor(1));
	}

	@Test
	public void setElevation() {
		Polygon2D shape = new Polygon2D();
		shape.addVertex(new Point2D(3, 4));
		shape.addVertex(new Point2D(1, 1));
		shape.addVertex(new Point2D(2, 6));
		MapTile t = new MapTile(new Point2D(1, 2), shape);

		t.setElevation(5);
		
		assertEquals(5, t.elevation(), 0.0);
	}
}
