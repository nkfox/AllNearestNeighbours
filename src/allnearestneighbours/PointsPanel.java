package allnearestneighbours;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Artem
 */

public class PointsPanel extends JPanel {

    public static final int WIDTH = 600, HEIGHT = 400;

    public static double MAX = 1000000;
    public static final Line up = new Line(-MAX , MAX , MAX , MAX );
    public static final Line down = new Line(-MAX , -MAX , MAX , -MAX );
    public static final Line left = new Line(-MAX , -MAX , -MAX , MAX );
    public static final Line right = new Line(MAX , -MAX , MAX , MAX);

    private List<Point> points;
    private VoronoiDiagram diagram;

    public PointsPanel(List<Point> points) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.points = points;
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        for (Point point : points) {
            point.update(page);
        }

        page.setColor(new Color(333333333));
        printConvexHull(page);

        page.setColor(new Color(0));
        printDiagram(page);
    }

    private void printConvexHull(Graphics page) {
        int i;
        System.out.println( diagram.convexHull.size());
        for (i = 0; i < diagram.convexHull.size() - 1; i++) {
            page.drawLine((int) diagram.convexHull.get(i).x,HEIGHT- (int) diagram.convexHull.get(i).y,
                    (int) diagram.convexHull.get(i + 1).x, HEIGHT-(int) diagram.convexHull.get(i + 1).y);
            System.out.println( diagram.convexHull.size());
        }
        page.drawLine((int) diagram.convexHull.get(i).x,HEIGHT- (int) diagram.convexHull.get(i).y,
                (int) diagram.convexHull.get(0).x,HEIGHT- (int) diagram.convexHull.get(0).y);

    }

    private void printDiagram(Graphics page) {
        for (VoronoiPoint point : diagram.points) {
            VoronoiEdge current = point.firstEdge;
            if (current != null)
                do {
                    System.out.println(current.beginVertex.x + " " + current.beginVertex.y + " " + current.endVertex.x + " " + current.endVertex.y);
                    page.drawLine((int) current.beginVertex.x, HEIGHT-(int) current.beginVertex.y, (int) current.endVertex.x, HEIGHT-(int) current.endVertex.y);
                    current = current.clockwise;
                } while (current != null && !current.equals(point.firstEdge));
            System.out.println("\n");
        }
    }

    public void setDiagram(VoronoiDiagram diagram) {
        this.diagram = diagram;
    }
}
