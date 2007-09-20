package rollerslam.environment.model.actions.voice;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.actions.VoiceAction;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class SendMsgAction extends VoiceAction {
	public Fact subject;
	
	public SendMsgAction(Agent a, Fact f){
		super(a);
		subject = f;
	}
	
	public SendMsgAction(Fact f){
		subject = f;
	}
	
	public String toString() {
		return "SendMsg("+subject+")";
	}
	
}
