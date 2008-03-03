package rollerslam.agent.communicative.specification.service;

import java.util.HashMap;

import rollerslam.agent.communicative.specification.type.object.OOState;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.SimulationInfrastructure;
import rollerslam.infrastructure.specification.type.AgentID;

public abstract class CommunicativeAgent {

	private Agent agent;
	private HashMap<AgentID, OOState> agentKB;
	private OOState kb;

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public HashMap<AgentID, OOState> getAgentKB() {
		return agentKB;
	}

	public void setAgentKB(HashMap<AgentID, OOState> agentKB) {
		this.agentKB = agentKB;
	}

	public OOState getKb() {
		return kb;
	}

	public void setKb(OOState kb) {
		this.kb = kb;
	}

	public abstract void setSimulationInfrastructure(
			SimulationInfrastructure simulation);

}
