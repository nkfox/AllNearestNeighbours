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

    public PointsPanel(List<Point> points) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.points = points;
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        for (Point point : points)
            point.draw(page);

        page.setColor(new Color(333333333));
        printConvexHull(page);

        page.setColor(new Color(0));
        if (diagram != null)
            diagram.draw(page);
    }

    private void printConvexHull(Graphics page) {
        int i;
        for (i = 0; i < diagram.convexHull.size() - 1; i++) {
            page.drawLine((int) diagram.convexHull.get(i).x, HEIGHT - (int) diagram.convexHull.get(i).y,
                    (int) diagram.convexHull.get(i + 1).x, HEIGHT - (int) diagram.convexHull.get(i + 1).y);
        }
        page.drawLine((int) diagram.convexHull.get(i).x, HEIGHT - (int) diagram.convexHull.get(i).y,
                (int) diagram.convexHull.get(0).x, HEIGHT - (int) diagram.convexHull.get(0).y);

    }

    public void setDiagram(VoronoiDiagram diagram) {
        this.diagram = diagram;
    }
}
