package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class GameStartedPerception extends Message {
	
	public Agent receiver;
	public int playerID;
	public GameStartedPerception(Agent sender, Agent receiver, int playerID) {
		super(sender);
		this.receiver = receiver;
		this.playerID = playerID;
	}
	
	public GameStartedPerception(Agent receiver, int playerID) {
		this.receiver = receiver;
		this.playerID = playerID;
	}
}
