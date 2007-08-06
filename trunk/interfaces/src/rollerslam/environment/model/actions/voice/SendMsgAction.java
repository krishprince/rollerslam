package rollerslam.environment.model.actions.voice;

import rollerslam.environment.model.actions.VoiceAction;
import rollerslam.environment.model.utils.Fact;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class SendMsgAction extends VoiceAction {
	public Fact subject;
	
	public SendMsgAction(Agent a, Fact f){
		super(a);
		subject = f;
	}
}
