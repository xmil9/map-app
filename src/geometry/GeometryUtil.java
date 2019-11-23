package geometry;

import java.util.List;

import math.MathUtil;


public class GeometryUtil {

	// Calculates a minimal rectangle around given points.
	public static Rect2D calcBoundingBox(List<Point2D> points) {
		if (points.size() == 0)
			return new Rect2D();
		if (points.size() == 1)
			return new Rect2D(points.get(0), points.get(0));
		
		Rect2D bounds = new Rect2D(points.get(0), points.get(1));
		
		for (int i = 2; i < points.size(); ++i) {
			Point2D pt = points.get(i);
			if (pt.x < bounds.left())
				bounds.setLeft(pt.x);
			if (pt.y < bounds.top())
				bounds.setTop(pt.y);
			if (pt.x > bounds.right())
				bounds.setRight(pt.x);
			if (pt.y > bounds.bottom())
				bounds.setBottom(pt.y);
		}
		
		return bounds;
	}

	// Checks if given points form a convex path.
	// Convex path - All edges bend in the same direction, don't cross, and are
	//               all on aone straight line.
	public static boolean isConvexPath(List<Point2D> path) {
		if (path.size() < 3)
			return false;
		if (path.size() == 3)
			return true;
		
		Vector2D prev = new Vector2D(path.get(0), path.get(1));
		Vector2D next = new Vector2D(path.get(1), path.get(2));
		double val = prev.perpDot(next);
		boolean orientation = MathUtil.fpGreaterEqual(val, 0);
		boolean isStraight = MathUtil.fpEqual(val, 0);
		
		for (int i = 2; i < path.size() - 1; ++i) {
			prev = next;
			next = new Vector2D(path.get(i), path.get(i + 1));
			val = prev.perpDot(next);
			boolean nextOrientation = MathUtil.fpGreaterEqual(val, 0.0);
			if (nextOrientation != orientation)
				return false;
			isStraight = isStraight && MathUtil.fpEqual(val, 0);
		}

		// Check closing edge.
		prev = next;
		next = new Vector2D(path.get(path.size() - 1), path.get(0));
		if (MathUtil.fpGreaterEqual(prev.perpDot(next), 0.0) != orientation)
			return false;

		return !isStraight;
	}
}
