package rollerslam.infrastructure.agent;


@SuppressWarnings("serial")
public class StateMessage extends Message {

	public EnvironmentStateModel model;
	
	public StateMessage(Agent sender) {
		super(sender);
	}

	public StateMessage(Agent sender, EnvironmentStateModel m) {
		super(sender);
		this.model = m;
	}
}
