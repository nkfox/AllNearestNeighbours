import allnearestneighbours.PointsPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Nataliia Kozoriz on 10.04.2016.
 */
public class MainForm extends JFrame {

    private JPanel panelRoot;
    private JTextArea infoTextArea;
    private JButton buttonBF;
    private JButton buttonVoronoi;
    private JButton buttonFile;
    private JButton buttonHelp;
    private JButton buttonPoints;
    private JTextField textFieldPointsAmount;
    private JCheckBox checkBoxNeighbours;
    private JCheckBox checkBoxConvexHull;
    private JCheckBox checkBoxDiagram;

    public MainForm() {
        super("Nearest neighbours");
        setContentPane(panelRoot);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        buttonPoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String val = textFieldPointsAmount.getText();
                Integer n = 10;
                try {
                    n = Integer.valueOf(val);
                } catch (Exception ex) {
                    infoTextArea.append("It is not a number\n");
                }
                if (n > 0) {
                    Main.getRandomPoints(n);
                    infoTextArea.append(n + " points are generated\n\n");
                } else {
                    infoTextArea.append(n + " is not positive\n\n");
                }
            }
        });

        buttonFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getData();
                int n = Main.points.size();
                infoTextArea.append(n + " points are read from file\n\n");
            }
        });

        buttonBF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.points == null)
                    infoTextArea.append("There are no points\n\n");
                else {
                    double sec = Main.getNearestNeighboursBruteForce();
                    infoTextArea.append("Brute force algorithm\n"+sec+" seconds\n\n");
                }
            }
        });

        buttonVoronoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Main.points == null)
                    infoTextArea.append("There are no points\n\n");
                else {
                    PointsPanel.printNeighbours = checkBoxNeighbours.isSelected();
                    PointsPanel.printConvexHull = checkBoxConvexHull.isSelected();
                    PointsPanel.printDiagram = checkBoxDiagram.isSelected();
                    double sec = Main.getNearestNeighboursVoronoi();
                    infoTextArea.append("Voronoi diagram are built\n"+sec+" seconds\n\n");
                }
            }
        });

        buttonHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame pointsFrame = new JFrame("Help");
                JTextArea area = new JTextArea("Help\n\nMain form has text area and buttons.\nText area shows info about all events\n"+
                "\'Generate points\' button randomly generate points. The amount has to be entered in the field below. "+
                "If the field in filled incorrectly,\nthe program announces about it and set default number of points (10).\n"+
                "Button \'Read from file\' is the other method to get points. The amount of points that are read are shown in the text area.\n\n"+
                "\'Brute force\' button gets all nearest neighbours by trying all pairs of points. Draw all points and connect them "+
                "with trein nearest neighbours.\n\'Voronoi diagram\' button gets all nearest neighbours by building voronoi diagram."+
                "Three checkboxes below give the opportunity to choose\nwhat information are shown in the picture: neighbours, "+
                "convex hull and/or voronoi diagram.\nTime of both algorithms are shown in the ext area.\n");
                area.setEditable(false);
                pointsFrame.getContentPane().add(area);
                pointsFrame.pack();
                pointsFrame.setVisible(true);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
