package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;


@SuppressWarnings("serial")
public class VoiceAction extends Message {
	public VoiceAction(Agent a) {
		super(a);
	}
	
	public VoiceAction(){
		
	}
}