package allnearestneighbours;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * All work with lines
 */
public class Line {

    //(x-a)/b = (y-c)/d
    double a;
    double b;
    double c;
    double d;

    public Line(double x1, double y1, double x2, double y2) {
        a = x1;
        b = x2 - x1;
        c = y1;
        d = y2 - y1;
    }

    public Line(Point p1, Point p2, boolean isPerpendicular) {
        this(p1.x, p1.y, p2.x, p2.y);
        if (isPerpendicular) {
            Point middle = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            double temp = b;
            b = d;
            d = -temp;
            a = middle.x;
            c = middle.y;
        }
    }

    public Point intersection(Line line) {
        double eps = 0.0000001;
        if (Math.abs(b * line.d - d * line.b) < eps)
            return null;
        if (Math.abs(b) < eps)
            return new Point(a, line.findY(a));
        if (Math.abs(line.b) < eps)
            return new Point(line.a, findY(line.a));
        double x = (a * d / b - c - line.d * line.a / line.b + line.c) / (d / b - line.d / line.b);
        double y = findY(x);
        return new Point(x, y);
    }

    public double findY(double x) {
        return (x - a) * d / b + c;
    }

    public double findX(double y) {
        return (y - c) * b / d + a;
    }

    public boolean intersects(VoronoiEdge edge, boolean endVertexIncluded) {
        double yLeft = findY(edge.beginVertex.x);
        double yRight = findY(edge.endVertex.x);
        double epsilon = 0.00001;
        if (!endVertexIncluded && Math.abs(yRight - edge.endVertex.y) < epsilon ||
                endVertexIncluded && Math.abs(yLeft - edge.beginVertex.y) < epsilon) return false;
        return (edge.beginVertex.y - yLeft) * (edge.endVertex.y - yRight) <= 0 ||
                Math.abs(yLeft - edge.beginVertex.y) < epsilon ||
                endVertexIncluded && Math.abs(yRight - edge.endVertex.y) < epsilon ||
                !endVertexIncluded && Math.abs(yLeft - edge.beginVertex.y) < epsilon;
    }

    public double distanceTo(Point point){
        double dist =  Math.abs(d*point.x-b*point.y+c*b-a*d)/Math.sqrt(d*d+b*b);
        System.out.println(dist);
        return dist;
    }
}
