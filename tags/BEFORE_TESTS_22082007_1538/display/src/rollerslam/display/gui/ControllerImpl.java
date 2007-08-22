/**
 *
 */
package rollerslam.display.gui;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.Vector;

import rollerslam.display.gui.mvc.Controller;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.mvc.View;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.display.Display;

/**
 * @author Marcos Aurélio
 *
 */
public class ControllerImpl implements Controller {

    ClientFacade facade = ClientFacadeImpl.getInstance();
    View view = null;
    Model model = null;

    public ControllerImpl(View view, Model model) {
        this.view = view;
        this.model = model;
        
        try {
			facade.getServiceDiscoverer().findServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * @see rollerslam.display.gui.mvc.Controller#connect(java.lang.String)
     */
    public void connect(String host) throws Exception {
        facade.getClientInitialization().init(host);
        facade.getDisplayRegistry().register(
                (Display) facade.getClientInitialization().exportObject(
                     new Display() {
                         public void update(Message m) throws RemoteException {
                             if (m instanceof StateMessage) {
                                 ControllerImpl.this.model.setModel((World) ((StateMessage) m).model);
                             }
                         }
                     }
                )
            );
    }

	/**
	 * @see rollerslam.display.gui.mvc.Controller#getAvailableHosts()
	 */
	public Vector<String> getAvailableHosts() throws Exception {
		Vector<String> ret = new Vector<String>();
		for (InetAddress addr : facade.getServiceDiscoverer().getFoundAddresses()) {
			ret.add(addr.getHostAddress());
		}
		return ret;
	}
    
    
}
