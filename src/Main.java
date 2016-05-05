import allnearestneighbours.Point;
import allnearestneighbours.PointsPanel;
import allnearestneighbours.VoronoiDiagram;
import allnearestneighbours.VoronoiPoint;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * Main
 */
public class Main {

    static List<Point> points;
    static VoronoiDiagram voronoiDiagram;

    public static void getRandomPoints(int n) {
        points = new ArrayList<>();
        while (n-- > 0) {
            Point point = new Point();
            boolean ok = true;
            for (Point p : points) {
                if (point.distanceTo(p) < 1) {
                    ok = false;
                    break;
                }
            }
            if (ok)
                points.add(point);
            else n++;
        }
    }

    static void getData() {
        points = new ArrayList<>();
        try (FileInputStream inp = new FileInputStream("D:/points.txt")) {
            Scanner in = new Scanner(new InputStreamReader(inp, "UTF-8"));
            while (in.hasNext()) {
                Double a = in.nextDouble();
                Double b = in.nextDouble();
                points.add(new Point(a, b));
            }
        } catch (Exception ignored) {
        }
        //points = points.subList(0, points.size() / 2);
        //points=points.subList(points.size() / 2, points.size());
    }

    public static PointsPanel printNeighbours() {
        JFrame pointsFrame = new JFrame("Points");
        //pointsFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PointsPanel panel = new PointsPanel(points);
        pointsFrame.getContentPane().add(panel);
        pointsFrame.pack();
        pointsFrame.setVisible(true);
        return panel;
    }

    public static PointsPanel printNeighbours(VoronoiDiagram diagram) {
        JFrame voronoiFrame = new JFrame("Diagram");
        //voronoiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PointsPanel panel = new PointsPanel(diagram);
        voronoiFrame.getContentPane().add(panel);
        voronoiFrame.pack();
        voronoiFrame.setVisible(true);
        return panel;
    }

    public static double getNearestNeighboursBruteForce() {
        long startTime = System.nanoTime();
        for (Point point : points) {
            points.stream().filter(otherPoint -> point != otherPoint && point.distanceTo(otherPoint) <
                    point.distanceTo(point.getNearestNeighbour())).forEach(point::setNearestNeighbour);
        }
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1000000000.0;
        System.out.println(duration + " seconds");
        printNeighbours();
        return duration;
    }

    public static double getNearestNeighboursVoronoi() {
        long startTime = System.nanoTime();
        Collections.sort(points);
        /*for (Point point : points)
            point.print();*/
        List<VoronoiPoint> voronoiPoints = points.stream().map(VoronoiPoint::new).collect(Collectors.toList());
        voronoiDiagram = new VoronoiDiagram(voronoiPoints);

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1000000000.0;
        System.out.println(duration + " seconds");
        printNeighbours(voronoiDiagram);
        return duration;
    }

    public static void main(String[] args) {
        new MainForm();
    }
}
