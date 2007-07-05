package rollerslam.infrastructure.server;

import rollerslam.agents.Agent;

public interface AgentRegistryExtended {
	/**
	 * @return the agents registered so far.
	 */
	Agent[] getRegisteredAgents();
}
