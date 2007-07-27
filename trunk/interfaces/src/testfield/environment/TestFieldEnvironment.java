package testfield.environment;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;
import rollerslam.infrastructure.server.EnvironmentCycleProcessor;

public @agent interface TestFieldEnvironment extends Serializable, EnvironmentCycleProcessor, Remote {

	@message void dash(Agent agent, int aX, int aY) throws RemoteException;
	
}
