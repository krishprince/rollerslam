package rollerslam.infrastructure.specification.service;

import java.util.Set;

import rollerslam.infrastructure.specification.type.AgentID;

public abstract class SimulationInfrastructure {

	private SimulationAdmin simAdmin;
	private Set<Agent> agent;

	public abstract Agent connectAgent(AgentID agentID);

	public SimulationAdmin getSimAdmin() {
		return simAdmin;
	}

	public void setSimAdmin(SimulationAdmin simAdmin) {
		this.simAdmin = simAdmin;
	}

	public Set<Agent> getAgent() {
		return agent;
	}

	public void setAgent(Set<Agent> agent) {
		this.agent = agent;
	}

}
