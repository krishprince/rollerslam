package rollerslam.test.general;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.rmi.RemoteException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import rollerslam.display.gui.RollerslamDisplay;
import rollerslam.environment.gui.ServerDisplay;
import rollerslam.infrastructure.client.ClientFacadeImpl;

/**
 *
 * @author Weslei
 */
@SuppressWarnings("serial")
public class RollerslamAllInOne extends JFrame implements ActionListener {

    private RollerslamDisplay rd = new RollerslamDisplay();
    private ServerDisplay sd = new ServerDisplay();

    private JPanel pinf = new JPanel();
    private JTextField jtfClass = new JTextField("rollerslam.agent.goalbased.GoalBasedAgentTeam");
    private JButton jbInstantiate = new JButton("Init");

    public RollerslamAllInOne() {
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

    public static void main(String... args) throws RemoteException {    	
        RollerslamAllInOne raio = new RollerslamAllInOne();
        raio.pack();
        raio.rd.main();
        raio.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        raio.setVisible(true);
        
		ClientFacadeImpl.getInstance().getClientInitialization().init("localhost");
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

    @SuppressWarnings("unchecked")
	private void jbInstantiateActionPerformed() throws Exception {
        Class c = Class.forName(jtfClass.getText());
        Class[] mainArgType = {(new String[0]).getClass()};
        
        @SuppressWarnings("unchecked")
        Method main = c.getMethod("mainAllInOne", mainArgType);
        main.invoke(null, new Object[] {null});
    }
}
