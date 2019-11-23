package geometry;


public class VoronoiRegion {
	public final Point2D seed;
	private final Polygon2D border;
	
	public VoronoiRegion(Point2D seed, Polygon2D border) {
		this.seed = seed;
		this.border = border;
	}
	
	public int countVertices() {
		if (border == null)
			return 0;
		return border.countVertices();
	}
	
	public boolean hasVertex(Point2D pt) {
		if (border == null)
			return false;
		return border.hasVertex(pt);
	}
	
	public Polygon2D border() {
		return border;
	}
}
