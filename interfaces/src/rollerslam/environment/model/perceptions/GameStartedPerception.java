package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class GameStartedPerception extends Message {

	public GameStartedPerception(Agent sender) {
		super(sender);
	}

}
