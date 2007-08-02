package rollerslam.infrastructure.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.Sensor;
import rollerslam.infrastructure.agent.SensorEffectorManager;

public class SensorEffectorManagerImpl implements SensorEffectorManager {

	private class AgentData {
		public Agent		agent    = null;
		public Set<Message> messages = new HashSet<Message>();

		public AgentData(Agent agent) {
			this.agent = agent;
		}
		
		public Sensor sensor = new Sensor() {
			public Set<Message> getActions() throws RemoteException {			
				Set<Message> ret = null;
				
				synchronized (messages) {
					ret = new HashSet<Message>();
					ret.addAll(messages);
					messages.clear();
				}
				return ret;
			}			
		};
		
		public Effector effector = new Effector() {
			public void doAction(Message m) throws RemoteException {
				synchronized (envMessages) {
					m.sender = agent;
					envMessages.add(m);
				}
			}			
		};
	};
	
	private HashMap<Agent, AgentData> buckets = new HashMap<Agent, AgentData>();
	private Set<Message> envMessages = new HashSet<Message>();
	
	private Sensor envSensor = new Sensor() {
		public Set<Message> getActions() throws RemoteException {
			Set<Message> ret = null;
			
			synchronized (envMessages) {
				ret = new HashSet<Message>();
				ret.addAll(envMessages);
				envMessages.clear();
			}
			return ret;
		}
		
	};
	
	private Effector envEffector = new Effector() {
		public void doAction(Message m) throws RemoteException {
			for (Agent agent : buckets.keySet()) {
				AgentData data = buckets.get(agent);
				
				if (data != null) {					
					synchronized (data.messages) {
						data.messages.add(m);
					}
				}
			}
		}		
	};
	
	public void registerAgent(Agent ag) throws RemoteException {		
		if (buckets.get(ag) == null) {
			AgentData data = new AgentData(ag);
			buckets.put(ag, data);
			
			try {
				data.effector = (Effector) ServerFacadeImpl.getInstance().exportObject(data.effector);
			} catch (AlreadyBoundException e) {
				e.printStackTrace();
			}

			try {
				data.sensor   = (Sensor) ServerFacadeImpl.getInstance().exportObject(data.sensor);
			} catch (AlreadyBoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void unregisterAgent(Agent ag) throws RemoteException {
		buckets.put(ag, null);		
	}
	
	public Effector getAgentEffector(Agent ag) throws RemoteException {
		AgentData data = buckets.get(ag);
		if (data != null) {
			return data.effector;
		}
		return null;
	}

	public Sensor getAgentSensor(Agent ag) throws RemoteException {
		AgentData data = buckets.get(ag);
		if (data != null) {
			return data.sensor;
		}
		return null;
	}

	public Effector getEnvironmentEffector() throws RemoteException {
		return envEffector;
	}

	public Sensor getEnvironmentSensor() throws RemoteException {
		return envSensor;
	}

}
