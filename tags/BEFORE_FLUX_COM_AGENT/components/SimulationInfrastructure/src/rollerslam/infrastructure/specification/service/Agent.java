package rollerslam.infrastructure.specification.service;

import java.util.Set;

import rollerslam.infrastructure.specification.type.AgentID;

public interface Agent {

	AgentID      getAgentID();
	void         sendActions(Set<Message> actions);
	Set<Message> getPerceptions();
	SimulationState getSimulationState();
	
}
