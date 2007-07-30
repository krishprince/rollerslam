package rollerslam.environment;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;
import rollerslam.infrastructure.server.EnvironmentCycleProcessor;

public @agent interface RollerslamEnvironment extends Serializable, EnvironmentCycleProcessor, Remote {
	/**
	 * Gets a player "body" to an agent
	 * 
	 * @param playerTeam
	 * @throws RemoteException
	 */
	@message void joinWorld(Agent agent, PlayerTeam playerTeam) throws RemoteException;	

	/**
	 * Changes player acceleration
	 * 
	 * @param agentID
	 * @param aX
	 * @param aY
	 * @throws RemoteException
	 */
	@message void dash(int agentID, int aX, int aY) throws RemoteException;	
}
