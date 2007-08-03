package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.environment.model.World;

@SuppressWarnings("serial")
public class SeePerception extends EyePercept {
	public World envModel;
	
	public SeePerception(Agent sender, World envModel){
		super(sender);
		this.envModel = envModel;
	}
	
}
