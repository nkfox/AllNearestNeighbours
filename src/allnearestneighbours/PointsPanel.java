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
    public static final Line up = new Line(0, HEIGHT, WIDTH, HEIGHT);
    public static final Line down = new Line(0, 0, WIDTH, 0);
    public static final Line left = new Line(0, 0, 0, HEIGHT);
    public static final Line right = new Line(WIDTH, 0, WIDTH, HEIGHT);
    private List<Point> points;
    private List<VoronoiPoint> convexHull;

    public PointsPanel(List<Point> points) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.points = points;
        convexHull = new ArrayList<>();
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        for (Point point : points) {
            point.update(page);
        }
        page.setColor(new Color(0));
        System.out.println(convexHull.size());
        for(int i=0;i<convexHull.size()-1;i++)
            page.drawLine(convexHull.get(i).x,convexHull.get(i).y,convexHull.get(i+1).x,convexHull.get(i+1).y);

    }

    public void setConvexHull(List<VoronoiPoint> convexHull) {
        this.convexHull = convexHull;
        if (convexHull.size() > 0)
            convexHull.add(convexHull.get(0));
    }
}
