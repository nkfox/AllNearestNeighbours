package allnearestneighbours;

import java.awt.*;
import java.util.Random;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * All info about point
 */
public class Point implements Comparable {
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

    public int compareTo(Object obj) {
        if (obj instanceof Point){
            Point point = (Point)obj;
            if (x > point.x || x == point.x && y < point.y) return 1;
            if (x == point.x && y == point.y) return 0;
            return -1;
        }
        return -1;
    }

    public double distanceTo(Point point) {
        if (point == null) return Double.MAX_VALUE;
        return Math.sqrt((x - point.x) * (x - point.x) + (y - point.y) * (y - point.y));
    }

    public void print() {
        System.out.println(x + " " + y);
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

    public void setNearestNeighbour(Point newNeighbour) {
        //this.nearestNeighbour = nearestNeighbour;
        if (nearestNeighbour == null || distanceTo(nearestNeighbour)>distanceTo(newNeighbour))
            nearestNeighbour = newNeighbour;
    }

    public void draw(Graphics page) {
        page.setColor(color);
        page.fillOval((int) x - 3, PointsPanel.HEIGHT - (int) y - 3, 6, 6);
        if (nearestNeighbour != null && PointsPanel.printNeighbours)
            page.drawLine((int) x, PointsPanel.HEIGHT - (int) y, (int) nearestNeighbour.x, PointsPanel.HEIGHT - (int) nearestNeighbour.y);
    }

    public void update(Graphics page) {
        draw(page);
    }

    @Override
    public boolean equals(Object point) {
        double epsilon = 0.00001;
        return point instanceof Point && Math.abs(x - ((Point) point).x) < epsilon && Math.abs(y - ((Point) point).y) < epsilon;
    }


}
