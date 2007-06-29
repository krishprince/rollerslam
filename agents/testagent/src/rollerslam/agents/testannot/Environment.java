package rollerslam.agents.testannot;

import rollerslam.infrastructure.client.agent;
import rollerslam.infrastructure.client.message;

public @agent interface Environment {

	@message void notifyQuestion(int val);
	
}
