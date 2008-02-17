package rollerslam.agent.communicative.specification.service;

import java.util.HashMap;

import rollerslam.agent.communicative.specification.type.object.OOState;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.SimulationInfrastructure;
import rollerslam.infrastructure.specification.type.AgentID;

public abstract class CommunicativeAgent {	
	public Agent 		    					agent;
	public abstract void 						setSimulationInfrastructure(SimulationInfrastructure simulation);
	
	public HashMap<AgentID, OOState>			agentKB;
	public OOState								kb;
	
}
