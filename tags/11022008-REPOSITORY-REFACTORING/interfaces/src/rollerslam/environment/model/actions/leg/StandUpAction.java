package rollerslam.environment.model.actions.leg;

import rollerslam.environment.model.actions.LegAction;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class StandUpAction extends LegAction {
	public StandUpAction(Agent a){
		super(a);
	}	
	
	public StandUpAction(){
		
	}

	public String toString() {
		return "StandUp()";
	}
	
}
