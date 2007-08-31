package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class LegAction extends Message {
	public LegAction(Agent a) {
		super(a);
	}
	
	public LegAction(){
		
	}
}
