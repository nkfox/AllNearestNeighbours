package allnearestneighbours;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 */
public class Line {

    //(x-a)/b = (y-c)/d
    int a;
    int b;
    int c;
    int d;

    public Line(int x1, int y1, int x2, int y2) {
        a = x1;
        b = x2 - x1;
        c = y1;
        d = y2 - y1;
    }

    public Line(Point p1, Point p2, boolean isPerpendicular) {
        this(p1.x, p1.y, p2.x, p2.y);
        if (isPerpendicular) {
            Point middle = new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            int temp = b;
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
        int x = (int)((a*d*1.0/b-c-line.d*line.a*1.0/line.b+line.c)/(d*1.0/b+line.d*1.0/line.b));
        int y = findY(x);
        return new Point(x,y);
    }

    public int findY(int x){
        return (int)((x-a+0.0)*d/b+c);
    }

    public int findX(int y){
        return (int)((y-c+0.0)*b/d+a);
    }
}
