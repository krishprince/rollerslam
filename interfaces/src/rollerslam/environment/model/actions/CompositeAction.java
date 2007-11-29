package rollerslam.environment.model.actions;

import java.util.Collection;

import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class CompositeAction extends Message {
	private Collection<Message> actions;
	public CompositeAction(Collection<Message> actions) {
		super(null);
		this.actions = actions;
	}
	
	public Collection<Message> getActions() {
		return actions;
	}
}
