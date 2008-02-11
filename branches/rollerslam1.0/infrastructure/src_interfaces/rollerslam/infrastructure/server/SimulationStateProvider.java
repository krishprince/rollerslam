package rollerslam.infrastructure.server;

import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Message;


public interface SimulationStateProvider extends SimulationAdmin {	
	/**
	 * @return a message containing the current environment state this message is sent
	 * to the registered monitors.
	 * 
	 * @throws RemoteException
	 */
	Message getEnvironmentState() throws RemoteException;
}
