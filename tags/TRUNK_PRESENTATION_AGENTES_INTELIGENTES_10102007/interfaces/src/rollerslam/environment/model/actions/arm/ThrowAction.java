package rollerslam.environment.model.actions.arm;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.environment.model.actions.ArmAction;
import rollerslam.environment.model.utils.Vector;

@SuppressWarnings("serial")
public class ThrowAction extends ArmAction {
	public int ax;
	public int ay;
	
	public Vector acceleration;
	
	public ThrowAction(Agent a, int ax, int ay) {
		super(a);
		this.ax = ax;
		this.ay = ay;
		this.acceleration = new Vector(ax,ay);
	}
	
	public ThrowAction(Agent a, Vector acceleration){
		super(a);
		this.acceleration = acceleration;
		this.ax = acceleration.x;
		this.ay = acceleration.y;
	}
	
	public ThrowAction(Vector acceleration){
		this.acceleration = acceleration;
	}
	
	public String toString() {
		return "ThrowBall("+acceleration+")";
	}
	
}

