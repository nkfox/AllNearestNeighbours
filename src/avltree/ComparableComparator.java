package avltree;

import allnearestneighbours.Point;
import allnearestneighbours.VoronoiPoint;

import java.util.Comparator;

/**
 * Created by Vladislav Kichatiy on 25.04.2016.
 * Comparator for sorting points in convex hull
 */
public class ComparableComparator<VoronoiPoint> implements Comparator<VoronoiPoint> {
    VoronoiPoint startPoint;
    double direction;

    public ComparableComparator() {}

    public ComparableComparator(VoronoiPoint point, VoronoiPoint dir) {
        startPoint = point;
        if (dir == null)
            direction = 0;
        else direction = Point.polarAngle((Point)startPoint,(Point)dir);
    }

    @Override
    public int compare(VoronoiPoint p1, VoronoiPoint p2) {
        if (p1 == null || p2 == null || startPoint == null) {
            throw new NullPointerException();
        }

        if (p1.equals(p2)) {
            return 0;
        }

        if (p1.equals(startPoint)) {
            return -1;
        }

        if (p2.equals(startPoint)) {
            return 1;
        }

        double angle1 = Point.polarAngle((Point)startPoint,(Point)p1);
        double angle2 = Point.polarAngle((Point)startPoint,(Point)p2);

        if (angle1 > direction) angle1 -= 2*Math.PI;
        if (angle2 > direction) angle2 -= 2*Math.PI;

        if (angle1 == angle2) {
            double d1 = ((Point)p1).distanceTo((Point)startPoint);
            double d2 = ((Point)p2).distanceTo((Point)startPoint);

            return Double.compare(d1, d2);
        }

        return Double.compare(angle1, angle2);
    }
}
