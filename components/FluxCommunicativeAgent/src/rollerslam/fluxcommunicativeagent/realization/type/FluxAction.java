package rollerslam.fluxcommunicativeagent.realization.type;

import rollerslam.fluxinferenceengine.specification.type.Action;
import rollerslam.infrastructure.specification.service.Message;

public class FluxAction extends Message {
	public Action action;

	public FluxAction(Action action) {
		super();
		this.action = action;
	}
}