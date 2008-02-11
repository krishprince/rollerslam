package rollerslam.agent.communicative.specification.type.action;

import java.util.HashSet;
import java.util.Set;

import rollerslam.agent.communicative.specification.type.fluent.FluentObject;
import rollerslam.infrastructure.specification.service.Message;

public class TellAction extends Message {
	public Set<FluentObject> objects = new HashSet<FluentObject>();
	
	public String toString() {
		return super.toString() + " # TELL_ACTION [" + objects + "]";
	}
	
}
