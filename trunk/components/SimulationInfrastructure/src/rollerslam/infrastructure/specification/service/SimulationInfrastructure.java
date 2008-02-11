package rollerslam.infrastructure.specification.service;

import java.util.Set;

import rollerslam.infrastructure.specification.type.AgentID;

public abstract class SimulationInfrastructure {
	public SimulationAdmin simAdmin;
	public Set<Agent>      agent;
	
	public abstract Agent connectAgent(AgentID agentID);
}
