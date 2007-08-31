package rollerslam.environment.model.perceptions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.environment.model.Fact;

@SuppressWarnings("serial")
public class RecvMsgPercept extends EarPercept {
	public Fact fact;
	
	public RecvMsgPercept(Agent sender, Fact fact){
		super(sender);
		this.fact = fact;
	}

	public RecvMsgPercept(Fact fact){
		this.fact = fact;
	}
}
