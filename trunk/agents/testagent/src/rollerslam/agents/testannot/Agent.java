package rollerslam.agents.testannot;

import rollerslam.infrastructure.client.agent;
import rollerslam.infrastructure.client.message;

public @agent interface Agent {
	
	@message void notifyAnswer(int val);
	
}
