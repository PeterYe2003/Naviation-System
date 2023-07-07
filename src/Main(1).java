// Peter Ye

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    Main() {
        ControlPanel controlPanel = new ControlPanel(new NavigationSystem());

        JFrame frame = new JFrame("NavigationSystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(controlPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.toFront();

    }
}