package rollerslam.environment.model.actions;

import rollerslam.environment.model.utils.Vector;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class HitAction extends KickAction {
		
	public HitAction(Agent a, int ax, int ay) {
		super(a, ax, ay);
	}
	
	public HitAction(Agent a, Vector acceleration){
		super(a, acceleration);
	}	
}
