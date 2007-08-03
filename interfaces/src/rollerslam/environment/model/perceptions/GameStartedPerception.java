package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class GameStartedPerception extends Message {
	
	public int playerID;
	public GameStartedPerception(Agent sender, int playerID) {
		super(sender);
		this.playerID = playerID;
	}

}
