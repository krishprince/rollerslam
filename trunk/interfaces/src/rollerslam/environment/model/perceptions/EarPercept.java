package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class EarPercept extends Message {
	public EarPercept(Agent sender){
		super(sender);
	}
	
	public EarPercept(){
		
	}
}
