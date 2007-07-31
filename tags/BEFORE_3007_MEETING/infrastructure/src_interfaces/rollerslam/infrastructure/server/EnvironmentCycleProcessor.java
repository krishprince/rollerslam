package rollerslam.infrastructure.server;

import java.rmi.RemoteException;


public interface EnvironmentCycleProcessor {
	/**
	 * This method is called each cycle. At this method all the bufferized messages should be processed.
	 * The perceptions of the agents should be generated and sent to the agents.
	 * 
	 * @throws RemoteException
	 */
	void think() throws RemoteException;
	
	/**
	 * @return a message containing the current environment state this message is sent
	 * to the registered monitors.
	 * 
	 * @throws RemoteException
	 */
	Message getEnvironmentState() throws RemoteException;
}
