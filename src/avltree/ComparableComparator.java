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

        double d1 = ((Point)p1).distanceTo((Point)startPoint);
        double d2 = ((Point)p2).distanceTo((Point)startPoint);

        double angle1 = Point.polarAngle((Point)startPoint,(Point)p1);
        double angle2 = Point.polarAngle((Point)startPoint,(Point)p2);
        if (angle1>direction) angle1 -= 2*Math.PI;
        if (angle2>direction) angle2 -= 2*Math.PI;

        double epsilon = 0.001;
        if (Math.abs(d1)<epsilon) return -1;
        if (Math.abs(d2)<epsilon) return -1;
        if (angle1<angle2 || angle1 == angle2 && d1<d2) return -1;
        if (angle1 == angle2 && d1==d2) return 0;
        return 1;
    }
}
