package rollerslam.environment.model.actions.sentinel;

import rollerslam.environment.model.actions.SentinelAction;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class CheckAliveAction extends SentinelAction {
	public CheckAliveAction(Agent a){
		super(a);
	}
}
