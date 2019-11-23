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
			return (sa.equals(sb) && ea.equals(eb)) &&
					(sa.equals(eb) && ea.equals(sb));
		}

		// Creates a Voronoi edge for this edge.
		public LineSegment2D makeVoronoiEdge(List<LineSegment2D> borders)
				throws GeometryException {
			if (triangles[1] == null) {
				return makeVoronoiEdgeToBorder(edge, triangles[0], borders);
			}
			return makeVoronoiEdgeBetweenTriangles(triangles[0], triangles[1]);
		}
		
		// Creates a Voronoi edge between two given Delauney triangles.
		private static LineSegment2D makeVoronoiEdgeBetweenTriangles(
				DelauneyTriangle a, DelauneyTriangle b) {
			return new LineSegment2D(a.circumcenter(), b.circumcenter());
		}
		
		// Creates a Voronoi edge for a given Delauney edge that is only part of
		// one Delauney triangle.
		private static LineSegment2D makeVoronoiEdgeToBorder(LineSegment2D delauneyEdge,
				DelauneyTriangle triangle, List<LineSegment2D> borders)
						throws GeometryException {
			// The cirumcenter of the triangle is the start point of the Voronoi
			// edge and the edge travels in the direction away from the triangle
			// until it meets a border edge. Since the triangle is oriented ccw,
			// a cw normal to any of its edges points away from it.
			LineRay2D bisector = new LineRay2D(triangle.circumcenter(),
					delauneyEdge.direction().cwNormal());

			Point2D borderPt = null;
			
			for (LineSegment2D borderEdge : borders) {
				LineIntersection2D.Result res =
						LineIntersection2D.intersect(borderEdge, bisector);
				if (res.type == LineIntersection2D.IntersectionType.POINT)
					borderPt = (Point2D) res.intersection;
			}
			
			if (borderPt == null) {
				throw new GeometryException(
						"Failed to find intersection of Voronoi edge with border.");
			}
			return new LineSegment2D(triangle.circumcenter(), borderPt);
		}
	}
	
	///////////////
	
	// Collection of Delauney edges involved in constructing of a Voronoi region.
	private static class DelauneyEdgeCollection {
		private List<DelauneyEdge> edges = new ArrayList<DelauneyEdge>();
		
		// Adds an edge to the collection.
		public void addEdge(LineSegment2D edge,
				DelauneyTriangle t) {
			int pos = findEdge(edge);
			if (pos != -1)
				edges.get(pos).addTriangle(t);
			else
				edges.add(new DelauneyEdge(edge, t));
		}
		
		// Generates Voronoi edges for the collection of Delauney edges.
		public List<LineSegment2D> makeVoronoiEdges(List<LineSegment2D> borders)
				throws GeometryException {
			List<LineSegment2D> voronoiEdges = new ArrayList<LineSegment2D>();
			for (DelauneyEdge de : edges)
				voronoiEdges.add(de.makeVoronoiEdge(borders));
			return voronoiEdges;
		}
		
		// Returns the index of a given edge it it is in the collection or null.
		private int findEdge(LineSegment2D edge) {
			for (int i = 0; i < edges.size() - 1; ++i)
				if (edges.get(i).isEdge(edge))
					return i;
			return -1;
		}
	}

	///////////////
	
	// Builds a polygon from an unordered list of edges. Vertices that touch given
	// border edges are connected along the borders.
	private static class PolygonBuilder {
		private List<LineSegment2D> edges;
		private final List<LineSegment2D> borders;
		
		public PolygonBuilder(List<LineSegment2D> edges, List<LineSegment2D> borders)
				throws GeometryException {
			if (edges.size() < 2) {
				throw new GeometryException(
						"PolygonBuilder is expecting two or more edges.");
			}
			this.edges = copyEdges(edges);
			this.borders = borders;
		}
		
		// Builds the polygon.
		public Polygon2D build() throws GeometryException {
			List<Point2D> orderedVertices = createVertexSequence();
			return new Polygon2D(orderedVertices);
		}
		
		// Creates a deep copy of a given list of edges.
		private static List<LineSegment2D> copyEdges(List<LineSegment2D> src) {
			List<LineSegment2D> copy = new ArrayList<LineSegment2D>();
			for (LineSegment2D e : src)
				copy.add((LineSegment2D) e.copy());
			return copy;
		}
		
		// Returns an order list of vertices that results from connecting the
		// available edges.
		private List<Point2D> createVertexSequence() throws GeometryException {
			if (edges.isEmpty())
				return new ArrayList<Point2D>();
			
			List<LineSegment2D> endEdges = findEndEdges();
			List<Point2D> vertices = orderEdges(endEdges);
			
			boolean isOpenPath = endEdges.size() > 0; 
			if (isOpenPath)
				closeWithBorder(vertices);
			
			return vertices;
		}
		
		// Returns those edges from a given list that do not connect to an other
		// edge at one of their endpoints. Orders the points of the returned edges
		// so that they form the first and last edge of a polygon.
		private List<LineSegment2D> findEndEdges() throws GeometryException {
			List<LineSegment2D> res = new ArrayList<LineSegment2D>();
			
			int numFound = 0;
			// Keep track of found edge objects so that we can delete them later.
			LineSegment2D foundEdges[] = new LineSegment2D[] {null, null};
			
			for (int i = 0; i < edges.size(); ++i) {
				Point2D start = edges.get(i).startPoint();
				Point2D end = edges.get(i).endPoint();
				boolean isStartEdge = (numFound == 0);
				
				if (findEndpoint(start, i) == -1) {
					res.add(new LineSegment2D(isStartEdge ? start : end,
							isStartEdge ? end : start));
					foundEdges[numFound++] = edges.get(i);
				} else if (findEndpoint(end, i) == -1) {
					res.add(new LineSegment2D(isStartEdge ? end : start,
							isStartEdge ? start : end));
					foundEdges[numFound++] = edges.get(i);
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
			
			// There should be either two or no edges.
			if (res.size() != 0 && res.size() != 2)
				throw new GeometryException(
						"Unexpected number of open edges in Voronoi region.");
			return res;
		}
		
		// Orders the available edges. Uses given edges at start and end.
		private List<Point2D> orderEdges(List<LineSegment2D> endEdges) {
			boolean isOpenPath = !endEdges.isEmpty();
			
			LineSegment2D nextEdge = null;
			if (isOpenPath) {
				// Use the given start edge.
				nextEdge = endEdges.get(0);
			} else {
				// We can start with any edges. Use the first one.
				nextEdge = edges.get(0);
				edges.remove(0);
			}
			
			// Concatenate the edges and store each start point. 
			List<Point2D> vertices = new ArrayList<Point2D>();
			while (nextEdge != null) {
				vertices.add(nextEdge.startPoint());
				nextEdge = findNextEdge(nextEdge);
			}

			// Append the end points of the given end edge.
			if (isOpenPath) {
				vertices.add(endEdges.get(1).startPoint());
				vertices.add(endEdges.get(1).endPoint());
			}
			
			return vertices;
		}
		
		// Closes an open path by connecting it to border edges.
		private void closeWithBorder(List<Point2D> vertices) {
			Point2D firstPt = vertices.get(0);
			Point2D lastPt = vertices.get(vertices.size() - 1);
			LineSegment2D startBorder = findBorder(firstPt);
			LineSegment2D endBorder = findBorder(lastPt);
			
			if (startBorder.equals(endBorder)) {
				// Nothing to do. The polygon automatically connects the first and
				// last points in a straight line.
			} else {
				LineIntersection2D.Result isect =
						LineIntersection2D.intersect(startBorder, endBorder);
				if (isect.type == LineIntersection2D.IntersectionType.POINT) {
					// Neighboring border edges. Add their corner point as vertex.
					// However, if one of the path points lies exactly on the
					// corner, then we don't need to add the corner.
					Point2D corner = (Point2D) isect.intersection;
					if (!corner.equals(firstPt) && !corner.equals(lastPt))
						vertices.add(corner);
				} else if (isect.type == LineIntersection2D.IntersectionType.NONE) {
					// The border edges are parallel. We need to connect the path
					// points across three border edges. Find the two border corners
					// that form a convex polygon with the rest of the path.
					int numPts = vertices.size();
					if (areConvex(vertices.get(numPts - 3),
							vertices.get(numPts - 2), lastPt, endBorder.startPoint()))
						vertices.add(endBorder.startPoint());
					else
						vertices.add(endBorder.endPoint());

					if (areConvex(vertices.get(2), vertices.get(1),
							firstPt, startBorder.startPoint()))
						vertices.add(startBorder.startPoint());
					else
						vertices.add(startBorder.endPoint());
				}
			}
		}
		
		// Checks if the given points form a convex path.
		private boolean areConvex(Point2D a, Point2D b, Point2D c, Point2D d) {
			List<Point2D> path = new ArrayList<Point2D>();
			path.add(a);
			path.add(b);
			path.add(c);
			path.add(d);
			return GeometryUtil.isConvexPath(path);
		}
		
		// Finds the edge that connects to a given previous edge.
		private LineSegment2D findNextEdge(LineSegment2D prevEdge) {
			Point2D connector = prevEdge.endPoint();
			
			int pos = findEndpoint(connector, -1); 
			if (pos == -1)
				return null;

			LineSegment2D e = edges.get(pos); 
			edges.remove(pos);
			
			if (connector.equals(e.startPoint()))
				return (LineSegment2D) e.copy();
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
		
		// Returns the border edge that a given point belongs to.
		private LineSegment2D findBorder(Point2D pt) {
			for (LineSegment2D borderEdge : borders) {
				if (borderEdge.isPointOnLine(pt).isOnLine)
					return borderEdge;
			}
			return null;
		}
	}
	
	///////////////
	
	// List of points to generate the Voronoi tesselation for.
	private final List<Point2D> samples;
	// Border around the sample points. Used to terminate Voronoi edges that
	// would extend to infinity.
	private final Rect2D border;
	// Edges of border.
	private final List<LineSegment2D> borderEdges;
	// List of regions generated by the the tesselation.
	private List<VoronoiRegion> regions = new ArrayList<VoronoiRegion>();
	// Triangles of the Delauney triangulation. A by-product of the tesselation
	// that can be useful, e.g. for debugging.
	private List<Triangle2D> triangulation;
	// Error log.
	private List<String> log = new ArrayList<String>();

	
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
		borderEdges = calcBorderEdges(border);
	}

	// Starts the Voronoi tesselation.
	public List<VoronoiRegion> tesselate() {
		// Handle some degenerate cases.
		if (samples.size() == 0) {
			// Return empty region list.
			return regions;
		}
		if (samples.size() == 1) {
			return tesselateWithSingleRegion();
		}
		if (samples.size() == 2) {
			return tesselateWithTwoRegions();
		}
		
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
			try {
				DelauneyEdgeCollection delauneyEdges = collectDelauneyEdges(sample,
						delauneyTriangles);
				List<LineSegment2D> voronoiEdges =
						delauneyEdges.makeVoronoiEdges(borderEdges);
				
				regions.add(new VoronoiRegion(sample,
						makePolygon(voronoiEdges, borderEdges)));
			} catch (GeometryException e) {
				log.add("Skipped region for sample point " + sample.toString() +
						": " + e.getMessage());
			}
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
	
	// Calculates border edges at a given offset around a given list of points.
	private static List<LineSegment2D> calcBorderEdges(Rect2D border) {
		List<LineSegment2D> res = new ArrayList<LineSegment2D>();
		res.add(new LineSegment2D(border.leftTop(), border.rightTop()));
		res.add(new LineSegment2D(border.rightTop(), border.rightBottom()));
		res.add(new LineSegment2D(border.rightBottom(), border.leftBottom()));
		res.add(new LineSegment2D(border.leftBottom(), border.leftTop()));
		return res;
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
		
		List<Polygon2D> regionPolys = PolygonLineIntersection2D.convexPolygonLine(
				makePolygon(border), bisection);
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
	private static Polygon2D makePolygon(List<LineSegment2D> edges,
			List<LineSegment2D> borders) throws GeometryException {
		PolygonBuilder builder = new PolygonBuilder(edges, borders);
		return builder.build();
	}

	// Creates a polygon from a given rectangle.
	private static Polygon2D makePolygon(Rect2D r) {
		Polygon2D poly = new Polygon2D();
		poly.addVertex(r.leftTop());
		poly.addVertex(r.rightTop());
		poly.addVertex(r.rightBottom());
		poly.addVertex(r.leftBottom());
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
