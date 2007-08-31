package rollerslam.environment.model.actions.sentinel;

import rollerslam.environment.model.actions.SentinelAction;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class KillPlayerAction extends SentinelAction {
	public KillPlayerAction(Agent a){
		super(a);
	}
	
	public KillPlayerAction(){
		
	}
}
