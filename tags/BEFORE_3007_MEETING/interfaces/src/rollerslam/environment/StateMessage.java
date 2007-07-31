package rollerslam.environment;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.server.Message;

@SuppressWarnings("serial")
public class StateMessage implements Message {

	public World model;
	
	public StateMessage() {
	}

	public StateMessage(World m) {
		this.model = m;
	}
}