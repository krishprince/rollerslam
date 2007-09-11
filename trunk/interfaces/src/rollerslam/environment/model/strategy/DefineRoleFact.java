package rollerslam.environment.model.strategy;

import java.io.Serializable;

import rollerslam.environment.model.utils.Vector;

@SuppressWarnings("serial")
public class DefineRoleFact implements Serializable {
	public PlayerPosition position = null;
	public AgentRole role = null;
	public Vector posCoord = null;
}
