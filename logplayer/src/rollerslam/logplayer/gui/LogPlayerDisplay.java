package rollerslam.logplayer.gui;

import javax.swing.event.ChangeEvent;
import rollerslam.display.gui.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import rollerslam.display.gui.mvc.Model;
import rollerslam.logplayer.gui.mvc.View;
import rollerslam.logplayer.gui.mvc.Controller;


/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class LogPlayerDisplay extends JPanel implements View, ActionListener, ChangeListener {

    private Controller controller = null;

    private JButton loadSimButton = new JButton("Load Simulation");
    private JLabel messages = new JLabel("");
    private JPanel controls;
    private JPanel info;
    private JLabel lblCurrentCycle;


    private JSlider cycleSlider;

    private JButton runStopButton = new JButton("Run");


    private GameCanvas game = new GameCanvas(messages);

    public LogPlayerDisplay() {
        Model model = new ModelImpl();
        this.controller = new ControllerImpl(this, model);

        game.setModel(model);

        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = new JPanel();

        int w = 800;
        int h = 600;
        panel.setPreferredSize(new java.awt.Dimension(w, h));
        panel.setMinimumSize(new java.awt.Dimension(w, h));
        panel.setSize(new java.awt.Dimension(w, h));

        panel.setLayout(null);

        panel.add(game);

        this.add(panel);


        info = new JPanel();
        info.setLayout(new FlowLayout());
        lblCurrentCycle = new JLabel();
        info.add(lblCurrentCycle);
        this.add(info);

        controls = new JPanel();
        controls.setLayout(new FlowLayout());

        cycleSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 1);
        cycleSlider.setEnabled(false);
        cycleSlider.addChangeListener(this);
        controls.add(cycleSlider);

        runStopButton.addActionListener(this);
        controls.add(runStopButton);
        runStopButton.setEnabled(false);
        this.add(controls);

        JPanel down = new JPanel();
        down.setLayout(new FlowLayout());
        down.add(messages);
        down.add(loadSimButton);

        loadSimButton.addActionListener(this);

        this.add(down);
    }

    public void main() {
        game.init();
    }


    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == loadSimButton) {
            loadSimButtonClick(e);
            return;
        }
        if (o == runStopButton) {
            runStopButtonClick(e);
            return;
        }
        throw new RuntimeException("Object " + e.getSource() + " doesn't have action defined!");
    }

    protected void showException(Exception e1) {
        e1.printStackTrace();
        JOptionPane.showMessageDialog(this, e1.getMessage());
    }

    public static void main(String[] args) {
        LogPlayerDisplay panel = new LogPlayerDisplay();

        JFrame jf = new JFrame();

        jf.getContentPane().setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
        jf.getContentPane().add(panel);

        // finally make the window visible
        jf.pack();
        jf.setResizable(false);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.main();
        jf.setVisible(true);
    }

    public void loadSimButtonClick(ActionEvent e) {
        File f = null;

        JFileChooser jfc = new JFileChooser("c:\\temp");
        jfc.removeChoosableFileFilter(jfc.getFileFilter());
        jfc.setFileFilter(new FileFilter() {
            public boolean accept(File pathname) {
                if (!pathname.isDirectory()) {
                    if (pathname.getName().endsWith(".script")) {
                        return true;
                    }
                    return false;
                }
                return true;
            }

            public String getDescription() {
                return "Rollerslam db file (*.script)";
            }
        });
        
        int opt = jfc.showOpenDialog(this);
        if (opt != JFileChooser.APPROVE_OPTION) {
            return;
        }
        f = jfc.getSelectedFile();
        try {
            controller.load(f);
        } catch (Exception e1) {
            showException(e1);
        }

        runStopButton.setEnabled(true);
        cycleSlider.setEnabled(true);
    }

    public void runStopButtonClick(ActionEvent e) {
        if (runStopButton.getText().equals("Run")) {
            this.controller.play();
            runStopButton.setText("Stop");
        } else {
            this.controller.stop();
            runStopButton.setText("Run");
        }
    }

    public void updateCurrentCycleMsg(Integer c, Integer m) {
        this.lblCurrentCycle.setText("Current cycle: " + c + " Total cycles: " + m);
    }

    public void updateSliderBounds(Integer m) {
        cycleSlider.setMaximum(m);
    }

    public void updateSlider(Integer i) {
        cycleSlider.setValue(i);
    }

    public void stateChanged(ChangeEvent e) {
        this.controller.goTo(cycleSlider.getValue());
    }
}
