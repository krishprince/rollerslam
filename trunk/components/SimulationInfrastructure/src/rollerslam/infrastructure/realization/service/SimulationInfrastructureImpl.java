package rollerslam.infrastructure.realization.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.service.SimulationAdmin;
import rollerslam.infrastructure.specification.service.SimulationInfrastructure;
import rollerslam.infrastructure.specification.service.SimulationState;
import rollerslam.infrastructure.specification.type.AgentID;

public class SimulationInfrastructureImpl extends SimulationInfrastructure
		implements SimulationAdmin {

	protected Object token = new Object();
	protected SimulationState state;
	protected HashMap<AgentID, Set<Message>> msgs;

	public SimulationInfrastructureImpl() {
		state = SimulationState.INITIALIZED;
		this.setSimAdmin(this);
		msgs = new HashMap<AgentID, Set<Message>>();
		this.setAgent(new HashSet<Agent>());
	}

	public SimulationState getState() {
		return state;
	}

	public void setState(SimulationState s) {
		this.state = s;
	}

	public Agent connectAgent(AgentID agentID) {
		AgentImpl agentConnector = new AgentImpl(this, agentID);
		msgs.put(agentID, new HashSet<Message>());
		this.getAgent().add(agentConnector);

		return agentConnector;
	}
}
