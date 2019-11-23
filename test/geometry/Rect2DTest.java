package geometry;

import static org.junit.Assert.*;
import org.junit.Test;


public class Rect2DTest {

	@Test
	public void construct_WithNormalizedCoords() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		assertEquals(1.0, r.left(), 0.0);
		assertEquals(2.0, r.top(), 0.0);
		assertEquals(3.0, r.right(), 0.0);
		assertEquals(4.0, r.bottom(), 0.0);
	}

	@Test
	public void construct_WithDenormalizedCoords() {
		Rect2D r = new Rect2D(4, 3, 2, 1);
		assertEquals(2.0, r.left(), 0.0);
		assertEquals(1.0, r.top(), 0.0);
		assertEquals(4.0, r.right(), 0.0);
		assertEquals(3.0, r.bottom(), 0.0);
	}

	@Test
	public void construct_WithPoints() {
		Rect2D r = new Rect2D(new Point2D(1, 2), new Point2D(3, 4));
		assertEquals(1.0, r.left(), 0.0);
		assertEquals(2.0, r.top(), 0.0);
		assertEquals(3.0, r.right(), 0.0);
		assertEquals(4.0, r.bottom(), 0.0);
	}

	@Test
	public void copy() {
		Rect2D r = new Rect2D(new Point2D(1, 2), new Point2D(3, 4));
		Rect2D copy = r.copy();
		assertEquals(1.0, copy.left(), 0.0);
		assertEquals(2.0, copy.top(), 0.0);
		assertEquals(3.0, copy.right(), 0.0);
		assertEquals(4.0, copy.bottom(), 0.0);
	}

	@Test
	public void equals_WithSelf() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		assertTrue(r.equals(r));
	}

	@Test
	public void equals_WithNull() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		assertFalse(r.equals(null));
	}

	@Test
	public void equals_WithOtherClass() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		assertFalse(r.equals(Double.valueOf(2.0)));
	}

	@Test
	public void equals_WhenEqual() {
		Rect2D r1 = new Rect2D(1, 2, 3, 4);
		Rect2D r2 = new Rect2D(1, 2, 3, 4);
		assertTrue(r1.equals(r2));
	}

	@Test
	public void equals_WhenInequal() {
		Rect2D r1 = new Rect2D(1, 2, 3, 4);
		Rect2D r2 = new Rect2D(5, 6, 7, 8);
		assertFalse(r1.equals(r2));
	}

	@Test
	public void isDegenerate_ForDegenerateRect() {
		assertTrue(new Rect2D(1, 2, 1, 3).isDegenerate());
		assertTrue(new Rect2D(1, 2, 2, 2).isDegenerate());
		assertTrue(new Rect2D(1, 1, 1, 1).isDegenerate());
	}

	@Test
	public void isDegenerate_ForNotDegenerateRect() {
		assertFalse(new Rect2D(1, 2, 2, 3).isDegenerate());
		assertFalse(new Rect2D(-1, -2, 1, -1).isDegenerate());
	}
	
	@Test
	public void setLeft_Normalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setLeft(2);
		assertEquals(2.0, r.left(), 0.0);
		assertEquals(2.0, r.top(), 0.0);
		assertEquals(3.0, r.right(), 0.0);
		assertEquals(4.0, r.bottom(), 0.0);
	}

	@Test
	public void setLeft_Denormalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setLeft(5);
		assertEquals(3.0, r.left(), 0.0);
		assertEquals(2.0, r.top(), 0.0);
		assertEquals(5.0, r.right(), 0.0);
		assertEquals(4.0, r.bottom(), 0.0);
	}

	@Test
	public void setRight_Normalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setRight(1);
		assertEquals(1.0, r.left(), 0.0);
		assertEquals(2.0, r.top(), 0.0);
		assertEquals(1.0, r.right(), 0.0);
		assertEquals(4.0, r.bottom(), 0.0);
	}

	@Test
	public void setRight_Denormalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setRight(-1);
		assertEquals(-1.0, r.left(), 0.0);
		assertEquals(2.0, r.top(), 0.0);
		assertEquals(1.0, r.right(), 0.0);
		assertEquals(4.0, r.bottom(), 0.0);
	}

	@Test
	public void setTop_Normalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setTop(0);
		assertEquals(1.0, r.left(), 0.0);
		assertEquals(0.0, r.top(), 0.0);
		assertEquals(3.0, r.right(), 0.0);
		assertEquals(4.0, r.bottom(), 0.0);
	}

	@Test
	public void setTop_Denormalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setTop(5);
		assertEquals(1.0, r.left(), 0.0);
		assertEquals(4.0, r.top(), 0.0);
		assertEquals(3.0, r.right(), 0.0);
		assertEquals(5.0, r.bottom(), 0.0);
	}

	@Test
	public void setBottom_Normalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setBottom(8);
		assertEquals(1.0, r.left(), 0.0);
		assertEquals(2.0, r.top(), 0.0);
		assertEquals(3.0, r.right(), 0.0);
		assertEquals(8.0, r.bottom(), 0.0);
	}

	@Test
	public void setBottomDenormalized() {
		Rect2D r = new Rect2D(1, 2, 3, 4);
		r.setBottom(-1);
		assertEquals(1.0, r.left(), 0.0);
		assertEquals(-1.0, r.top(), 0.0);
		assertEquals(3.0, r.right(), 0.0);
		assertEquals(2.0, r.bottom(), 0.0);
	}

	@Test
	public void width() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		assertEquals(2.0, r.width(), 0.0);
	}

	@Test
	public void height() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		assertEquals(5.0, r.height(), 0.0);
	}

	@Test
	public void leftTop() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		Point2D p = r.leftTop();
		assertEquals(1.0, p.x, 0.0);
		assertEquals(2.0, p.y, 0.0);
	}

	@Test
	public void rightTop() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		Point2D p = r.rightTop();
		assertEquals(3.0, p.x, 0.0);
		assertEquals(2.0, p.y, 0.0);
	}

	@Test
	public void leftBottom() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		Point2D p = r.leftBottom();
		assertEquals(1.0, p.x, 0.0);
		assertEquals(7.0, p.y, 0.0);
	}

	@Test
	public void rightBottom() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		Point2D p = r.rightBottom();
		assertEquals(3.0, p.x, 0.0);
		assertEquals(7.0, p.y, 0.0);
	}

	@Test
	public void center() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		Point2D p = r.center();
		assertEquals(2.0, p.x, 0.0);
		assertEquals(4.5, p.y, 0.0);
	}

	@Test
	public void inflate() {
		Rect2D r = new Rect2D(1, 2, 3, 7);
		r.inflate(2);
		assertEquals(-1.0, r.left(), 0.0);
		assertEquals(0.0, r.top(), 0.0);
		assertEquals(5.0, r.right(), 0.0);
		assertEquals(9.0, r.bottom(), 0.0);
	}

	@Test
	public void inflate_WithNegativeValues() {
		Rect2D r = new Rect2D(1, 2, 5, 7);
		r.inflate(-1);
		assertEquals(2.0, r.left(), 0.0);
		assertEquals(3.0, r.top(), 0.0);
		assertEquals(4.0, r.right(), 0.0);
		assertEquals(6.0, r.bottom(), 0.0);
	}
}
