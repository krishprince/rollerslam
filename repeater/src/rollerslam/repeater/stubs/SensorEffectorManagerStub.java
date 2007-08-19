package rollerslam.repeater.stubs;
import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Sensor;
import rollerslam.infrastructure.agent.SensorEffectorManager;


public class SensorEffectorManagerStub implements SensorEffectorManager {
	public SensorEffectorManagerStub() {}

	public Effector getAgentEffector(Agent ag) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Sensor getAgentSensor(Agent ag) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Effector getEnvironmentEffector() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Sensor getEnvironmentSensor() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerAgent(Agent ag) throws RemoteException {
		// TODO Auto-generated method stub

	}

	public void unregisterAgent(Agent ag) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
