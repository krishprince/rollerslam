/*
 * ServerDisplay.java
 *
 * Created on 05/08/2007, 22:12:15
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.server.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import rollerslam.environment.RollerslamEnvironmentAgent;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.infrastructure.server.SimulationState;

/**
 *
 * @author Weslei
 */
public class ServerDisplay extends JPanel implements ActionListener {

    private JButton startSimulationButton = new JButton("Start");
    private JLabel statusLabel = new JLabel("...");
    private ServerFacade serverFacade = null;

    public ServerDisplay() {
        serverFacade = ServerFacadeImpl.getInstance();

        initComponents();
    }

    private void updateStatusLabel() {
        try {
            while ((serverFacade == null) || (serverFacade.getSimulationAdmin() == null)) {
                Thread.sleep(10);
            }
            if ((serverFacade != null) && (serverFacade.getSimulationAdmin() != null)) {
                statusLabel.setText(serverFacade.getSimulationAdmin().getState().toString());
                return;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not get simulation state.. Details:\n\n" + ex);
        }
    }

    private void initComponents() {
        this.setLayout(new GridLayout(2, 1));

        startSimulationButton.addActionListener(this);

        JPanel top = new JPanel();

        top.add(statusLabel);

        this.add(top);

        JPanel botton = new JPanel();

        botton.add(startSimulationButton);

        this.add(botton);
        int w = 120;
        int h = 80;
        setPreferredSize(new java.awt.Dimension(w, h));
        setMinimumSize(new java.awt.Dimension(w, h));
        setSize(new java.awt.Dimension(w, h));

        new Thread() {

            public void run() {
                try {
                    serverFacade.getServerInitialization().init(1099, new RollerslamEnvironmentAgent());
                } catch (Exception ex) {
                    throw new RuntimeException("Error obtaining remote data... Details:\n\n" + ex);
                }
            }
        }.start();

        updateStatusLabel();
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        jf.getContentPane().setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
        jf.getContentPane().add(new ServerDisplay());

        jf.pack();

        jf.setResizable(false);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startSimulationButton) {
            try {
                startSimulationButtonActionPerformed();
            } catch (Exception ex) {
                throw new RuntimeException("Start" + ex);
            }
        }
    }

    private void startSimulationButtonActionPerformed() throws Exception {
        SimulationState ss = serverFacade.getSimulationAdmin().getState();
        if ((ss == SimulationState.CREATED) || (ss == SimulationState.STOPPED)) {
            serverFacade.getSimulationAdmin().run();
            updateStatusLabel();
            startSimulationButton.setText("Stop");
        } else if (startSimulationButton.getText().equals("Stop")) {
            serverFacade.getSimulationAdmin().stop();
            updateStatusLabel();
            startSimulationButton.setText("Start");
        }
    }
}
