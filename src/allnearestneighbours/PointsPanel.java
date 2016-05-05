package allnearestneighbours;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by Nataliia Kozoriz on 19.03.2016.
 * Paint points with neighbours or diagram
 */
public class PointsPanel extends JPanel {

    public static final int WIDTH = 1350, HEIGHT = 700;

    public static double MAX = 100000000;
    public static final Line up = new Line(-MAX, MAX, MAX, MAX);
    public static final Line down = new Line(-MAX, -MAX, MAX, -MAX);
    public static final Line left = new Line(-MAX, -MAX, -MAX, MAX);
    public static final Line right = new Line(MAX, -MAX, MAX, MAX);

    private List<Point> points;

    private VoronoiDiagram diagram;
    public static boolean printNeighbours = true;
    public static boolean printConvexHull = true;
    public static boolean printDiagram = true;


    public PointsPanel(List<Point> points) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.points = points;
    }

    public PointsPanel(VoronoiDiagram diagram) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.diagram = diagram;
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        if (points != null) {
            for (Point point : points)
                point.draw(page);
        }

        if (diagram != null) {
            if (printConvexHull) {
                page.setColor(new Color(333333333));
                printConvexHull(page);
            }

            diagram.draw(page);
        }
    }

    private void printConvexHull(Graphics page) {
        VoronoiPoint first = null, prev = null;
        for (VoronoiPoint point : diagram.convexHull) {
            if (first == null) first = point;
            if (prev != null)
                page.drawLine((int) point.x, HEIGHT - (int) point.y,
                        (int) prev.x, HEIGHT - (int) prev.y);
            prev = point;
        }
        if (first != null)
            page.drawLine((int) first.x, HEIGHT - (int) first.y,
                    (int) prev.x, HEIGHT - (int) prev.y);

    }
}
