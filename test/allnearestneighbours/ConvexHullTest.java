package allnearestneighbours;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConvexHullTest {
    @Test
    public void mergeSimpleTest() {
        ConvexHull a = new ConvexHull(new VoronoiPoint(0, 0));
        ConvexHull b = new ConvexHull(new VoronoiPoint(1, 1));

        ConvexHull c = ConvexHull.merge(a, b);
        assertTrue(c.contains(new VoronoiPoint(0, 0)));
        assertTrue(c.contains(new VoronoiPoint(1, 1)));
        assertEquals(c.size(), 2);
    }

    @Test
    public void mergeHorizontalTest() {
        ArrayList<VoronoiPoint> points = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            points.add(new VoronoiPoint(i, 0));
        }

        ArrayList<ConvexHull> hulls = new ArrayList<>();
        for (VoronoiPoint p : points) {
            hulls.add(new ConvexHull(p));
        }

        for (int step = 1; step < 8; step *= 2) {
            for (int i = 0; i < 8; i = i + step * 2) {
                hulls.set(i, ConvexHull.merge(hulls.get(i), hulls.get(i + step)));
            }
        }

        ConvexHull res = hulls.get(0);
        assertTrue(res.contains(points.get(0)));
        assertTrue(res.contains(points.get(7)));
        assertEquals(res.size(), 2);
    }

    @Test
    public void mergeSquareTest() {
        int side = 16;
        int size = side * side;

        ArrayList<VoronoiPoint> points = new ArrayList<>();
        for (int i = 0; i < side; ++i) {
            for (int j = 0; j < side; ++j) {
                points.add(new VoronoiPoint(i, j));
            }
        }

        ArrayList<ConvexHull> hulls = new ArrayList<>();
        for (VoronoiPoint p : points) {
            hulls.add(new ConvexHull(p));
        }

        for (int step = 1; step < size; step *= 2) {
            for (int i = 0; i < size; i = i + step * 2) {
                hulls.set(i, ConvexHull.merge(hulls.get(i), hulls.get(i + step)));
            }
        }

        ConvexHull res = hulls.get(0);
        assertTrue(res.contains(new VoronoiPoint(0, 0)));
        assertTrue(res.contains(new VoronoiPoint(0, side - 1)));
        assertTrue(res.contains(new VoronoiPoint(side - 1, 0)));
        assertTrue(res.contains(new VoronoiPoint(side - 1, side - 1)));
        assertEquals(res.size(), 4);
    }

    @Test
    public void mergeVerticalTest() {
        ArrayList<VoronoiPoint> points = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            points.add(new VoronoiPoint(0, i));
        }

        ArrayList<ConvexHull> hulls = new ArrayList<>();
        for (VoronoiPoint p : points) {
            hulls.add(new ConvexHull(p));
        }

        for (int step = 1; step < 8; step *= 2) {
            for (int i = 0; i < 8; i = i + step * 2) {
                hulls.set(i, ConvexHull.merge(hulls.get(i), hulls.get(i + step)));
            }
        }

        ConvexHull res = hulls.get(0);
        assertTrue(res.contains(points.get(0)));
        assertTrue(res.contains(points.get(7)));
        assertEquals(2, res.size());
    }
}