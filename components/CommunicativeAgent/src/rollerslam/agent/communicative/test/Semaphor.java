package rollerslam.agent.communicative.test;

import rollerslam.agent.communicative.specification.type.fluent.FluentState;


public class Semaphor extends FluentState {
	public int value = 0;
	
	public String toString() {
		return "value->"+value;
	}
}
