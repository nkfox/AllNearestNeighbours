import allnearestneighbours.Point;
import allnearestneighbours.PointsPanel;
import allnearestneighbours.VoronoiDiagram;
import allnearestneighbours.VoronoiPoint;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * Main
 */
public class Main {

    static List<Point> points;

    public static void getRandomPoints(int n) {
        points = new ArrayList<>();
        /*while(n-->0){
            Point point = new Point();
            //point.print();
            points.add(point);
        }*/

        points.add(new Point(50,150));
        points.add(new Point(100,100));
        points.add(new Point(100,200));
        points.add(new Point(150,150));

        /*//one edge intersected twice
        points.add(new Point(83.02382472734023,302.3972688413379));
        points.add(new Point(217.29757168920074,271.9796315018999));
        points.add(new Point(303.0322488571952,127.28579802818949));
        points.add(new Point(416.4147690849933,284.4969709688983));
        points.add(new Point(490.0263161424196,137.63432698355564));*/

        /*// worked
        points.add(new Point(14.387708063040927, 257.50095740058134));
        points.add(new Point(262.8715036214173, 152.13450244280767));
        points.add(new Point(290.6051400239267, 330.1350815030661));
        points.add(new Point(546.0364444369347, 36.71177204163576));
        points.add(new Point(588.2094851002904, 304.4107512161108));*/

        /*//three points on one line - ok
        points.add(new Point(536.9823347276437,123.02802995020201));
        points.add(new Point(534.9551920464446,342.0229870734802));
        points.add(new Point(12.917193875781319,295.08003193035495));
        points.add(new Point(336.8117597512865,273.4084051779495));
        points.add(new Point(541.0527508505194,19.113295730066106));*/

        /*//ok
        points.add(new Point(35.921312251015095,197.7140353426867));
        points.add(new Point(39.17465602202921,58.127030891360846));
        points.add(new Point(170.42553832241137,63.10318960613177));
        points.add(new Point(438.65639843058534,184.0539107361582));
        points.add(new Point(516.3257649046346,11.254652433007095));*/

        /*//ok
        points.add(new Point(296,282));
        points.add(new Point(342,17));
        points.add(new Point(357,139));
        points.add(new Point(388,43));
        points.add(new Point(516,156));*/

        /*points.add(new Point(14.81546770154496,61.99998934992226));
        points.add(new Point(51.680775578502725,223.60429345107232));
        points.add(new Point(76.94845431982134,253.79432344833367));
        points.add(new Point(154.87348099959684,247.18613302751322));
        points.add(new Point(161.22913568683686,399.0070520266005));

        points.add(new Point(187.76067245900433,377.9395307438273));
        points.add(new Point(191.6700277245859,352.62132773437645));
        points.add(new Point(196.83095556236768,187.09805456229302));
        points.add(new Point(212.76796353757953,90.40696801540764));
        points.add(new Point(223.01208031812158,282.4511197790884));

        points.add(new Point(239.29068887731688,291.608141152069));
        points.add(new Point(246.2034297245269,247.65536033493015));
        points.add(new Point(248.22992085785026,270.2992574465615));
        points.add(new Point(280.2021252251342,12.449132719952205));
        points.add(new Point(292.4814217057355,209.7696658797179));

        points.add(new Point(320.4740485753601,375.7614653025065));
        points.add(new Point(342.56093068071993,170.13847864427322));
        points.add(new Point(345.241210694582,158.36940203396287));
        points.add(new Point(354.7390092709726,347.10542902551737));
        points.add(new Point(381.08905817686224,267.02941667958027));

        points.add(new Point(381.515170318542,93.0751012143634));
        points.add(new Point(443.3634405705981,389.23667534482064));
        points.add(new Point(470.70174633102965,365.45458615082055));
        points.add(new Point(478.1210767760621,140.86816155993515));
        points.add(new Point(508.8917377515082,363.78023616054094));

        points.add(new Point(524.4342996365244,245.33405314933225));
        points.add(new Point(558.9841500635202,212.31118958483447));
        points.add(new Point(567.3843552268543,354.0973604678288));
        points.add(new Point(579.0996369456861,231.368496830049));
        points.add(new Point(589.0632394157781,51.00255842586052));*/



    }

    public static void getNearestNeighboursBruteForce() {
        for (Point point : points) {
            points.stream().filter(otherPoint -> point != otherPoint && point.distanceTo(otherPoint) <
                    point.distanceTo(point.getNearestNeighbour())).forEach(point::setNearestNeighbour);
            //point.printNeighbour();
        }
    }

    public static PointsPanel printNeighbours() {
        JFrame circlesFrame = new JFrame("Points");
        circlesFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PointsPanel panel = new PointsPanel(points);
        circlesFrame.getContentPane().add(panel);
        circlesFrame.pack();
        circlesFrame.setVisible(true);
        return panel;
    }

    public static void main(String[] args) {

        getRandomPoints(10);

        getNearestNeighboursBruteForce();
        PointsPanel panel = printNeighbours();

        Collections.sort(points);
        List<VoronoiPoint> voronoiPoints = points.stream().map(VoronoiPoint::new).collect(Collectors.toList());
        System.out.println("Points sorted");
        VoronoiDiagram voronoiDiagram = new VoronoiDiagram(voronoiPoints);
        System.out.println("Diagram finished");
        panel.setDiagram(voronoiDiagram);
        System.out.println("Painting finished");
    }
}
