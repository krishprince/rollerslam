package rollerslam.logplayer.gui;

import java.awt.Dimension;
import javax.swing.event.ChangeEvent;
import rollerslam.display.gui.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
    private JPanel pMiddle;
    
    private JLabel lblCurrentCycle;
    
    private JSlider cycleSlider;
    private JSlider speedSlider;
    
    private JButton runStopButton = new JButton("Run");
    
    private JEditorPane jep;
    
    private JComboBox agentTypeCbo;
    private JComboBox messageTypeCbo;
    private JButton btnShowMessages; 

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

        
        pMiddle = new JPanel();
        pMiddle.setLayout(null);
        Dimension mD = new Dimension(752, 200);
        pMiddle.setMinimumSize(mD);
        pMiddle.setMaximumSize(mD);
        pMiddle.setSize(mD);
        pMiddle.setPreferredSize(mD);
        
        JPanel controls = new JPanel();
        controls.setLayout(null);
        controls.setBorder(BorderFactory.createTitledBorder("Simulation Execution Controls"));
        
        lblCurrentCycle = new JLabel("Cycle 0 of 100.");
        controls.add(lblCurrentCycle);
        lblCurrentCycle.setBounds(10, 5, 360, 40);
        
                
        cycleSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 1);
        cycleSlider.setEnabled(false);
        cycleSlider.addChangeListener(this);
        cycleSlider.setMajorTickSpacing(100);
        cycleSlider.setPaintLabels(true);
        cycleSlider.setPaintTicks(true);
        cycleSlider.setBorder(BorderFactory.createTitledBorder("Current Cycle"));
        
        controls.add(cycleSlider);
        cycleSlider.setBounds(5, 40, 360, 64);

        speedSlider = new JSlider(JSlider.HORIZONTAL, 50, 1050, 200);
        speedSlider.addChangeListener(this);
        speedSlider.setMajorTickSpacing(250);
        speedSlider.setMinorTickSpacing(50);
        speedSlider.setSnapToTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setPaintTicks(true);
        controls.add(speedSlider);
        speedSlider.setEnabled(false);
        speedSlider.setBorder(BorderFactory.createTitledBorder("Play Speed (ms)"));
        speedSlider.setBounds(5, 124, 200, 64);
        
        runStopButton.addActionListener(this);
        controls.add(runStopButton);
        runStopButton.setEnabled(false);
        runStopButton.setBounds(242, 160, 80, 26);
        
        
        pMiddle.add(controls);
        controls.setBounds(0, 0, 370, 200);
        
        
        JPanel mPanel = new JPanel();
        mPanel.setLayout(null);
        mPanel.setBorder(BorderFactory.createTitledBorder("Messages"));
        
        
        agentTypeCbo = new JComboBox(new String[]{"All"});
        mPanel.add(agentTypeCbo);
        agentTypeCbo.setBorder(BorderFactory.createTitledBorder("Agent Filter"));
        agentTypeCbo.setBounds(5, 15, 120, 50);
        agentTypeCbo.setEnabled(false);
        
        messageTypeCbo = new JComboBox(new String[]{"All", "Agent Action", "Goal Update"});
        mPanel.add(messageTypeCbo);
        messageTypeCbo.setBorder(BorderFactory.createTitledBorder("Message Type Filter"));
        messageTypeCbo.setBounds(135, 15, 120, 50);
        messageTypeCbo.setEnabled(false);
        
        jep = new JEditorPane();
        JScrollPane scrollPane = new JScrollPane(jep);

        jep.setEditable(false);
        jep.setContentType("text/html");
        jep.setText("");
        mPanel.add(scrollPane);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Message Info"));
        scrollPane.setBounds(5, 70, 360, 120);
        
        btnShowMessages = new JButton("Show");
        mPanel.add(btnShowMessages);
        btnShowMessages.setBounds(270, 35, 70, 25);
        btnShowMessages.setEnabled(false);
        btnShowMessages.addActionListener(this);
        
        
        pMiddle.add(mPanel);
        mPanel.setBounds(376, 0, 370, 200);
        
        this.add(pMiddle);

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
            loadSimButton.setText("Please Wait!!!");
            loadSimButtonClick(e);
            loadSimButton.setText("Load Simulation");
            return;
        } else 
        if (o == runStopButton) {
            runStopButtonClick(e);
            return;
        }
        if (o == btnShowMessages) {
            
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
            runStopButton.setEnabled(true);
            cycleSlider.setEnabled(true);
            speedSlider.setEnabled(true);
            
            agentTypeCbo.setEnabled(true);
            messageTypeCbo.setEnabled(true);
            btnShowMessages.setEnabled(true);
        } catch (Exception e1) {
            showException(e1);
        }
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
