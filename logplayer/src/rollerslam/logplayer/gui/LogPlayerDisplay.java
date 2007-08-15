package rollerslam.logplayer.gui;

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
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.mvc.View;
import rollerslam.logplayer.gui.mvc.Controller;


/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class LogPlayerDisplay extends JPanel implements View, ActionListener {

    private Controller controller = null;

    private JButton loadSimButton = new JButton("Load Simulation");
    private JLabel  messages      = new JLabel("");
    
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
        if (e.getSource() == loadSimButton) {
            loadSimButtonClick(e);
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

    private void loadSimButtonClick(ActionEvent e) {
        File f = null;
        
        JFileChooser jfc = new JFileChooser();
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
    }

}
