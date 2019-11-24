package geometry;


public class PolygonIntersection2D {

	public static Polygon2D convexPolygons(Polygon2D a, Polygon2D b) {
		Polygon2D resultPoly = new Polygon2D();
		if (!a.isConvex() || !b.isConvex())
			return resultPoly;
		
		Polygon2D ccwA = makeCcw(a);
		Polygon2D ccwB = makeCcw(b);
		
		int maxIter = 2 * (a.countEdges() + b.countEdges());
		int numIter = 0;
		
		Point2D firstIsectPt = null;
		boolean foundFirstIsectInPrevIter = false;
		
		Point2D p = a.vertex(a.countVertices() - 1);
		LineSegment2D ep = a.edge(0);
		Point2D q = b.vertex(b.countVertices() - 1);
		LineSegment2D eq = b.edge(0);

		while (numIter <= maxIter) {
			LineIntersection2D.Result isect = LineIntersection2D.intersect(ep, eq);
			if (isect.type != LineIntersection2D.IntersectionType.NONE) {
				Point2D isectPt = (Point2D) isect.intersection;

			}
			
			++numIter;
		}
		
		return null;
	}
	
	private static Polygon2D makeCcw(Polygon2D poly) {
		if (!isCcw(poly))
			return poly.reversed();
		return poly;
	}
	
	private static boolean isCcw(Polygon2D poly) {
		return poly.edge(0).direction().isCcw(poly.edge(1).direction());
	}
}
