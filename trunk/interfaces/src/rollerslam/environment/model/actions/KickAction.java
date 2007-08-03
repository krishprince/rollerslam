package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
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
}

