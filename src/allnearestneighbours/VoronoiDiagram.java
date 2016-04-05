package allnearestneighbours;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * Diagram is build by divide and conquer
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
        //System.out.println(left.points.size() + " " + right.points.size());
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
        while (i < left.convexHull.size() - 1 && (left.convexHull.get(i).x < left.convexHull.get(i + 1).x ||
                Math.abs(left.convexHull.get(i).x - left.convexHull.get(i + 1).x) < 0.00001 &&
                        left.convexHull.get(i).y > left.convexHull.get(i + 1).y)) i++;
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

        VoronoiEdge currentLeftEdge = getFirstEdge(left, upperEdge.beginVertex, currentLine, true);
        VoronoiEdge currentRightEdge = getFirstEdge(right, upperEdge.endVertex, currentLine, false);
        VoronoiEdge previousLeftEdge = null, previousRightEdge = null;
        Point leftIntersection = null, rightIntersection = null;
        VoronoiPoint currentLeftPoint, currentRightPoint;
        if (currentLeftEdge != null)
            currentLeftPoint = currentLeftEdge.leftSide;
        else currentLeftPoint = left.points.get(0);
        if (currentRightEdge != null)
            currentRightPoint = currentRightEdge.leftSide;
        else currentRightPoint = right.points.get(0);
        VoronoiEdge last = null;

        double currentY = PointsPanel.MAX;

        int i=6;
        do {
            if (left.points.size() == 5 && right.points.size() == 5 && i--==0) break;
            double leftY = getY(currentLeftEdge, currentLine);
            double rightY = getY(currentRightEdge, currentLine);

            if (leftY == -PointsPanel.MAX && rightY == -PointsPanel.MAX) {
                // а что если два ребра у вершины параллельны??

                if (upperEdge.beginVertex.distanceTo(upperEdge.endVertex) < upperEdge.beginVertex.distanceTo(right.points.get(0)))
                    currentLine = new Line(upperEdge.beginVertex, upperEdge.endVertex, true);
                else currentLine = new Line(upperEdge.beginVertex, right.points.get(0), true);

                Point begin = new Point(-PointsPanel.MAX, currentLine.findY(-PointsPanel.MAX));
                Point end = new Point(PointsPanel.MAX, currentLine.findY(PointsPanel.MAX));
                VoronoiEdge edge = new VoronoiEdge(begin, end, currentRightPoint, currentLeftPoint);
                currentLeftPoint.firstEdge = edge;
                edge.reverse.clockwise = currentRightPoint.firstEdge;
                currentRightPoint.firstEdge = edge.reverse;

                break; // change!!
            }
            if (currentLeftEdge != null && (leftY >= rightY || rightY >= currentY) && currentY + 0.00001 >= leftY) {
                VoronoiEdge edge = buildNewEdge(currentLine,currentY,leftY,currentRightPoint, currentLeftPoint,currentLeftEdge,last);
                //System.out.println(edge.beginVertex.x + " " + edge.beginVertex.y + "              " + edge.endVertex.x + " " + edge.endVertex.y);

                VoronoiEdge rightAnticlockwise = currentRightEdge != null ? currentRightEdge.anticlockwise : null;
                VoronoiEdge leftClockwise = currentLeftEdge.clockwise;
                Point previousEnd = currentLeftEdge.endVertex;

                fillLeftInfo(currentLeftEdge, currentRightEdge, edge, previousLeftEdge, previousRightEdge);

                currentY = leftY;
                if (!edge.endVertex.equals(previousEnd))
                    currentLeftEdge = currentLeftEdge.reverse;
                else currentLeftEdge = leftClockwise.reverse.clockwise;
                previousLeftEdge = currentLeftEdge;
                previousRightEdge = edge;
                leftIntersection = edge.endVertex;
                last = edge;

                currentLeftPoint = currentLeftEdge.leftSide;
                if (Math.abs(leftY - rightY) < 0.00001) {
                    if (!edge.endVertex.equals(currentRightEdge.beginVertex)) {
                        currentRightEdge.beginVertex = edge.endVertex;
                        currentRightEdge.reverse.endVertex = edge.endVertex;
                        currentRightEdge = currentRightEdge.reverse;
                    }
                    else currentRightEdge = rightAnticlockwise.reverse.anticlockwise;
                    previousRightEdge = currentRightEdge;
                    rightIntersection = edge.endVertex;
                    currentRightPoint = currentRightEdge.leftSide;
                }
                currentLine = new Line(currentLeftPoint, currentRightPoint, true);
                if (currentLeftPoint.equals(lowerEdge.beginVertex) && currentRightPoint.equals(lowerEdge.endVertex))
                    addLastEdge(currentLeftPoint, currentRightPoint, currentLine, currentY, previousLeftEdge, previousRightEdge);
                else {
                    currentLeftEdge = findNextEdge(currentLeftEdge, currentLine, false, edge.reverse, leftIntersection, rightIntersection,true);
                    currentRightEdge = findNextEdge(currentRightEdge, currentLine, true, edge, leftIntersection, rightIntersection,false);
                }
                //break;
            } else if (currentRightEdge != null) {
                VoronoiEdge edge = buildNewEdge(currentLine,currentY,rightY,currentRightPoint, currentLeftPoint,currentRightEdge,last);
                //System.out.println(edge.beginVertex.x + " " + edge.beginVertex.y + "              " + edge.endVertex.x + " " + edge.endVertex.y);

                VoronoiEdge rightAnticlockwise = currentRightEdge.anticlockwise;
                VoronoiEdge leftClockwise = currentLeftEdge != null ? currentLeftEdge.clockwise : null;
                Point previousBegin = currentRightEdge.beginVertex;

                fillRightInfo(currentLeftEdge, currentRightEdge, edge, previousLeftEdge, previousRightEdge);

                currentY = rightY;
                if (!edge.endVertex.equals(previousBegin))
                    currentRightEdge = currentRightEdge.reverse;
                else currentRightEdge = rightAnticlockwise.reverse.anticlockwise;
                previousLeftEdge = edge.reverse;
                previousRightEdge = currentRightEdge;
                rightIntersection = edge.endVertex;
                last = edge;

                currentRightPoint = currentRightEdge.leftSide;
                if (Math.abs(leftY - rightY) < 0.00001) {
                    if (!edge.endVertex.equals(currentLeftEdge.endVertex)) {
                        currentLeftEdge.beginVertex = edge.endVertex;
                        currentLeftEdge.reverse.endVertex = edge.endVertex;
                        currentLeftEdge = currentLeftEdge.reverse;
                    }
                    else currentLeftEdge = leftClockwise.reverse.clockwise;
                    previousLeftEdge = currentLeftEdge;
                    leftIntersection = edge.endVertex;
                    currentLeftPoint = currentLeftEdge.leftSide;
                }
                currentLine = new Line(currentLeftPoint, currentRightPoint, true);
                if (currentLeftPoint.equals(lowerEdge.beginVertex) && currentRightPoint.equals(lowerEdge.endVertex))
                    addLastEdge(currentLeftPoint, currentRightPoint, currentLine, currentY, previousLeftEdge, previousRightEdge);
                else {
                    currentLeftEdge = findNextEdge(currentLeftEdge, currentLine, true, edge.reverse, leftIntersection, rightIntersection,true);
                    currentRightEdge = findNextEdge(currentRightEdge, currentLine, false, edge, leftIntersection, rightIntersection,false);
                }
                //break;
            } else {
                System.out.println("Error");
                break;
            }
        }
        while (!currentLeftPoint.equals(lowerEdge.beginVertex) || !currentRightPoint.equals(lowerEdge.endVertex));
    }

    private static VoronoiEdge buildNewEdge(Line currentLine,double beginY, double endY, VoronoiPoint currentRightPoint,
                                            VoronoiPoint currentLeftPoint, VoronoiEdge intersected,VoronoiEdge last){
        if (Math.abs(currentLine.d)>0.00001)
            return new VoronoiEdge(new Point(currentLine.findX(beginY), beginY),
                new Point(currentLine.findX(endY), endY), currentRightPoint, currentLeftPoint);
        return new VoronoiEdge(new Point(last.endVertex.x, beginY), new Point(currentLine.intersection(
                new Line(intersected.beginVertex, intersected.endVertex,false)).x, endY), currentRightPoint, currentLeftPoint);
    }

    private VoronoiEdge getFirstEdge(VoronoiDiagram diagram, Point point, Line line, boolean endVertexIncluded) {
        VoronoiPoint currentPoint;
        int i = 0;
        while (!diagram.points.get(i).equals(point)) i++;
        currentPoint = diagram.points.get(i);

        VoronoiEdge edge = currentPoint.firstEdge;
        while (edge != null && !line.intersects(edge, endVertexIncluded)) {
            edge = edge.clockwise;
        }
        return edge;
    }

    private double getY(VoronoiEdge edge, Line currentLine) {
        double y = -PointsPanel.MAX;
        Point point;
        if (edge != null && (point = currentLine.intersection(new Line(edge.beginVertex, edge.endVertex, false))) != null)
            y = point.y;
        return y;
    }

    private void fillLeftInfo(VoronoiEdge currentLeftEdge, VoronoiEdge currentRightEdge, VoronoiEdge edge, VoronoiEdge previousLeftEdge, VoronoiEdge previousRightEdge) {
        currentLeftEdge.clockwise = edge.reverse;
        edge.reverse.anticlockwise = currentLeftEdge;

        currentLeftEdge.endVertex = edge.endVertex;
        currentLeftEdge.reverse.beginVertex = edge.endVertex;

        if (currentRightEdge != null) {
            currentRightEdge.anticlockwise = edge;
            edge.clockwise = currentRightEdge;
        }

        if (previousLeftEdge != null) {
            edge.reverse.clockwise = previousLeftEdge;
            previousLeftEdge.anticlockwise = edge.reverse;
            edge.anticlockwise = previousRightEdge;
            previousRightEdge.clockwise = edge;
        }

        edge.leftSide.firstEdge = edge;
        findFirstEdge(edge.leftSide);
        edge.rightSide.firstEdge = edge.reverse;
        findFirstEdge(edge.rightSide);
    }

    private void fillRightInfo(VoronoiEdge currentLeftEdge, VoronoiEdge currentRightEdge, VoronoiEdge edge, VoronoiEdge previousLeftEdge, VoronoiEdge previousRightEdge) {
        currentRightEdge.anticlockwise = edge;
        edge.clockwise = currentRightEdge;

        currentRightEdge.beginVertex = edge.endVertex;
        currentRightEdge.reverse.endVertex = edge.endVertex;

        if (currentLeftEdge != null) {
            currentLeftEdge.clockwise = edge.reverse;
            edge.reverse.anticlockwise = currentLeftEdge;
        }

        if (previousRightEdge != null) {
            previousLeftEdge.anticlockwise = edge.reverse;
            edge.reverse.clockwise = previousLeftEdge;
            previousRightEdge.clockwise = edge;
            edge.anticlockwise = previousRightEdge;
        }

        edge.leftSide.firstEdge = edge;
        findFirstEdge(edge.leftSide);
        edge.rightSide.firstEdge = edge.reverse;
        findFirstEdge(edge.rightSide);
    }

    private void findFirstEdge(VoronoiPoint point) {
        VoronoiEdge edge = point.firstEdge;
        int t = 100;
        while (edge.anticlockwise != null) {
            edge = edge.anticlockwise;
            if (edge.equals(point.firstEdge)) return;
            if (t-- < 0) {
                System.out.println("Error in findFirstEdge");
                return;
            }
        }
        point.firstEdge = edge;
    }

    private static void addLastEdge(VoronoiPoint left, VoronoiPoint right, Line currentLine, double currentY,
                                    VoronoiEdge leftEdge, VoronoiEdge rightEdge) {
        double minY = -PointsPanel.MAX;
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

    private static VoronoiEdge findNextEdge(VoronoiEdge edge, Line currentLine, boolean included, VoronoiEdge lastIncluded,
                                            Point lastLeftIntersection, Point lastRightIntersection,boolean includedEnd) {
        VoronoiEdge currentEdge;
        if (edge == null) {
            currentEdge = lastIncluded.leftSide.firstEdge;
            int i = 200;
            while (i-- > 0 && currentEdge != null && !currentLine.intersects(currentEdge, includedEnd)) {
                currentEdge = currentEdge.clockwise;
            }
            if (i == 0) System.out.println("Error in findNextEdge");
            if (currentEdge == null) return null;
            Line edgeLine = new Line(currentEdge.beginVertex, currentEdge.endVertex, false);
            Point point = currentLine.intersection(edgeLine);
            if ((lastLeftIntersection == null || point != null && point.distanceTo(lastLeftIntersection) > 0.001) &&
                    (lastRightIntersection == null || point != null && point.distanceTo(lastRightIntersection) > 0.001))
                return currentEdge;
            currentEdge = currentEdge.clockwise;
            i = 200;
            while (i-- > 0 && currentEdge != null && !currentLine.intersects(currentEdge, includedEnd)) {
                currentEdge = currentEdge.clockwise;
            }
            if (i == 0) System.out.println("Error in findNextEdge");
        } else {
            if (included && currentLine.intersects(edge, includedEnd)) return edge;
            currentEdge = edge.clockwise;
            if (currentEdge != null && /*(currentEdge.equals(lastIncluded) || currentEdge.equals(lastIncluded.reverse))*/
                    isBadEdge(currentEdge, currentLine, lastIncluded, lastLeftIntersection, lastRightIntersection,includedEnd))
                currentEdge = null;
            else {
                int i = 200;
                while (i-- > 0 && currentEdge != null && !currentLine.intersects(currentEdge, includedEnd)) {
                    currentEdge = currentEdge.clockwise;
                }
                if (i == 0) System.out.println("Error in findNextEdge");
            }
            if (currentEdge == null) {
                currentEdge = edge.anticlockwise;
                if (currentEdge != null && /*(currentEdge.equals(lastIncluded) || currentEdge.equals(lastIncluded.reverse))*/
                        isBadEdge(currentEdge, currentLine, lastIncluded, lastLeftIntersection, lastRightIntersection,includedEnd))
                    currentEdge = null;
                else {
                    int i = 200;
                    while (i-- > 0 && currentEdge != null && !currentLine.intersects(currentEdge, includedEnd)) {
                        currentEdge = currentEdge.anticlockwise;
                    }
                    if (i == 0) System.out.println("Error in findNextEdge");
                }
            }
        }
        return currentEdge;
    }

    private static boolean isBadEdge(VoronoiEdge currentEdge, Line currentLine,VoronoiEdge lastIncluded,
                               Point lastLeftIntersection, Point lastRightIntersection,boolean includedEnd){
        Point intersection = currentLine.intersection(new Line(currentEdge.beginVertex, currentEdge.endVertex,false));
        return currentEdge.equals(lastIncluded) || currentEdge.equals(lastIncluded.reverse) ||
                intersection!=null && (intersection.equals(lastLeftIntersection) || intersection.equals(lastRightIntersection));

    }

    public void draw(Graphics page) {
        int k = 0;
        for (VoronoiPoint point : points) {
            VoronoiEdge current = point.firstEdge;
            int i = 2000;
            if (current != null)
                do {
                    //System.out.println(current.beginVertex.x + " " + current.beginVertex.y + " " + current.endVertex.x + " " + current.endVertex.y);
                    /*if (current.beginVertex.x<0 && current.endVertex.x > 30 || current.endVertex.x<0 && current.beginVertex.x > 30 ||
                            current.beginVertex.x>PointsPanel.WIDTH && current.endVertex.x <PointsPanel.WIDTH-30
                            || current.endVertex.x>PointsPanel.WIDTH && current.beginVertex.x <PointsPanel.WIDTH-30) k++;
                    else*/
                    page.drawLine((int) current.beginVertex.x, PointsPanel.HEIGHT - (int) current.beginVertex.y,
                            (int) current.endVertex.x, PointsPanel.HEIGHT - (int) current.endVertex.y);
                    current = current.clockwise;
                } while (current != null && !current.equals(point.firstEdge) && i-- > 0);
            //System.out.println("\n");
        }
        System.out.println(k);
    }

    public void update(Graphics page) {
        draw(page);
    }
}
