package rollerslam.environment.model.actions.leg;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.environment.model.actions.LegAction;
import rollerslam.environment.model.utils.Vector;

@SuppressWarnings("serial")
public class DashAction extends LegAction {

	public Vector acceleration;
	
	public DashAction(Agent a, Vector acceleration){
		super(a);
		this.acceleration = acceleration;
	}
	
	public DashAction(Vector acceleration){
		this.acceleration = acceleration;
	}
}
