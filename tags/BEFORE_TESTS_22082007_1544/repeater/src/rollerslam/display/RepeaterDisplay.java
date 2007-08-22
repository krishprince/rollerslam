package rollerslam.display;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JOptionPane;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.DisplayRegistryServer;
import rollerslam.repeater.server.RepeaterServer;

/**
* Essa classe representa um display, mas as mensagens enviadas pela simula��o
* s�o reencaminhadas para o conjunto de displays registrados pelo repetidor
* 
* @author Pablo
*/
@SuppressWarnings("serial")
public class RepeaterDisplay implements Display {
	
	private DisplayRegistryServer displayRegistry;
	
	public RepeaterDisplay(DisplayRegistryServer displayRegistry) {
		this.displayRegistry = displayRegistry;
	}

	/**
	 * @see rollerslam.infrastructure.server.Display#update(Message m)
	 */
	public void update(Message m) throws RemoteException {
		Vector<Display> toRemove = new Vector<Display>();
		
		for (Display display : displayRegistry.getRegisteredDisplays()) {			
			try{
				display.update(m);
			} catch(Exception e) {
				e.printStackTrace();
				toRemove.add(display);
			}
		}
		
		for (Display display : toRemove) {
			displayRegistry.unregister(display);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		RepeaterServer server = RepeaterServer.getInstance();
		try {
			String host = null;
			
			if (args.length > 0) {
				host = args[0];
			} else {
				host = JOptionPane.showInputDialog("Simulation:", "localhost");
			}
			
			server.init(host);			
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
	}	
}