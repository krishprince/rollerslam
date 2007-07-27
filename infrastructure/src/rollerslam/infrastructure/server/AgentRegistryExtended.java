package rollerslam.infrastructure.server;

import java.rmi.RemoteException;
import java.util.Set;

import rollerslam.infrastructure.agent.Agent;

public interface AgentRegistryExtended {
	/**
	 * @return the agents registered so far.
	 */
	Set<Agent> getRegisteredAgents() throws RemoteException;
}
