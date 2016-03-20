import allnearestneighbours.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 *
 */
public class Main {

    static List<Point> points;

    public static void getRandomPoints(int n){
        points = new ArrayList<>();
        while(n-->0){
            Point point = new Point();
            //point.print();
            points.add(point);
        }
    }

    public static void getNearestNeighboursBruteForce(){
        for (Point point: points){
            for(Point otherPoint: points){
                if (point!=otherPoint && point.distanceTo(otherPoint)<point.distanceTo(point.getNearestNeighbour())){
                    point.setNearestNeighbour(otherPoint);
                }
            }
            //point.printNeighbour();
        }
    }

    public static PointsPanel printNeighbours(){
        JFrame circlesFrame = new JFrame("Points");
        circlesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PointsPanel panel = new PointsPanel(points);
        circlesFrame.getContentPane().add(panel);
        circlesFrame.pack();
        circlesFrame.setVisible(true);
        return panel;
    }

    public static void main(String[] args) {
        getRandomPoints(10);

        //System.out.println("\n");
        getNearestNeighboursBruteForce();
        PointsPanel panel =  printNeighbours();

        Collections.sort(points);
        List<VoronoiPoint> voronoiPoints = points.stream().map(VoronoiPoint::new).collect(Collectors.toList());
        VoronoiDiagram voronoiDiagram = new VoronoiDiagram(voronoiPoints);

        panel.setConvexHull(voronoiDiagram.getConvexHull());

        VoronoiDiagram v = new VoronoiDiagram(voronoiPoints);
    }
}
