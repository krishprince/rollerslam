package rollerslam.test.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rollerslam.display.gui.RollerslamDisplay;
import rollerslam.display.gui.RollerslamMobileDisplay;
import rollerslam.environment.gui.ServerDisplay;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.logging.LogRecordingService;
import rollerslam.infrastructure.logging.LogRecordingServiceImpl;
import rollerslam.infrastructure.server.PrintTrace;

/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class RollerslamAllInOne extends JFrame implements ActionListener {

    private RollerslamDisplay rd = new RollerslamDisplay();
    private ServerDisplay sd;

    private JPanel pinf = new JPanel();
    private JTextField jtfClass = new JTextField("rollerslam.agent.goalbased.GoalBasedAgentTeam");
    private JButton jbInstantiate = new JButton("Init");

    public RollerslamAllInOne() {
    	LogRecordingService logger;
		try {
			logger = (LogRecordingService) LocateRegistry.getRegistry("localhost")
					.lookup(LogRecordingService.class.getSimpleName());
		} catch (Exception e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
			logger = null;
		}     	
    	sd = new ServerDisplay(logger);
        initComponents();
    }

    private void initComponents() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        getContentPane().add(rd);

        getContentPane().add(sd);

        jbInstantiate.addActionListener(this);

        pinf.add(jtfClass);
        pinf.add(jbInstantiate);

        getContentPane().add(pinf);
    }

    public static void main(String... args) throws Exception {
        new Thread(new Runnable() {
        	public void run() {
        		try {
        	    	LogRecordingServiceImpl.main(new String[0]);
				} catch (Exception e) {
					if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
				}
        	}
        }).start();

        Thread.sleep(5000);
    	
    	RollerslamAllInOne raio = new RollerslamAllInOne();
        raio.pack();
        raio.rd.main();
        raio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        raio.setVisible(true);

        Thread.sleep(5000);

        // this should be called only AFTER the server is completely initialized!!!                
        ClientFacade cli = ClientFacadeImpl.getInstance();
        cli.getClientInitialization().init("localhost");
        
        new Thread(new Runnable() {
        	public void run() {
        		try {
					RollerslamMobileDisplay.main(new String[]{"localhost"});
				} catch (Exception e) {
					if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
				}
        	}
        }).start();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbInstantiate) {
            try {
                jbInstantiateActionPerformed();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex);
            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void jbInstantiateActionPerformed() throws Exception {
        Class c = Class.forName(jtfClass.getText());
        Class[] mainArgType = {(new String[0]).getClass()};

        @SuppressWarnings(value = "unchecked")
        Method main = c.getMethod("mainAllInOne", mainArgType);
        main.invoke(null, new Object[]{null});
    }
}
