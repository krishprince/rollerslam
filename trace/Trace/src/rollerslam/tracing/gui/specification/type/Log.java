/**
 * 
 */
package rollerslam.tracing.gui.specification.type;

import rollerslam.infrastructure.specification.type.AgentID;

/**
 * @author Rafael Oliveira
 * 
 */
public abstract class Log{

	private AgentID agentID;
	private String additionInfo;
	
	public Log(AgentID agentID, String additionInfo) {
		this.agentID = agentID;
		this.additionInfo = additionInfo;
	}
	
	
	public AgentID getAgentID() {
		return agentID;
	}
	public void setAgentID(AgentID agentID) {
		this.agentID = agentID;
	}
	public String getAdditionInfo() {
		return additionInfo;
	}
	public void setAdditionInfo(String additionInfo) {
		this.additionInfo = additionInfo;
	}

	
}
