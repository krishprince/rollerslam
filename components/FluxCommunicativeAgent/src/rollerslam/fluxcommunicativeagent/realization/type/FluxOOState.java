package rollerslam.fluxcommunicativeagent.realization.type;

import java.util.Set;

import rollerslam.agent.communicative.specification.type.object.ObjectState;
import rollerslam.fluxinferenceengine.specification.type.Fluent;

public class FluxOOState extends ObjectState {
	public Set<Fluent> fluents;

	public FluxOOState(Set<Fluent> fluents) {
		super();
		this.fluents = fluents;
	}
	
	public String toString() {
		return "" + fluents;
	}
}
