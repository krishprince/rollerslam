/**
 * 
 */
package rollerslam.display.gui;

import java.rmi.RemoteException;

import rollerslam.display.gui.mvc.Controller;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.mvc.View;
import rollerslam.environment.StateMessage;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.Message;

/**
 * @author Marcos Aurélio
 *
 */
public class ControllerImpl implements Controller {
    ClientFacade facade = ClientFacadeImpl.getInstance();
    View 		 view   = null;
    Model		 model  = null;
    
    public ControllerImpl(View view, Model model) {
    	this.view = view;
    	this.model = model;
    }
    
	/**
	 * @see rollerslam.display.gui.mvc.Controller#connect(java.lang.String)
	 */
	public void connect(String host) throws Exception {
		facade.init(host);
		facade.getDisplayRegistry().register((Display) facade.exportObject(new Display() {

			public void update(Message m) throws RemoteException {
				if (m instanceof StateMessage) {
					ControllerImpl.this.model.setModel(((StateMessage)m).model);
				}
			}
			
		}));		
	}

	/**
	 * @see rollerslam.display.gui.mvc.Controller#startSimulation()
	 */
	public void startSimulation() throws Exception {
		try {
			facade.getSimulationAdmin().run();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}					
	}

	/**
	 * @see rollerslam.display.gui.mvc.Controller#stopSimulation()
	 */
	public void stopSimulation() throws Exception {
		try {
			facade.getSimulationAdmin().stop();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}					
	}

}
