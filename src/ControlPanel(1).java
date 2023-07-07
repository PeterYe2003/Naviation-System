// Peter Ye
// Control Panel is the class that is in charge of holding the navCanvas, buttons, and drawing of the path.

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

class ControlPanel extends JPanel {
    private JPanel navCanvas;
    NavigationSystem navigationSystem;
    Node firstClickedNode;
    Node secondClickedNode;
    JLabel distanceLabel;
    JLabel timeLabel;
    JLabel errorLabel;
    BufferedImage bufferedImage;


    ControlPanel(NavigationSystem navSystem) {
        navigationSystem = navSystem;
        navCanvas = new NavCanvas();
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBorder(new TitledBorder("Path Finding"));

        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new FindPathButtonListener());
        actionPanel.add(findPathButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new clearMapButtonListener());
        actionPanel.add(clearButton);
        clearButton.setPreferredSize(new Dimension(200, 40));

        distanceLabel = new JLabel();
        distanceLabel.setFont(new Font("Sans Serif", Font.BOLD, 15));
        distanceLabel.setText("Distance is:");
        actionPanel.add(distanceLabel);

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Sans Serif", Font.BOLD, 15));
        timeLabel.setText("Time to find path:");
        actionPanel.add(timeLabel);

        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Sans Serif", Font.BOLD, 12));
        errorLabel.setText("Please pick start and end locations");
        actionPanel.add(errorLabel);



        setLayout(new BorderLayout());
        add(navCanvas, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.EAST);

    }

    class FindPathButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (firstClickedNode == null || secondClickedNode == null) {
                errorLabel.setVisible(true);
            } else {
                distanceLabel.setText("Distance is: " + navigationSystem.pathFindingDijkstra(firstClickedNode, secondClickedNode));
                timeLabel.setText("Time to find path: " + navigationSystem.pathFindingDijkstraTime() + " ms");
                Graphics2D b = (Graphics2D) navCanvas.getGraphics();
                b.setStroke(new BasicStroke(3));
                b.setColor(Color.BLUE);
                Node currentNode = secondClickedNode;
                while(currentNode.linkToPreviousNode !=null){
                    for (int a = 0; a < currentNode.linkToPreviousNode.totalPoints - 1; a++) {
                        b.drawLine(currentNode.linkToPreviousNode.x[a], currentNode.linkToPreviousNode.y[a], currentNode.linkToPreviousNode.x[a + 1], currentNode.linkToPreviousNode.y[a + 1]);
                    }
                    currentNode = currentNode.previousNode;
                }
                errorLabel.setVisible(false);
            }
        }
    }

    class clearMapButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            firstClickedNode = null;
            secondClickedNode = null;
            Graphics g = navCanvas.getGraphics();
            g.drawImage(bufferedImage, 0, 0, navCanvas.getWidth(), navCanvas.getHeight(), navCanvas);
            distanceLabel.setText("Distance is:");
            timeLabel.setText("Time to find path:");
        }
    }

// NavCanvas is  in charge of painting the map and taking in the nodes that are clicked.
    class NavCanvas extends JPanel implements MouseListener {

        NavCanvas() {
            setPreferredSize(new Dimension(navigationSystem.giveMaxX() + 10, navigationSystem.giveMaxY() + 10));
            bufferedImage = new BufferedImage(navigationSystem.giveMaxX() + 10, navigationSystem.giveMaxY() + 10, BufferedImage.TYPE_INT_RGB);
            addMouseListener(this);
        }


        public void paintComponent(Graphics g) {
            Graphics b = bufferedImage.getGraphics();
            b.setColor(Color.WHITE);
            b.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            b.setColor(Color.GRAY);
            for (Link link : navigationSystem.linkHashmap.values()) {
                for (int a = 0; a < link.totalPoints - 1; a++) {
                    b.drawLine(link.x[a], link.y[a], link.x[a + 1], link.y[a + 1]);
                }
            }
            g.drawImage(bufferedImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent event) {
            // Clears the previous dots.
            if (firstClickedNode == null) {
                int mouseX = event.getX();
                int mouseY = event.getY();
                firstClickedNode = navigationSystem.findNearestNode(mouseX, mouseY);
                Graphics pen = this.getGraphics();
                pen.setColor(Color.BLUE);
                pen.fillOval(firstClickedNode.x - 5, firstClickedNode.y - 5, 10, 10);
            } else if (secondClickedNode == null) {
                int mouseX = event.getX();
                int mouseY = event.getY();
                secondClickedNode = navigationSystem.findNearestNode(mouseX, mouseY);
                Graphics pen = this.getGraphics();
                pen.setColor(Color.RED);
                pen.fillOval(secondClickedNode.x - 5, secondClickedNode.y - 5, 10, 10);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

}