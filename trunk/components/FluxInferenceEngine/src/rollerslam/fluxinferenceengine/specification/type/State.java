package rollerslam.fluxinferenceengine.specification.type;

import java.util.Arrays;

public class State {
	public Fluent[] fluents;

	public State(Fluent[] fluents) {
		super();
		this.fluents = fluents;
	}
	
	public State() {
		this(new Fluent[0]);
	}
	
	public String toString() {
		return Arrays.toString(fluents);
	}
}
