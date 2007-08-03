package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.environment.model.utils.Vector;

@SuppressWarnings("serial")
public class DashAction extends LegAction {
	public int ax;
	public int ay;
	
	public Vector acceleration;
	
	public DashAction(Agent a, int ax, int ay) {
		super(a);
		this.ax = ax;
		this.ay = ay;
	}
	
	public DashAction(Agent a, Vector acceleration){
		super(a);
		this.acceleration = acceleration;
	}
}
