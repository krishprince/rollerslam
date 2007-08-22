package rollerslam.display.gui;

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
    private JLabel  messages      = new JLabel("");
    
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
        down.add(connectButton);

        connectButton.addActionListener(this);

        this.add(down);
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

        if (PrintTrace.TracePrint){
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, e1.getMessage());
    }

    public static void main(String[] args) {
        RollerslamDisplay panel = new RollerslamDisplay();
        
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

    private void connectButtonClick(ActionEvent e) {
    	Vector<String> options = new Vector<String>();
    	
    	try {
			options.addAll(controller.getAvailableHosts());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		String addr = "";
    	
		if (options.isEmpty()) {
    		addr = JOptionPane.showInputDialog("Simulation URL:", "localhost");
    	} else {
    		if (options.size() == 1) {
    			addr = options.firstElement();
    		} else {
    			int opc = JOptionPane
						.showOptionDialog(this, "Choose simulation host:", this
								.getName(), JOptionPane.DEFAULT_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options
										.toArray(new String[0]), options
										.firstElement());
    			
    			if (opc != JOptionPane.CLOSED_OPTION) {
    				addr = options.elementAt(opc);
    			}
    		}
    	}
		
		System.out.println("CONNECTING TO " + addr);
		
        try {
            controller.connect(addr);
        } catch (Exception e1) {
            showException(e1);
        }
    }

}
