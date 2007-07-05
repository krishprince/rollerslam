package rollerslam.infrastructure.server;

import rollerslam.infrastructure.agent.Agent;

public interface AgentRegistryExtended {
	/**
	 * @return the agents registered so far.
	 */
	Agent[] getRegisteredAgents();
}
