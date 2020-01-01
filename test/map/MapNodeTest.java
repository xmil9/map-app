package map;

import static org.junit.Assert.*;
import org.junit.Test;

import geometry.Point2D;

public class MapNodeTest {

	@Test
	public void equals_WithSelf() {
		MapNode n = new MapNode(new Point2D(1, 2));
		assertTrue(n.equals(n));
	}

	@Test
	public void equals_WithNull() {
		MapNode n = new MapNode(new Point2D(1, 2));
		assertFalse(n.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		MapNode n = new MapNode(new Point2D(1, 2));
		assertFalse(n.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		MapNode na = new MapNode(new Point2D(1, 2));
		MapNode nb = new MapNode(new Point2D(1, 2));
		assertTrue(na.equals(nb));
	}

	@Test
	public void equals_WhenInequal() {
		MapNode na = new MapNode(new Point2D(1, 2));
		MapNode nb = new MapNode(new Point2D(2, 2));

		assertFalse(na.equals(nb));
	}

	@Test
	public void addNeighbor() {
		MapNode na = new MapNode(new Point2D(1, 2));
		MapNode nb = new MapNode(new Point2D(2, 2));
		MapNode nc = new MapNode(new Point2D(-2, 6));
		
		na.addNeighbor(nb);
		na.addNeighbor(nc);
		
		assertEquals(2, na.countNeighbors());
	}

	@Test
	public void countNeighbors() {
		// Covered by addNeighbor test.
	}

	@Test
	public void neighbor() {
		MapNode na = new MapNode(new Point2D(1, 2));
		MapNode nb = new MapNode(new Point2D(2, 2));
		MapNode nc = new MapNode(new Point2D(-2, 6));
		
		na.addNeighbor(nb);
		na.addNeighbor(nc);
		
		assertEquals(nb, na.neighbor(0));
		assertEquals(nc, na.neighbor(1));
	}

	@Test
	public void setElevation() {
		MapNode n = new MapNode(new Point2D(1, 2));
		n.setElevation(5);
		assertEquals(5,  n.elevation(), 0.0);
	}
}
