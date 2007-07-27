/*
 * ServerGUI.java
 *
 */

package rollerslam.display.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.display.Display;
import tictactoe.display.TicTacToeDisplay;

/**
 *
 * @author Weslei
 */
public class ServerGUI extends JFrame {

	private JButton startButton = new JButton("Start Simulation");
	private JButton stopButton  = new JButton("Stop Simulation");
	
    public ServerGUI() {
        try {
        	getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            
            // get hold the content of the frame and set up the resolution of the game
            javax.swing.JPanel panel = new JPanel();
            panel.setPreferredSize(new java.awt.Dimension(800, 600));
            panel.setMinimumSize(new java.awt.Dimension(800, 600));
            panel.setSize(new java.awt.Dimension(800, 600));
            
            panel.setLayout(null);
            rollerslam.display.gui.Game g = new rollerslam.display.gui.Game();

            final ClientFacade facade = ClientFacadeImpl.getInstance();
    		facade.getDisplayRegistry().register((Display) facade.exportObject(new TicTacToeDisplay()));		

            panel.add(g);

            getContentPane().add(panel);
            
            JPanel down = new JPanel();
            down.setLayout(new FlowLayout());
            down.add(startButton);
            down.add(stopButton);

            startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						facade.getSimulationAdmin().run();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}					
				}            	
            });

            stopButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						facade.getSimulationAdmin().stop();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}					
				}            	
            });
            
            getContentPane().add(down);
            
            // finally make the window visible
            pack();
            setResizable(false);

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
            
            g.init();
        } catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
