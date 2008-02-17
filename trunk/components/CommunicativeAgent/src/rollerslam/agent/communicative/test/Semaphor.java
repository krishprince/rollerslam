package rollerslam.agent.communicative.test;

import rollerslam.agent.communicative.specification.type.object.ObjectState;


public class Semaphor extends ObjectState {
	public int value = 0;
	
	public String toString() {
		return "value->"+value;
	}
}
