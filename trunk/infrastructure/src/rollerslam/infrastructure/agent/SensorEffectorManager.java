package rollerslam.infrastructure.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SensorEffectorManager extends Remote {

	void registerAgent(Agent ag) throws RemoteException;
	
	void unregisterAgent(Agent ag) throws RemoteException;
	
	Effector getAgentEffector(Agent ag) throws RemoteException ;

	Sensor getAgentSensor(Agent ag) throws RemoteException ;

	Sensor getEnvironmentSensor() throws RemoteException ;

	Effector getEnvironmentEffector() throws RemoteException ;

}
