package rollerslam.infrastructure.logging;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Agent;

public interface Logging extends Remote {

	void log(Agent agent, LogData data) throws RemoteException;
	
}
