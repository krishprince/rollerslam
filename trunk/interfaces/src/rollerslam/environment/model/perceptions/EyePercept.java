package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class EyePercept extends Message {
	
	public EyePercept(Agent sender){
		super(sender);
	}
	
	public EyePercept(){
		
	}
}
