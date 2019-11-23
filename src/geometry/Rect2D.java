package geometry;

import java.util.Objects;

import math.MathUtil;

// 2-dimensional rectangle.
// Always normalized: left <= right, top <= bottom
public class Rect2D extends Object {
	private double left;
	private double top;
	private double right;
	private double bottom;
	
	public Rect2D() {
		this(0, 0, 0, 0);
	}

	public Rect2D(double l, double t, double r, double b) {
		// Normalize.
		left = l <= r ? l : r;
		top = t <= b ? t : b;
		right = r > l ? r : l;
		bottom = b > t ? b : t;
	}

	public Rect2D(Point2D leftTop, Point2D rightBot) {
		this(leftTop.x, leftTop.y, rightBot.x, rightBot.y);
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		Rect2D otherRt = (Rect2D) other;
		return MathUtil.fpEqual(left, otherRt.left) &&
				MathUtil.fpEqual(top, otherRt.top) &&
				MathUtil.fpEqual(right, otherRt.right) &&
				MathUtil.fpEqual(bottom, otherRt.bottom);
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, top, right, bottom);
	}
	
	public Rect2D copy() {
		return new Rect2D(left, top, right, bottom);
	}

	public boolean isDegenerate() {
		return MathUtil.fpEqual(left, right) || MathUtil.fpEqual(top,  bottom);
	}
	
	public double left() {
		return left;
	}
	
	public void setLeft(double val) {
		left = val;
		normalize();
	}
	
	public double right() {
		return right;
	}
	
	public void setRight(double val) {
		right = val;
		normalize();
	}
	
	public double top() {
		return top;
	}
	
	public void setTop(double val) {
		top = val;
		normalize();
	}
	
	public double bottom() {
		return bottom;
	}
	
	public void setBottom(double val) {
		bottom = val;
		normalize();
	}
	
	public double width() {
		return right - left;
	}
	
	public double height() {
		return bottom - top;
	}
	
	public Point2D leftTop() {
		return new Point2D(left, top);
	}
	
	public Point2D rightTop() {
		return new Point2D(right, top);
	}
	
	public Point2D leftBottom() {
		return new Point2D(left, bottom);
	}
	
	public Point2D rightBottom() {
		return new Point2D(right, bottom);
	}
	
	public Point2D center() {
		return new Point2D((left + right) / 2.0, (top + bottom) / 2.0);
	}
	
	public void inflate(double by) {
		left -= by;
		right += by;
		top -= by;
		bottom += by;
	}
	
	private void normalize() {
		if (left > right) {
			double tmp = left;
			left = right;
			right = tmp;
		}
		if (top > bottom) {
			double tmp = top;
			top = bottom;
			bottom = tmp;
		}
	}
}
