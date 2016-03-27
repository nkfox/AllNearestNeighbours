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
        /*while(n-->0){
            Point point = new Point();
            point.print();
            points.add(point);
        }*/

        /*points.add(new Point(50,150));
        points.add(new Point(100,100));
        points.add(new Point(100,200));
        points.add(new Point(150,150));*/

        /*// worked
        points.add(new Point(14.387708063040927,257.50095740058134));
        points.add(new Point(262.8715036214173,152.13450244280767));
        points.add(new Point(290.6051400239267,330.1350815030661));
        points.add(new Point(546.0364444369347,36.71177204163576));
        points.add(new Point(588.2094851002904,304.4107512161108));*/

       /* //three points on one line
        points.add(new Point(536.9823347276437,123.02802995020201));
        points.add(new Point(534.9551920464446,342.0229870734802));
        points.add(new Point(12.917193875781319,295.08003193035495));
        points.add(new Point(336.8117597512865,273.4084051779495));
        points.add(new Point(541.0527508505194,19.113295730066106));*/

        points.add(new Point(35.921312251015095,197.7140353426867));
        points.add(new Point(39.17465602202921,58.127030891360846));
        points.add(new Point(170.42553832241137,63.10318960613177));
        points.add(new Point(438.65639843058534,184.0539107361582));
        points.add(new Point(516.3257649046346,11.254652433007095));

       /* points.add(new Point(296,282));
        points.add(new Point(342,17));
        points.add(new Point(357,139));
        points.add(new Point(388,43));
        points.add(new Point(516,156));*/
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
        getRandomPoints(5);

        //System.out.println("\n");
        getNearestNeighboursBruteForce();
        PointsPanel panel =  printNeighbours();

        Collections.sort(points);
        List<VoronoiPoint> voronoiPoints = points.stream().map(VoronoiPoint::new).collect(Collectors.toList());
        System.out.println(6);
        VoronoiDiagram voronoiDiagram = new VoronoiDiagram(voronoiPoints);
        System.out.println(7);
        panel.setDiagram(voronoiDiagram);

        //VoronoiDiagram v = new VoronoiDiagram(voronoiPoints);

    }
}
