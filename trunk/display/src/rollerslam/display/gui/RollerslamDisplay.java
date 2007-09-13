package rollerslam.display.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import rollerslam.display.gui.mvc.Controller;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.mvc.View;
import rollerslam.infrastructure.server.PrintTrace;

/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class RollerslamDisplay extends JPanel implements View, ActionListener {

    private Controller controller = null;

    private JButton connectButton = new JButton("Connect to Simulation");
    private JLabel messages = new JLabel("");

    private GameCanvas game = new GameCanvas(messages);

    public RollerslamDisplay() {
        Model model = new ModelImpl();
        this.controller = new ControllerImpl(this, model);

        game.setModel(model);

        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = new JPanel();

        int w = 752;
        int h = 556;
        panel.setPreferredSize(new Dimension(w, h));
        panel.setMinimumSize(new Dimension(w, h));
        panel.setSize(new Dimension(w, h));
        panel.setMaximumSize(new Dimension(w, h));

        panel.setLayout(null);

        panel.add(game);

        this.add(panel);

        JPanel down = new JPanel();
        down.setLayout(new FlowLayout());
        down.add(messages);

        this.add(down);

        JPanel down2 = new JPanel();
        down2.setLayout(new FlowLayout());
        down2.add(connectButton);

        connectButton.addActionListener(this);

        this.add(down2);
        
    }

    public void main() {
        game.init();
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connectButton) {
            connectButtonClick(e);
            return;
        }
        throw new RuntimeException("Object " + e.getSource() + " doesn't have action defined!");
    }

    protected void showException(Exception e1) {
        if (PrintTrace.TracePrint) {
            e1.printStackTrace();
        }
        String msg = e1.getMessage();
        if (msg == null || "".equals(msg)) {
            msg = "Exception class: " + e1.getClass().getName();
        }
        JOptionPane.showMessageDialog(this, msg, "There was an error...", JOptionPane.ERROR_MESSAGE);        
    }

    public static void main(String[] args) {
        RollerslamDisplay panel = new RollerslamDisplay();

        JFrame jf = new JFrame("Rollerslam Display");

        jf.getContentPane().setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
        jf.getContentPane().add(panel);

        // finally make the window visible
        jf.pack();
        jf.setResizable(false);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.main();
        jf.setVisible(true);
    }

    private void connectButtonClick(ActionEvent e) {
        final String DEFAULT_MSG = "Select one option";
        Vector<String> options = new Vector<String>();
        options.add(DEFAULT_MSG);

        try {
            options.addAll(controller.getAvailableHosts());
        } catch (Exception e2) {
            this.showException(e2);
        }

        JComboBox jcb = new JComboBox(options);
        jcb.setEditable(true);

        int opt = JOptionPane.showConfirmDialog(this, jcb, "Select or inform server URI", JOptionPane.OK_CANCEL_OPTION);
        if (opt != JOptionPane.OK_OPTION) {
            return;
        }

        String addr = (String) jcb.getSelectedItem();
        System.out.println("CONNECTING TO " + addr);

        try {
            controller.connect(addr);
        } catch (Exception e1) {
            showException(e1);
        }
    }
}
