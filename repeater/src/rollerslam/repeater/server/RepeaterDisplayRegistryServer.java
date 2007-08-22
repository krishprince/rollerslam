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

	/**
	 * @see rollerslam.infrastructure.server.DisplayRegistry#register(rollerslam.infrastructure.display.Display)
	 */
	public void register(Display d) throws RemoteException {
		displays.add(d);		
	}

	/**
	 * @see rollerslam.infrastructure.server.DisplayRegistry#unregister(rollerslam.infrastructure.display.Display)
	 */
	public void unregister(Display d) throws RemoteException {
		displays.remove(d);		
	}

	/**
	 * @see rollerslam.infrastructure.server.DisplayRegistryExtended#getRegisteredDisplays()
	 */
	public Set<Display> getRegisteredDisplays() throws RemoteException {
		return displays;
	}

}
