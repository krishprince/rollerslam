package rollerslam.fluxcommunicativeagent.realization.type;

import rollerslam.fluxinferenceengine.specification.type.Action;
import rollerslam.infrastructure.specification.service.Message;

public class FluxAction extends Message {

	private Action action;

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public FluxAction(Action action) {
		super();
		this.action = action;
	}
	
	public String toString() {
		return super.toString();
	}
}
