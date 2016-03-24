package allnearestneighbours;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 *
 */
public class VoronoiDiagram {

    List<VoronoiPoint> points;
    List<VoronoiPoint> convexHull;

    public VoronoiDiagram(List<VoronoiPoint> points) {
        this.points = points;
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
        System.out.println(left.points.size()+" "+right.points.size());
        merge(left, right);
    }

    private void merge(VoronoiDiagram left, VoronoiDiagram right) {
        if (left.points.size() == 1 && right.points.size() == 1) {
            firstStepOfMerging(left, right);
        } else {
            List<VoronoiEdge> edges = findConvexHull(left, right);
            constructDividingChain(left, right, edges.get(0), edges.get(1));
        }
    }

    private void firstStepOfMerging(VoronoiDiagram left, VoronoiDiagram right) {
        VoronoiPoint begin = left.points.get(0);
        VoronoiPoint end = right.points.get(0);
        convexHull.add(begin);
        convexHull.add(end);
        VoronoiEdge edge = VoronoiEdge.getPerpendicularEdge(begin, end);
        if (edge.leftSide.equals(end))
            edge = edge.reverse;
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
        boolean canLeftZero, cLZ = true, canRightZero, cRZ = true;
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
                cLZ = false;
            } else if (canRightZero) {
                upperEdge = new VoronoiEdge(upperEdge.beginVertex, right.convexHull.get(0), null, null);
                cRZ = false;
            }
        }
        return upperEdge;
    }

    private VoronoiEdge getLowerEdge(VoronoiEdge lowerEdge, VoronoiDiagram left, VoronoiDiagram right, int i) {
        int j = right.convexHull.size() - 1;
        boolean canLeft, canRight;
        boolean canLeftZero, cLZ = true, canRightZero, cRZ = true;
        while ((canLeft = ((i < left.convexHull.size()) && !lowerEdge.isLowerThan(new VoronoiEdge(left.convexHull.get(i), lowerEdge.endVertex, null, null)))) |
                (canRight = ((j >= 0) && !lowerEdge.isLowerThan(new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(j), null, null)))) |
                (canLeftZero = (cLZ && i >= left.convexHull.size() && !lowerEdge.isLowerThan(new VoronoiEdge(left.convexHull.get(0), lowerEdge.endVertex, null, null)))) |
                (canRightZero = (cRZ && j < 0 && !lowerEdge.isLowerThan(new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(0), null, null))))) {
            if (canLeft) {
                lowerEdge = new VoronoiEdge(left.convexHull.get(i), lowerEdge.endVertex, null, null);
                i++;
            } else if (canRight) {
                lowerEdge = new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(j), null, null);
                j--;
            } else if (canLeftZero) {
                lowerEdge = new VoronoiEdge(left.convexHull.get(0), lowerEdge.endVertex, null, null);
                cLZ = false;
            } else if (canRightZero) {
                lowerEdge = new VoronoiEdge(lowerEdge.beginVertex, right.convexHull.get(0), null, null);
                cRZ = false;
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

    private void constructDividingChain(VoronoiDiagram left, VoronoiDiagram right, VoronoiEdge upperEdge, VoronoiEdge lowerEdge) {
        Line currentLine = new Line(upperEdge.beginVertex, upperEdge.endVertex, true);

        VoronoiEdge currentLeftEdge = getFirstEdge(left, upperEdge.beginVertex, currentLine);
        VoronoiEdge currentRightEdge = getFirstEdge(right, upperEdge.endVertex, currentLine);
        VoronoiEdge previousLeftEdge = null, previousRightEdge = null;
        VoronoiPoint currentLeftPoint, currentRightPoint;
        if (currentLeftEdge != null)
            currentLeftPoint = currentLeftEdge.leftSide;
        else currentLeftPoint = left.points.get(0);
        if (currentRightEdge != null)
            currentRightPoint = currentRightEdge.leftSide;
        else currentRightPoint = right.points.get(0);

        double currentY = PointsPanel.MAX;

        do {
            double leftY = -PointsPanel.MAX, rightY = -PointsPanel.MAX;
            if (currentLeftEdge != null)
                leftY = currentLine.intersection(new Line(currentLeftEdge.beginVertex, currentLeftEdge.endVertex, false)).y;
            if (currentRightEdge != null)
                rightY = currentLine.intersection(new Line(currentRightEdge.beginVertex, currentRightEdge.endVertex, false)).y;

            if (currentLeftEdge != null && leftY >= rightY && currentY>=leftY) {
                VoronoiEdge edge = new VoronoiEdge(new Point(currentLine.findX(currentY), currentY),
                        new Point(currentLine.findX(leftY), leftY), currentRightPoint, currentLeftPoint);

                currentLeftEdge.clockwise = edge;
                edge.anticlockwise = currentLeftEdge;
                if (currentLeftEdge.reverse.anticlockwise == null)
                    currentLeftEdge.reverse.anticlockwise = edge.reverse;
                if (edge.clockwise == null)
                    edge.clockwise = currentLeftEdge.reverse;

                currentLeftEdge.endVertex = edge.endVertex;
                currentLeftEdge.reverse.beginVertex = edge.endVertex;
                if (previousLeftEdge != null) {
                    previousLeftEdge.anticlockwise = edge;
                }

                currentY = leftY;
                currentLeftEdge = currentLeftEdge.reverse;
                previousLeftEdge = currentLeftEdge;

                currentLeftPoint = currentLeftEdge.leftSide;
                currentLine = new Line(currentLeftPoint, currentRightPoint, true);
                if (currentLeftPoint.equals(lowerEdge.beginVertex) && currentRightPoint.equals(lowerEdge.endVertex))
                    addLastEdge(currentLeftPoint, currentRightPoint, currentLine, currentY, currentLeftEdge, edge.reverse);
                else {
                    //if (currentLeftEdge.clockwise != null)
                        currentLeftEdge = currentLeftEdge.clockwise;
                    while (currentLeftEdge != null && !currentLine.intersects(currentLeftEdge)) {
                        currentLeftEdge = currentLeftEdge.clockwise;
                    }
                }
            } else if (currentRightEdge != null) {
                VoronoiEdge edge = new VoronoiEdge(new Point(currentLine.findX(currentY), currentY),
                        new Point(currentLine.findX(rightY), rightY), currentRightPoint, currentLeftPoint);

                currentRightEdge.anticlockwise = edge;
                edge.clockwise = currentRightEdge;
                if (currentRightEdge.reverse.clockwise == null)
                    currentRightEdge.reverse.clockwise = edge.reverse;
                if (edge.reverse.anticlockwise == null)
                    edge.reverse.anticlockwise = currentRightEdge.reverse;

                currentRightEdge.beginVertex = edge.endVertex;
                currentRightEdge.reverse.endVertex = edge.endVertex;
                if (previousRightEdge == null)
                    currentRightEdge.leftSide.firstEdge = edge;
                else previousRightEdge.clockwise = edge;

                currentY = rightY;
                currentRightEdge = currentRightEdge.reverse;
                previousRightEdge = currentRightEdge;

                currentRightPoint = currentRightEdge.leftSide;
                currentLine = new Line(currentLeftPoint, currentRightPoint, true);
                if (currentLeftPoint.equals(lowerEdge.beginVertex) && currentRightPoint.equals(lowerEdge.endVertex))
                    addLastEdge(currentLeftPoint, currentRightPoint, currentLine, currentY, edge.reverse, currentRightEdge);
                else {
                    //if (currentRightEdge.anticlockwise != null)
                        currentRightEdge = currentRightEdge.anticlockwise;
                    while (currentRightEdge != null && !currentLine.intersects(currentRightEdge)) {
                        currentRightEdge = currentRightEdge.anticlockwise;
                    }
                }
            }
        }
        while (!currentLeftPoint.equals(lowerEdge.beginVertex) || !currentRightPoint.equals(lowerEdge.endVertex)  ||
                currentLeftEdge != null && currentRightEdge != null);

    }

    private VoronoiEdge getFirstEdge(VoronoiDiagram diagram, Point point, Line line) {
        VoronoiPoint currentPoint;
        int i = 0;
        while (!diagram.points.get(i).equals(point)) i++;
        currentPoint = diagram.points.get(i);

        VoronoiEdge edge = currentPoint.firstEdge;
        while (edge != null && !line.intersects(edge)) {
            edge = edge.clockwise;
        }
        return edge;
    }

    private static void addLastEdge(VoronoiPoint left, VoronoiPoint right, Line currentLine, double currentY,
                                    VoronoiEdge leftEdge, VoronoiEdge rightEdge) {
        double minY = -1000000;
        VoronoiEdge edge;
        Point beginVertex = new Point(currentLine.findX(currentY), currentY);
        Point endVertex = new Point(currentLine.findX(minY), minY);
        edge = new VoronoiEdge(beginVertex, endVertex, right, left);

        edge.reverse.clockwise = leftEdge;
        leftEdge.anticlockwise = edge.reverse;
        edge.anticlockwise = rightEdge;
        rightEdge.clockwise = edge;

        left.firstEdge = edge.reverse;
    }
}
