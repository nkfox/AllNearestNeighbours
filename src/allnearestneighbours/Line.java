package allnearestneighbours;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
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

    public Point intersection(Line line){
        if (b*line.d == d*line.b)
            return null;
        if(b == 0)
            return new Point(a,line.findY(a));
        if (line.b==0)
            return new Point(line.a,findY(line.a));
        double x = (a*d/b-c-line.d*line.a/line.b+line.c)/(d/b-line.d/line.b);
        double y = findY(x);
        return new Point(x,y);
    }

    public double findY(double x){
        return (x-a+0.0)*d/b+c;
    }

    public double findX(double y){
        return (y-c+0.0)*b/d+a;
    }

    public boolean intersects(VoronoiEdge edge){
        /*if (edge.beginVertex.y> edge.endVertex.y)
            edge=edge.reverse;*/
        double yLeft = findY(edge.beginVertex.x);
        double yRight = findY(edge.endVertex.x);
        /*if (yLeft > yRight){
            int temp = yLeft;
            yLeft = yRight;
            yRight = temp;
        }*/
        return (edge.beginVertex.y-yLeft)*(edge.endVertex.y-yRight)<=0;
    }
}
