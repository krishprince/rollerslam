package test.environment;


import java.io.Serializable;
import java.rmi.RemoteException;

import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;
import rollerslam.infrastructure.server.EnvironmentCycleProcessor;

public @agent interface TestEnvironment extends Serializable, EnvironmentCycleProcessor {

	@message void notifyValue(int val) throws RemoteException;
	
}
