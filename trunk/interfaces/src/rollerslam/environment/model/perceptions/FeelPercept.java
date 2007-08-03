package rollerslam.environment.model.perceptions;

import rollerslam.environment.model.utils.Vector;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class FeelPercept extends BodyPercept {
	public boolean hasBall;
	public Vector position;
	public Vector velocity;
	public Vector acceleration;
	
	public FeelPercept(Agent sender,  boolean hasBall, Vector position, 
			Vector velocity, Vector acceleration){
		super(sender);
	}
}
