package rollerslam.infrastructure.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.server.SimulationStateProvider;

public interface EnvironmentAgent extends Agent {

	SimulationStateProvider getSimulationStateProvider() throws RemoteException;
			
}
