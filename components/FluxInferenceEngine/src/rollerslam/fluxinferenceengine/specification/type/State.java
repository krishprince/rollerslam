package rollerslam.fluxinferenceengine.specification.type;

import java.util.Arrays;

public class State {

	private Fluent[] fluents;

	public Fluent[] getFluents() {
		return fluents;
	}

	public void setFluents(Fluent[] fluents) {
		this.fluents = fluents;
	}

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
