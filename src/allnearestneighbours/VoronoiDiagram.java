package allnearestneighbours;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 */
public class VoronoiDiagram {

    List<VoronoiPoint> points;
    List<VoronoiEdge> edges;
    List<VoronoiPoint> convexHull;

    public VoronoiDiagram(List<VoronoiPoint> points) {
        this.points = points;
        this.edges = new ArrayList<>();
        this.convexHull = new ArrayList<>();
        buildVoronoiDiagram();
    }

    private void buildVoronoiDiagram() {
        if (points.size() == 1) {
            convexHull.add(points.get(0));
            return;
        }
        VoronoiDiagram left = new VoronoiDiagram(points.subList(0, points.size() / 2));
        VoronoiDiagram right = new VoronoiDiagram(points.subList(points.size() / 2, points.size()));
        merge(left, right);
    }

    private void merge(VoronoiDiagram left, VoronoiDiagram right) {
        if (left.points.size() == 1 && right.points.size() == 1) {
            firstStepOfMerging(left, right);
        } else {
            List<VoronoiEdge> edges = findConvexHull(left, right);
            constructDividingChain(left,right,edges.get(0),edges.get(1));
        }
    }

    private void firstStepOfMerging(VoronoiDiagram left, VoronoiDiagram right) {
        VoronoiPoint begin = left.points.get(0);
        VoronoiPoint end = right.points.get(0);
        convexHull.add(begin);
        convexHull.add(end);
        VoronoiEdge edge = VoronoiEdge.getPerpendicularEdge(begin, end);
        edges.add(edge);
        begin.firstEdge = edge;
        end.firstEdge = edge.reverse;
    }

    private List<VoronoiEdge> findConvexHull(VoronoiDiagram left, VoronoiDiagram right) {
        int i = 0;
        while (i < left.convexHull.size() - 1 && left.convexHull.get(i).x < left.convexHull.get(i + 1).x) i++;
        VoronoiEdge mainEdge = new VoronoiEdge(left.convexHull.get(i), right.convexHull.get(0), null, null);
        VoronoiEdge upperEdge = getUpperEdge(mainEdge, left, right, i - 1);
        VoronoiEdge lowerEdge = getLowerEdge(mainEdge, left, right, i + 1);
        combineConvexHulls(left, right, upperEdge, lowerEdge);

        List<VoronoiEdge> edges = new ArrayList<>();
        edges.add(upperEdge);
        edges.add(lowerEdge);
        return edges;
    }

    private VoronoiEdge getUpperEdge(VoronoiEdge upperEdge, VoronoiDiagram left, VoronoiDiagram right, int i) {
        int j = 1;
        boolean canLeft, canRight;
        boolean canLeftZero,cLZ=true, canRightZero,cRZ=true;
        while ((canLeft = (i >= 0 && upperEdge.isLowerThan(new VoronoiEdge(left.convexHull.get(i), upperEdge.endVertex, null, null)))) |
                (canRight = (j < right.convexHull.size() && upperEdge.isLowerThan(new VoronoiEdge(upperEdge.beginVertex, right.convexHull.get(j), null, null)))) |
                (canLeftZero = (cLZ && i < 0 && upperEdge.isLowerThan(new VoronoiEdge(left.convexHull.get(0), upperEdge.endVertex, null, null)))) |
                (canRightZero = (cRZ && j >= right.convexHull.size() && upperEdge.isLowerThan(new VoronoiEdge(upperEdge.beginVertex, right.convexHull.get(0), null, null))))) {
            if (canLeft) {
                upperEdge = new VoronoiEdge(left.convexHull.get(i), upperEdge.endVertex, null, null);
                i--;
            } else if (canRight) {
                upperEdge = new VoronoiEdge(upperEdge.beginVertex, right.convexHull.get(j), null, null);
                j++;
            } else if (canLeftZero) {
                upperEdge = new VoronoiEdge(left.convexHull.get(0), upperEdge.endVertex, null, null);
                cLZ=false;
            } else if (canRightZero) {
                upperEdge = new VoronoiEdge(upperEdge.beginVertex, right.convexHull.get(0), null, null);
                cRZ=false;
            }
        }
        return upperEdge;
    }

    private VoronoiEdge getLowerEdge(VoronoiEdge lowerEdge, VoronoiDiagram left, VoronoiDiagram right, int i) {
        int j = right.convexHull.size() - 1;
        boolean canLeft, canRight;
        boolean canLeftZero,cLZ=true, canRightZero,cRZ=true;
        while ((canLeft = ((i < left.convexHull.size()) && !lowerEdge.isLowerThan(new VoronoiEdge(left.convexHull.get(i), lowerEdge.endVertex, null, null)))) |
                (canRight = ((j >= 0) && !lowerEdge.isLowerThan(new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(j), null, null)))) |
                (canLeftZero = (cLZ && i >= left.convexHull.size() && !lowerEdge.isLowerThan(new VoronoiEdge(left.convexHull.get(0), lowerEdge.endVertex, null, null)))) |
                (canRightZero = (cRZ &&j < 0 && !lowerEdge.isLowerThan(new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(0), null, null))))) {
            if (canLeft) {
                lowerEdge = new VoronoiEdge(left.convexHull.get(i), lowerEdge.endVertex, null, null);
                i++;
            } else if (canRight) {
                lowerEdge = new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(j), null, null);
                j--;
            } else if (canLeftZero) {
                lowerEdge = new VoronoiEdge(left.convexHull.get(0), lowerEdge.endVertex, null, null);
                cLZ=false;
            } else if (canRightZero) {
                lowerEdge = new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(0), null, null);
                cRZ=false;
            }
        }
        return lowerEdge;
    }

    private void combineConvexHulls(VoronoiDiagram left, VoronoiDiagram right, VoronoiEdge upperEdge, VoronoiEdge lowerEdge) {
        int i = 0;
        while (!left.convexHull.get(i).equals(upperEdge.beginVertex))
            convexHull.add(left.convexHull.get(i++));
        convexHull.add(left.convexHull.get(i++));

        int j = 0;
        while (!right.convexHull.get(j).equals(upperEdge.endVertex)) j++;
        while (!right.convexHull.get(j % right.convexHull.size()).equals(lowerEdge.endVertex)) {
            convexHull.add(right.convexHull.get(j++ % right.convexHull.size()));
            if (j >= right.convexHull.size()) break;
        }
        convexHull.add(right.convexHull.get(j % right.convexHull.size()));

        while (i < left.convexHull.size() && !left.convexHull.get(i).equals(lowerEdge.beginVertex)) i++;
        while (i < left.convexHull.size())
            convexHull.add(left.convexHull.get(i++));

    }

    private void constructDividingChain(VoronoiDiagram left, VoronoiDiagram right, VoronoiEdge upperEdge, VoronoiEdge lowerEdge){

    }

    public List<VoronoiPoint> getConvexHull() {
        return convexHull;
    }
}
