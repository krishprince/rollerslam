package rollerslam.environment.model.actions.leg;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.environment.model.actions.LegAction;
import rollerslam.environment.model.utils.Vector;

@SuppressWarnings("serial")
public class KickAction extends LegAction {
	public int ax;
	public int ay;
	
	public Vector acceleration;
	
	public KickAction(Agent a, int ax, int ay) {
		super(a);
		this.ax = ax;
		this.ay = ay;
	}
	
	public KickAction(Agent a, Vector acceleration){
		super(a);
		this.acceleration = acceleration;
	}
	
	public KickAction(Vector acceleration){
		this.acceleration = acceleration;
	}
}

