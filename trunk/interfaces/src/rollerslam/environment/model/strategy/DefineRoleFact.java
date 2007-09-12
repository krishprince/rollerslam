package rollerslam.environment.model.strategy;

import java.io.Serializable;

import rollerslam.environment.model.utils.Vector;

@SuppressWarnings("serial")
public class DefineRoleFact implements Serializable {
	public PlayerPosition position = null;
	public AgentRole role = null;
	public Vector posCoord = null;
	
	public String toString() {
		return "Your position is " + position + " your role is " + role
				+ " and your coord is " + posCoord;
	}
}
