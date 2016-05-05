package allnearestneighbours;

import java.awt.*;
import java.util.Random;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * All info about point
 */
public class Point implements Comparable<Point> {
    public double x;
    public double y;
    Point nearestNeighbour;
    protected Color color = new Color(Math.abs((new Random()).nextInt()) % 16777216);

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        nearestNeighbour = null;
    }

    public Point(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        nearestNeighbour = null;
        this.color = color;
    }

    public Point() {
        this.x = Math.random() * PointsPanel.WIDTH;
        this.y = Math.random() * PointsPanel.HEIGHT;
    }

    public int compareTo(Point point) {
        if (x > point.x || x == point.x && y < point.y) return 1;
        if (x == point.x && y == point.y) return 0;
        return -1;

    }

    public double distanceTo(Point point) {
        if (point == null) return Double.MAX_VALUE;
        return Math.sqrt((x - point.x) * (x - point.x) + (y - point.y) * (y - point.y));
    }

    public void print() {
        System.out.println(x + " " + y);
    }

    public Point getNearestNeighbour() {
        return nearestNeighbour;
    }

    public void setNearestNeighbour(Point newNeighbour) {
        if (nearestNeighbour == null || distanceTo(nearestNeighbour) > distanceTo(newNeighbour))
            nearestNeighbour = newNeighbour;
    }

    public void draw(Graphics page) {
        page.setColor(color);
        page.fillOval((int) x - 3, PointsPanel.HEIGHT - (int) y - 3, 6, 6);
        if (nearestNeighbour != null && PointsPanel.printNeighbours)
            page.drawLine((int) x, PointsPanel.HEIGHT - (int) y, (int) nearestNeighbour.x, PointsPanel.HEIGHT - (int) nearestNeighbour.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object point) {
        double epsilon = 0.00001;
        return point instanceof Point && Math.abs(x - ((Point) point).x) < epsilon && Math.abs(y - ((Point) point).y) < epsilon;
    }

    public static double polarAngle(Point center, Point p) {
        double alpha = Math.atan2(p.y - center.y, p.x - center.x);
        if (alpha < 0) alpha += 2 * Math.PI;
        return alpha;
    }
}
