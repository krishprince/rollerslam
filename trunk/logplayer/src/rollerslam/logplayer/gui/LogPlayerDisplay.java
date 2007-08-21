package rollerslam.logplayer.gui;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import rollerslam.display.gui.*;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
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
    
    private JLabel lblWait;


    private JSlider cycleSlider;
    private JSlider speedSlider;

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

        int w = 752;
        int h = 556;
        panel.setPreferredSize(new Dimension(w, h));
        panel.setMinimumSize(new Dimension(w, h));
        panel.setSize(new Dimension(w, h));
        panel.setMaximumSize(new Dimension(w, h));
        
        panel.setLayout(null);

        panel.add(game);

        this.add(panel);

        
        controls = new JPanel();
        controls.setLayout(null);
        controls.setBorder(BorderFactory.createTitledBorder("Simulation Execution Controls"));
        
        Dimension cD = new Dimension(752, 200);
        controls.setMinimumSize(cD);
        controls.setMaximumSize(cD);
        controls.setSize(cD);
        controls.setPreferredSize(cD);
       // controls.setBackground(Color.RED);
        
        
        lblCurrentCycle = new JLabel("Cycle 0 of 100.");
        controls.add(lblCurrentCycle);
        lblCurrentCycle.setBounds(10, 5, 350, 40);
                
                
        cycleSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
        cycleSlider.setEnabled(false);
        cycleSlider.addChangeListener(this);
        cycleSlider.setMajorTickSpacing(100);
        cycleSlider.setPaintLabels(true);
        cycleSlider.setPaintTicks(true);
        cycleSlider.setBorder(BorderFactory.createTitledBorder("Current Cycle"));
        
        controls.add(cycleSlider);
        cycleSlider.setBounds(0, 40, 400, 64);

        speedSlider = new JSlider(JSlider.HORIZONTAL, 50, 1050, 200);
        speedSlider.addChangeListener(this);
        speedSlider.setMajorTickSpacing(250);
        speedSlider.setMinorTickSpacing(50);
        speedSlider.setSnapToTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        controls.add(speedSlider);
        speedSlider.setBorder(BorderFactory.createTitledBorder("Play Speed (ms)"));
        speedSlider.setBounds(520, 40, 200, 64);
        
        runStopButton.addActionListener(this);
        controls.add(runStopButton);
        runStopButton.setEnabled(false);
        runStopButton.setBounds(336, 160, 80, 26);
        
        lblWait = new JLabel("Loading Sim File...");
        lblWait.setForeground(Color.BLUE);
        lblWait.setVisible(false);
        lblWait.setFont(new Font("Arial", Font.BOLD, 22));
        controls.add(lblWait);
        lblWait.setBounds(276, 120, 200, 26);
        
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
        
        lblWait.setVisible(true);
        int opt = jfc.showOpenDialog(this);
        
        if (opt != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        f = jfc.getSelectedFile();
        
        try {
            controller.load(f);
            runStopButton.setEnabled(true);
            cycleSlider.setEnabled(true);
        } catch (Exception e1) {
            showException(e1);
        }
        lblWait.setVisible(false);
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
        this.lblCurrentCycle.setText("Cycle " + c + " of " + m);
    }

    public void updateSliderBounds(Integer m) {
        cycleSlider.setMaximum(m);
    }

    public void updateSlider(Integer i) {
        cycleSlider.setValue(i);
    }

    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == cycleSlider) {
            if (cycleSlider.getValue() == 0) {
                cycleSlider.setValue(1);
            }
            this.controller.goTo(cycleSlider.getValue());
        }
        if (source == speedSlider) {
            this.controller.setPlaySpeed(speedSlider.getValue());
        }
    }
}
