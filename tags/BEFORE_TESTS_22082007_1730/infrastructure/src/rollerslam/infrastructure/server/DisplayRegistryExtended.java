package rollerslam.infrastructure.server;

import java.rmi.RemoteException;
import java.util.Set;

import rollerslam.infrastructure.display.Display;

/**
 * This class contains the services provided by the DisplayRegistry that are not
 * published to RMI clients
 * 
 * @author maas
 */
public interface DisplayRegistryExtended {
	/**
	 * @return the current list of registered displays
	 */
	Set<Display> getRegisteredDisplays() throws RemoteException;
}
