package geometry;

import java.util.Objects;

import math.FpUtil;

// Represents a point in 2D space.
public class Point2D extends Object {
	public final double x;
	public final double y;
	
	public Point2D() {
		this(0, 0);
	}
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		Point2D otherPt = (Point2D) other;
		return FpUtil.fpEqual(x, otherPt.x) &&
				FpUtil.fpEqual(y, otherPt.y);
	}

	@Override
	public int hashCode() {
		return Objects.hash(FpUtil.fpHashCode(x), FpUtil.fpHashCode(y));
	}
	
	@Override
	public String toString() {
		return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
	}
	
	public Point2D copy() {
		return new Point2D(x, y);
	}
	
	public Point2D offset(double dx, double dy) {
		return new Point2D(x + dx, y + dy);
	}
	
	public Point2D offset(Vector2D v) {
		return offset(v.x, v.y);
	}
	
	public static double distanceSquared(Point2D a, Point2D b) {
		double dx = b.x - a.x;
		double dy = b.y - a.y;
		return dx * dx + dy * dy;
	}
}
