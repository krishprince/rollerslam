package rollerslam.environment.model;

import rollerslam.infrastructure.server.Message;

public class StateMessage implements Message {
	public World state;
	
	public StateMessage(World w) {
		this.state = w;
	}
}
