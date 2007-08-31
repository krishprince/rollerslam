package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class SentinelAction extends Message {
	public SentinelAction(Agent a) {
		super(a);
	}
	
	public SentinelAction(){
		
	}
}
