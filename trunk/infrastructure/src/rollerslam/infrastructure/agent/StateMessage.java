package rollerslam.infrastructure.agent;


@SuppressWarnings("serial")
public class StateMessage implements Message {

	public EnvironmentStateModel model;
	
	public StateMessage() {
	}

	public StateMessage(EnvironmentStateModel m) {
		this.model = m;
	}
}
