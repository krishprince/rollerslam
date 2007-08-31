package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class BodyPercept extends Message {
	public BodyPercept(Agent sender){
		super(sender);
	}
	
	public BodyPercept(){
		
	}
}
