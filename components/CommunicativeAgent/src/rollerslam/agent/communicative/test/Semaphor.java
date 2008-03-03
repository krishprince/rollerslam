package rollerslam.agent.communicative.test;

import rollerslam.agent.communicative.specification.type.object.ObjectState;


public class Semaphor extends ObjectState {

	private int value = 0;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String toString() {
		return "value->"+value;
	}
}
