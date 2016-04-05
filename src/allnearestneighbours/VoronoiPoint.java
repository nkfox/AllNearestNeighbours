package allnearestneighbours;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * Voronoi point is in voronoi cell, so it has firs edge of cell
 */
public class VoronoiPoint extends Point {

    VoronoiEdge firstEdge;

    public VoronoiPoint(double x, double y) {
        super(x, y);
    }

    public VoronoiPoint(Point point) {
        super(point.x, point.y);
    }
}
