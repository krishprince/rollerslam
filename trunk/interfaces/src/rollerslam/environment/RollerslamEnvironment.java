package rollerslam.environment;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;
import rollerslam.infrastructure.server.EnvironmentCycleProcessor;

public @agent interface RollerslamEnvironment extends Serializable, EnvironmentCycleProcessor, Remote {
	@message void dash(int agentID, int aX, int aY) throws RemoteException;	
}
