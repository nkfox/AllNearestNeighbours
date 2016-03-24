package allnearestneighbours;

import java.awt.*;
import java.util.Random;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 */
public class Point implements Comparable<Point> {
    double x;
    double y;
    Point nearestNeighbour;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        nearestNeighbour = null;
    }

    public Point() {
        this.x = Math.random() * PointsPanel.WIDTH;
        this.y = Math.random() * PointsPanel.HEIGHT;
    }

    public int compareTo(Point point) {
        if (x > point.x || x == point.x && y > point.y) return 1;
        if (x == point.x && y == point.y) return 0;
        return -1;
    }

    public double distanceTo(Point point) {
        if (point == null) return Double.MAX_VALUE;
        return Math.sqrt((x - point.x) * (x - point.x) + (y - point.y) * (y - point.y));
    }

    public void print() {
        System.out.println("(" + x + "," + y + ")");
    }

    public void printNeighbour() {
        System.out.print("(" + x + "," + y + ") : dist " +
                String.format("%5.2f", distanceTo(nearestNeighbour)) + "     ");
        if (nearestNeighbour != null)
            nearestNeighbour.print();
    }

    public Point getNearestNeighbour() {
        return nearestNeighbour;
    }

    public void setNearestNeighbour(Point nearestNeighbour) {
        this.nearestNeighbour = nearestNeighbour;
    }

    private Color color = new Color(Math.abs((new Random()).nextInt()) % 16777216);

    public void draw(Graphics page) {
        page.setColor(color);
        page.fillOval((int)x - 3, PointsPanel.HEIGHT-(int)y - 3, 6, 6);
        //page.drawLine(x, y, nearestNeighbour.x, nearestNeighbour.y);
    }

    public void update(Graphics page) {
        draw(page);
    }

    @Override
    public boolean equals(Object point) {
        if (point instanceof Point)
            return x == ((Point) point).x && y == ((Point) point).y;
        return false;
    }


}
