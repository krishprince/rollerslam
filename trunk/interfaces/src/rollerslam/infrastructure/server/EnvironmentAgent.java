package rollerslam.infrastructure.server;

import java.rmi.RemoteException;

import rollerslam.agents.Agent;

/**
 * This agent contains the environment. It should receive the messages sent by
 * the agents in simulation through the {@link Agent#sendPerception(Message)} method.
 * 
 * The messages should be bufferized and processed when the method {@link EnvironmentAgent#think()}
 * is called.
 * 
 * @author maas
 */
public interface EnvironmentAgent extends Agent {

	/**
	 * This method is called each cycle. At this method all the bufferized messages should be processed.
	 * The perceptions of the agents should be generated and sent to the agents.
	 * 
	 * @throws RemoteException
	 */
	void think() throws RemoteException;

}