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
        super(point.x, point.y, point.color);
    }

    public int getSituationOneSidePoints(VoronoiPoint center, VoronoiPoint p1, VoronoiPoint p2) {
        Line line;
        line = new Line(x, y, center.x, center.y);
        boolean s1 = line.isLower(p1);
        boolean s2 = line.isLower(p2);

        if (s1 && s2) {
            if (x < center.x) return 2;
            else return 1;
        }
        if (!s1 && !s2) {
            if (x < center.x) return 1;
            else return 2;
        }
        return 3;
    }

    public int getSituation(VoronoiPoint center, VoronoiPoint p1, VoronoiPoint p2) {
        int oneSide = getSituationOneSidePoints(center, p1, p2);
        if (oneSide == 1 || oneSide == 2)
            return oneSide;

        int lessPi = p1.getSituationOneSidePoints(p2, this, center);
        if (lessPi == 3) return 4;
        return 3;
    }
}
