package rollerslam.repeater.server;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.DisplayRegistryServer;

/**
* 
* @author Pablo
*
*/
public class RepeaterDisplayRegistryServer implements DisplayRegistryServer {
	
	private Set<Display> displays = new HashSet<Display>();
	private DisplayRegistryObserver observer;

	public DisplayRegistryObserver getObserver() {
		return observer;
	}

	public void setObserver(DisplayRegistryObserver observer) {
		this.observer = observer;
	}

	/**
	 * @see rollerslam.infrastructure.server.DisplayRegistry#register(rollerslam.infrastructure.display.Display)
	 */
	public void register(Display d) throws RemoteException {
		System.out.println("ADDED " + d);
		displays.add(d);
		
		if (observer != null) {
			observer.notifyRegistered(d);
		}
	}

	/**
	 * @see rollerslam.infrastructure.server.DisplayRegistry#unregister(rollerslam.infrastructure.display.Display)
	 */
	public void unregister(Display d) throws RemoteException {
		displays.remove(d);		

		if (observer != null) {
			observer.notifyUnregistered(d);
		}
	}

	/**
	 * @see rollerslam.infrastructure.server.DisplayRegistryExtended#getRegisteredDisplays()
	 */
	public Set<Display> getRegisteredDisplays() throws RemoteException {
		return displays;
	}

}
