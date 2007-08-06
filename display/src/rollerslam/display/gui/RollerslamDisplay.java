package rollerslam.display.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import orcas.logcomponents.basiclog.LogFactory;
import rollerslam.display.gui.mvc.Controller;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.mvc.View;

/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class RollerslamDisplay extends JFrame implements View, ActionListener {

    private Controller controller = null;

    private JButton connectButton = new JButton("Connect to Simulation");
    private JButton startButton = new JButton("Start Simulation");
    private JButton stopButton = new JButton("Stop Simulation");
    private GameCanvas game = new GameCanvas();

    public RollerslamDisplay() {

        Model model = new ModelImpl();
        this.controller = new ControllerImpl(this, model);

        game.setModel(model);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // get hold the content of the frame and set up the resolution of the game
        javax.swing.JPanel panel = new JPanel();
        panel.setPreferredSize(new java.awt.Dimension(800, 600));
        panel.setMinimumSize(new java.awt.Dimension(800, 600));
        panel.setSize(new java.awt.Dimension(800, 600));

        panel.setLayout(null);

        panel.add(game);

        getContentPane().add(panel);

        JPanel down = new JPanel();
        down.setLayout(new FlowLayout());
        down.add(connectButton);
        down.add(startButton);
        down.add(stopButton);

        connectButton.addActionListener(this);
        startButton.addActionListener(this);
        stopButton.addActionListener(this);

        getContentPane().add(down);

        // finally make the window visible
        pack();
        setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        game.init();
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) {
            connectButtonClick(e);
            return;
        }
        if (e.getSource() == startButton) {
            startButtonClick(e);
            return;
        }
        if (e.getSource() == stopButton) {
            stopButtonClick(e);
            return;
        }
        throw new RuntimeException("Object " + e.getSource() + " doesn't have action defined!");
    }

    protected void showException(Exception e1) {
        e1.printStackTrace();
        JOptionPane.showMessageDialog(this, e1.getMessage());
    }

    public static void main(String[] args) {
        LogFactory.getInstance().getLog().log("Client display initializing...");
        new RollerslamDisplay();
        LogFactory.getInstance().getLog().log("Client initialized...");
    }

    private void connectButtonClick(ActionEvent e) {
        String addr = JOptionPane.showInputDialog("Simulation URL:", "localhost");
        try {
            controller.connect(addr);
        } catch (Exception e1) {
            showException(e1);
        }
    }

    private void startButtonClick(ActionEvent e) {
        try {
            controller.startSimulation();
        } catch (Exception e1) {
            showException(e1);
        }
    }

    private void stopButtonClick(ActionEvent e) {
        try {
            controller.stopSimulation();
        } catch (Exception e1) {
            showException(e1);
        }
    }
}
