package geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



// Algorithm to perform a Voronoi tesselation.
public class VoronoiTesselation {

	///////////////
	
	// An edge of the Delauney triangulation and the one or two triangles that
	// it is part of.
	private static class DelauneyEdge {
		private LineSegment2D edge;
		private DelauneyTriangle[] triangles = {null, null};
		
		public DelauneyEdge(LineSegment2D edge,
				DelauneyTriangle t) {
			this.edge = edge;
			triangles[0] = t;
		}
		
		// Add a given triangle to the triangles that the edge is part of.
		public void addTriangle(DelauneyTriangle t) {
			triangles[1] = t;
		}
		
		// Checks if this edge is the same as a given edge. The check is direction
		// insensitive.
		public boolean isEdge(LineSegment2D e) {
			Point2D sa = edge.startPoint();
			Point2D ea = edge.endPoint();
			Point2D sb = e.startPoint();
			Point2D eb = e.endPoint();
			return (sa.equals(sb) && ea.equals(eb)) ||
					(sa.equals(eb) && ea.equals(sb));
		}

		// Creates a Voronoi edge for this edge.
		public Line2D makeVoronoiEdge() {
			if (triangles[1] == null)
				return makeInfiniteVoronoiEdge(edge, triangles[0]);
			return makeVoronoiEdgeBetweenTriangles(triangles[0], triangles[1]);
		}
		
		// Creates a Voronoi edge between two given Delauney triangles.
		private static Line2D makeVoronoiEdgeBetweenTriangles(
				DelauneyTriangle a, DelauneyTriangle b) {
			Point2D ca = a.circumcenter();
			Point2D cb = b.circumcenter();
			if (!ca.equals(cb))
				return new LineSegment2D(ca, cb);
			// Degenerate edge.
			return null;
		}
		
		// Creates a Voronoi edge for a given Delauney edge that only has one
		// adjacent Delauney triangle.
		private static Line2D makeInfiniteVoronoiEdge(LineSegment2D delauneyEdge,
				DelauneyTriangle triangle) {
			// The cirumcenter of the triangle is the start point of the Voronoi
			// edge and the edge travels in the direction away from the triangle.
			// Since the triangle is oriented ccw, a cw normal to any of its edges
			// points away from it.
			return new LineRay2D(triangle.circumcenter(),
					delauneyEdge.direction().cwNormal());
		}
	}
	
	///////////////
	
	// Collection of Delauney edges involved in constructing of a Voronoi region.
	private static class DelauneyEdgeCollection {
		private List<DelauneyEdge> edges = new ArrayList<DelauneyEdge>();
		
		// Adds an edge to the collection.
		public void addEdge(LineSegment2D edge, DelauneyTriangle t) {
			int pos = findEdge(edge);
			if (pos != -1)
				edges.get(pos).addTriangle(t);
			else
				edges.add(new DelauneyEdge(edge, t));
		}
		
		// Generates Voronoi edges for the collection of Delauney edges.
		public List<Line2D> makeVoronoiEdges() {
			List<Line2D> voronoiEdges = new ArrayList<Line2D>();
			for (DelauneyEdge de : edges) {
				Line2D ve = de.makeVoronoiEdge();
				if (ve != null)
					voronoiEdges.add(ve);
			}
			return voronoiEdges;
		}
		
		// Returns the index of a given edge it it is in the collection or null.
		private int findEdge(LineSegment2D edge) {
			for (int i = 0; i < edges.size(); ++i)
				if (edges.get(i).isEdge(edge))
					return i;
			return -1;
		}
	}

	///////////////
	
	// Builds a polygon from an unordered list of edges. Clips polygon at given bounds.
	private static class PolygonBuilder {
		private List<Line2D> edges;
		private final Polygon2D clip;
		
		public PolygonBuilder(List<Line2D> edges, Rect2D clipBounds) {
			this.edges = copyEdges(edges);
			this.clip = makePolygon(clipBounds);
		}
		
		// Builds the polygon.
		public Polygon2D build() {
			Polygon2D unclipped = new Polygon2D(createVertexSequence());
			return ConvexPolygonIntersection2D.intersect(unclipped, clip);
		}
		
		// Creates a deep copy of a given list of edges.
		private static List<Line2D> copyEdges(List<Line2D> src) {
			List<Line2D> copy = new ArrayList<Line2D>();
			for (Line2D e : src)
				copy.add((Line2D) e.copy());
			return copy;
		}
		
		// Returns an order list of vertices that results from connecting the
		// available edges.
		private List<Point2D> createVertexSequence() {
			if (edges.isEmpty())
				return new ArrayList<Point2D>();
			
			List<Line2D> endEdges = findEndEdges();
			List<Point2D> vertices = orderEdges(endEdges);
			
			return vertices;
		}
		
		// Returns those edges from a given list that do not connect to an other
		// edge at one of their endpoints.
		private List<Line2D> findEndEdges() {
			List<Line2D> res = new ArrayList<Line2D>();
			
			// Keep track of found edge objects so that we can delete them later.
			int numFound = 0;
			Line2D foundEdges[] = new Line2D[] {null, null};
			
			for (int i = 0; i < edges.size(); ++i) {
				Line2D e = edges.get(i);
				if (!e.hasEndPoint()) {
					res.add(e);
					foundEdges[numFound++] = e;
				}					
					
				// We can stop after we found two.
				if (numFound > 1)
					break;
			}

			// Remove the found end edges from the available edges.
			for (int i = 0; i < foundEdges.length; ++i) {
				if (foundEdges[i] != null)
					edges.remove(foundEdges[i]);
			}

			return res;
		}
		
		// Orders the available edges. Uses given edges at start and end.
		private List<Point2D> orderEdges(List<Line2D> endEdges) {
			// There should be either zero or two end edges.
			boolean isOpenPath = endEdges.size() == 2;
			
			List<Point2D> vertices = new ArrayList<Point2D>();

			Line2D nextEdge = null;
			if (isOpenPath) {
				// Process the given start edge first.
				Line2D startEdge = endEdges.get(0);
				vertices.add(calcDistantPoint(startEdge));
				nextEdge = findNextEdge(startEdge.startPoint());
			} else {
				// We can start with any edges. Use the first one.
				nextEdge = edges.get(0);
				edges.remove(0);
			}
			
			// Concatenate the edges and store each start point. 
			while (nextEdge != null) {
				vertices.add(nextEdge.startPoint());
				nextEdge = findNextEdge(nextEdge.endPoint());
			}

			// Append the end points of the given end edge.
			if (isOpenPath) {
				vertices.add(endEdges.get(1).startPoint());
				vertices.add(calcDistantPoint(endEdges.get(1)));
				
				fixIntersectingEndEdges(vertices);
			}
			
			return vertices;
		}
		
		// Returns a point far past the end point of a given line.
		private static Point2D calcDistantPoint(Line2D edge) {
			final double DIST = 100000;
			Vector2D normedDir = edge.direction().normalize();
			return edge.startPoint().offset(normedDir.scale(DIST));
		}
		
		// Finds the edge that connects to a given previous edge.
		private Line2D findNextEdge(Point2D connector) {
			if (connector == null)
				return null;
			
			int edgeIdx = findEndpoint(connector, -1); 
			if (edgeIdx == -1)
				return null;

			Line2D e = edges.get(edgeIdx); 
			edges.remove(edgeIdx);
			
			// Make sure the end points of the edge are in the correct order.
			if (connector.equals(e.startPoint()))
				return e.copy();
			// Flip the found edge.
			return new LineSegment2D(e.endPoint(), e.startPoint());
		}
		
		// Checks if a given point exists as endpoint of an edge excluding
		// a specific edge at a given index. Use -1 to include all edges.
		private int findEndpoint(Point2D pt, int excludedIdx) {
			for (int i = 0; i <edges.size(); ++i) {
				if (i != excludedIdx &&
						(edges.get(i).startPoint().equals(pt) ||
						edges.get(i).endPoint().equals(pt))) {
					return i;
				}
			}
			return -1;
		}
		
		// Make sures end edges do not intersect before they end at their distant
		// end points. The polygon would not be convex in that case, creating
		// problems when intersecting it with the border.
		private static void fixIntersectingEndEdges(List<Point2D> vertices) {
			LineIntersection2D.Result isect = LineIntersection2D.intersect(
					new LineSegment2D(vertices.get(1), vertices.get(0)),
					new LineSegment2D(vertices.get(vertices.size() - 2),
							vertices.get(vertices.size() - 1)));
			if (isect.type == LineIntersection2D.IntersectionType.POINT) {
				Point2D isectPt = (Point2D) isect.intersection;
				
				// It's ok if the intersection is at the start points.
				if (!isectPt.equals(vertices.get(1)) &&
						!isectPt.equals(vertices.get(vertices.size() - 2))) {
					vertices.set(0, isectPt);
					vertices.set(vertices.size() - 1, isectPt.copy());
				}
			}
		}
	}
	
	///////////////
	
	// List of points to generate the Voronoi tesselation for.
	private final List<Point2D> samples;
	// Border around the sample points. Used to terminate Voronoi edges that
	// would extend to infinity.
	private final Rect2D border;
	// List of regions generated by the the tesselation.
	private List<VoronoiRegion> regions = new ArrayList<VoronoiRegion>();
	// Triangles of the Delauney triangulation. A by-product of the tesselation
	// that can be useful, e.g. for debugging.
	private List<Triangle2D> triangulation;

	
	// Construct from points with the bounding box of the points as border.
	public VoronoiTesselation(Set<Point2D> samplePoints) {
		this(samplePoints, 0.0);
	}

	// Construct from points with the bounding box of the points offset by a given
	// distance as border.
	public VoronoiTesselation(Set<Point2D> samplePoints, double borderOffset) {
		this(samplePoints,
				calcBorder(new ArrayList<Point2D>(samplePoints), borderOffset));
	}

	// Construct from points with a given border.
	public VoronoiTesselation(Set<Point2D> samplePoints, Rect2D border) {
		samples = new ArrayList<Point2D>(samplePoints);
		this.border = border;
	}

	// Starts the Voronoi tesselation.
	public List<VoronoiRegion> tesselate() {
		// Handle some degenerate cases.
		if (samples.size() == 0)
			return regions;
		if (samples.size() == 1)
			return tesselateWithSingleRegion();
		if (samples.size() == 2)
			return tesselateWithTwoRegions();
		
		// General case for more than three sample points.
		// - Each sample point is the seed of a Voronoi region.
		// - Perform Delauney triangulation.
		// - For each sample point collect all Delauney edges that share it.
		// - From each Delauney edge build the corresponding Voronoi edge as
		//   follows:
		// -   If two Delauney triangles share the edge, then the Voronoi edge
		//     is the line connecting the triangles' circumcenters.
		// -   If only one Delauney triangle contains the edge, then the Voronoi
		//     edge is the line from the triangle's circumcenter to the closest
		//     border.
		// - Combine the collected Voronoi edges into a polygon that forms the
		//   border of the Voronoi region for the processed sample point.
		
		final List<DelauneyTriangle> delauneyTriangles =
				delauneyTriangulation();
		
		for (Point2D sample : samples) {
			DelauneyEdgeCollection delauneyEdges =
					collectDelauneyEdges(sample, delauneyTriangles);
			List<Line2D> voronoiEdges = delauneyEdges.makeVoronoiEdges();
			
			Polygon2D voronoiPoly = makePolygon(voronoiEdges, border);
			if (voronoiPoly.countVertices() > 0)
				regions.add(new VoronoiRegion(sample, voronoiPoly));
		}
		
		return regions;
	}
	
	// Returns the Delauney triangulation that was used to perform the tesselation.  
	public List<Triangle2D> getTriangulation() {
		return triangulation;
	}
	
	// Calculates bounding box at a given offset around a given list of points.
	private static Rect2D calcBorder(List<Point2D> points, double offset) {
		Rect2D border = GeometryUtil.calcBoundingBox(points);
		border.inflate(offset);
		return border;
	}
	
	// Degenerate tesselation with a single region.
	private List<VoronoiRegion> tesselateWithSingleRegion() {
		Point2D sample = samples.get(0);
		
		Polygon2D regionBorder = null;
		if (border.isDegenerate()) {
			// Region degenerates into single point.
			regionBorder = new Polygon2D();
			regionBorder.addVertex(sample);
		} else {
			// Region covers the entire area.
			regionBorder = makePolygon(border);
		}
		
		regions.add(new VoronoiRegion(samples.get(0), regionBorder));
		return regions;
	}
	
	// Degenerate tesselation with two regions.
	private List<VoronoiRegion> tesselateWithTwoRegions() {
		Point2D pa = samples.get(0);
		Point2D pb = samples.get(1);
		
		// Split area into two regions along the bisection of
		// the edge between the two sample points.
		LineSegment2D sampleEdge = new LineSegment2D(pa, pb);
		Vector2D normal = sampleEdge.direction().ccwNormal();
		InfiniteLine2D bisection = new InfiniteLine2D(sampleEdge.midPoint(), normal);
		
		List<Polygon2D> regionPolys = ConvexPolygonLineCut2D.cut(makePolygon(border),
				bisection);
		if (regionPolys.size() == 2) {
			// Figure out which polygon belongs to which sample point.
			boolean isFirstPolyForA = areOnSameSideOf(pa, regionPolys.get(0), bisection);
			regions.add(new VoronoiRegion(pa, regionPolys.get(isFirstPolyForA ? 0 : 1)));
			regions.add(new VoronoiRegion(pb, regionPolys.get(isFirstPolyForA ? 1 : 0)));
		} else {
			// Unexpected case. Abandon tesselation.
			regions.clear();
		}
		
		return regions;
	}
	
	// Performs a Delauney triangulation for the configured sample points.
	private List<DelauneyTriangle> delauneyTriangulation() {
		DelauneyTriangulation delauney = new DelauneyTriangulation(
				new HashSet<Point2D>(samples));
		triangulation = delauney.triangulate();
		return delauney.delauneyTriangles();
	}
	
	// Collects all edges of Delauney triangles that share a given sample
	// point.
	private DelauneyEdgeCollection collectDelauneyEdges(Point2D sample,
			List<DelauneyTriangle> delauneyTriangles) {
		DelauneyEdgeCollection edges = new DelauneyEdgeCollection();
		
		for (DelauneyTriangle t : delauneyTriangles) {
			int pos = t.findVertex(sample);
			if (pos != -1) {
				// Keep edges ordered.
				edges.addEdge(new LineSegment2D(t.vertex(pos == 0 ? 2 : pos - 1),
						t.vertex(pos)), t);
				edges.addEdge(new LineSegment2D(t.vertex(pos),
						t.vertex(pos == 2 ? 0 : pos + 1)), t);
			}
		}
		
		return edges;
	}
	
	// Creates a polygon from given unordered edges.
	private static Polygon2D makePolygon(List<Line2D> edges, Rect2D borderBounds) {
		PolygonBuilder builder = new PolygonBuilder(edges, borderBounds);
		return builder.build();
	}

	// Creates a polygon from a given rectangle.
	private static Polygon2D makePolygon(Rect2D r) {
		Polygon2D poly = new Polygon2D();
		poly.addVertex(r.leftTop());
		poly.addVertex(r.leftBottom());
		poly.addVertex(r.rightBottom());
		poly.addVertex(r.rightTop());
		return poly;
	}
	
	// Checks if a given point and polygon are on the same side of a given line.
	private static boolean areOnSameSideOf(Point2D pt, Polygon2D poly,
			InfiniteLine2D line) {
		Vector2D lineDir = line.direction();
		boolean isLeft = lineDir.isCcw(new Vector2D(line.anchorPoint(), pt));
		
		for (int i = 0; i < poly.countVertices(); ++i) {
			Point2D polyPt = poly.vertex(i);
			boolean isPolyPtOnLine = line.isPointOnLine(polyPt).isOnLine;
			boolean isPolyPtLeft = lineDir.isCcw(
					new Vector2D(line.anchorPoint(), polyPt));
			if (!isPolyPtOnLine && isPolyPtLeft != isLeft)
				return false;
		}
		
		return true;
	}
}
