package math;

import static org.junit.Assert.*;
import org.junit.Test;


public class IntervalTest {

	@Test
	public void construct_Denormalized() {
		Interval i = new Interval(3, 2);
		assertEquals(2.0, i.a, 0.0);
		assertEquals(3.0, i.b, 0.0);
	}

	@Test
	public void copy() {
		Interval i = new Interval(2, 3);
		Interval copy = i.copy();
		assertEquals(copy, i);
		assertFalse(copy == i);
	}

	@Test
	public void equals_WithSelf() {
		Interval i = new Interval(2, 3);
		assertTrue(i.equals(i));
	}

	@Test
	public void equals_WithNull() {
		Interval i = new Interval(2, 3);
		assertFalse(i.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Interval i = new Interval(2, 3);
		assertFalse(i.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Interval i = new Interval(2, 3);
		Interval j = new Interval(2, 3);
		assertTrue(i.equals(j));
	}

	@Test
	public void equals_WhenInequal() {
		Interval i = new Interval(2, 3);
		Interval j = new Interval(3, 4);
		assertFalse(i.equals(j));
	}

	@Test
	public void length() {
		Interval i = new Interval(2, 3);
		assertEquals(1.0, i.length(), 0.00001);
	}

	@Test
	public void length_ForEmptyInterval() {
		Interval i = new Interval(2, 2);
		assertEquals(0.0, i.length(), 0.00001);
	}

	@Test
	public void empty_ForEmptyInterval() {
		Interval i = new Interval(2, 2);
		assertTrue(i.isEmpty());
	}

	@Test
	public void empty_ForNonEmptyInterval() {
		Interval i = new Interval(2, 3);
		assertFalse(i.isEmpty());
	}

	@Test
	public void contains_ForValueInInterval() {
		Interval i = new Interval(2, 3);
		assertTrue(i.contains(2.3));
	}

	@Test
	public void contains_ForValueNotInInterval() {
		Interval i = new Interval(2, 3);
		assertFalse(i.contains(1.3));
		assertFalse(i.contains(3.3));
	}

	@Test
	public void contains_ForValueOnBoundary() {
		Interval i = new Interval(2, 3);
		assertTrue(i.contains(2.0));
		assertTrue(i.contains(3.0));
	}

	@Test
	public void intersect_ForOverlappingIntervals() {
		Interval i = new Interval(2, 4);
		Interval j = new Interval(2.5, 5);
		Interval isect = i.intersect(j);
		assertEquals(isect, new Interval(2.5, 4));
	}

	@Test
	public void intersect_ForDisjointIntervals() {
		Interval i = new Interval(2, 4);
		Interval j = new Interval(5, 5.5);
		Interval isect = i.intersect(j);
		assertTrue(isect == null);
	}

	@Test
	public void intersect_ForFullyContainedIntervals() {
		Interval i = new Interval(2, 4);
		Interval j = new Interval(2.5, 3.5);
		Interval isect = i.intersect(j);
		assertEquals(isect, new Interval(2.5, 3.5));
	}

	@Test
	public void unite_ForOverlappingIntervals() {
		Interval i = new Interval(2, 4);
		Interval j = new Interval(2.5, 5);
		Interval u = i.unite(j);
		assertEquals(u, new Interval(2, 5));
	}

	@Test
	public void unite_ForDisjointIntervals() {
		Interval i = new Interval(2, 4);
		Interval j = new Interval(5, 5.5);
		Interval u = i.unite(j);
		assertEquals(u, new Interval(2, 5.5));
	}

	@Test
	public void unite_ForFullyContainedIntervals() {
		Interval i = new Interval(2, 4);
		Interval j = new Interval(2.5, 3.5);
		Interval u = i.unite(j);
		assertEquals(u, new Interval(2, 4));
	}
}
