package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class ArmAction extends Message {
	public ArmAction(Agent a) {
		super(a);
	}
	
	public ArmAction(){
		
	}
}
