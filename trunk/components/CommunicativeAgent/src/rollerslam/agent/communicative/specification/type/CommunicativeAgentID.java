package rollerslam.agent.communicative.specification.type;

import rollerslam.infrastructure.specification.type.AgentID;


public class CommunicativeAgentID extends AgentID {
	private String myID;
	
	public CommunicativeAgentID(String id) {
		this.myID = id;
	}
	
	public String toString() {
		return "" + myID;
	}
}
