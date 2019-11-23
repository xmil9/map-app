package geometry;

import java.util.Objects;

import math.MathUtil;


public class Circle2D extends Object {

	public final Point2D center;
	public final double radius;
	
	public Circle2D(Point2D center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (getClass() != other.getClass())
			return false;
		Circle2D otherCircle = (Circle2D) other;
		return center.equals(otherCircle.center) &&
				MathUtil.fpEqual(radius, otherCircle.radius);
	}

	@Override
	public int hashCode() {
		return Objects.hash(center, radius);
	}
	
	public Circle2D copy() {
		return new Circle2D(center.copy(), radius);
	}
	
	public boolean isPoint() {
		return MathUtil.fpEqual(radius, 0.0);
	}
	
	public Circle2D offset(Vector2D v) {
		return new Circle2D(center.offset(v), radius);
	}
	
	// Checks if a given point is in the circle (inside or on the circle).
	public boolean isPointInCircle(Point2D pt) {
		double distSquared = new Vector2D(pt, center).lengthSquared();
		return MathUtil.fpLessEqual(distSquared, radius * radius);
	}
	
	// Checks if a given point is on the circle.
	public boolean isPointOnCircle(Point2D pt) {
		double distSquared = new Vector2D(pt, center).lengthSquared();
		return MathUtil.fpEqual(distSquared, radius * radius);
	}
	
	// Checks if a given point is strictly inside the circle (not on the circle).
	public boolean isPointInsideCircle(Point2D pt) {
		double distSquared = new Vector2D(pt, center).lengthSquared();
		return MathUtil.fpLess(distSquared, radius * radius);
	}
	
	// Returns a point along the circle's circumference at a given angle measured
	// in radians.
	// Zero radians results in the point at 3 o'clock on the circle.
	// Larger radian values (up to 2pi) result in points moving counter-clockwise
	// around the circle.
	public Point2D pointAtRadian(double angleInRadians) {
		double x = center.x + radius * Math.cos(angleInRadians);
		double y = center.y + radius * Math.sin(angleInRadians);
		return new Point2D(x, y);
	}
}
