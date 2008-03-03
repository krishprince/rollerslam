package rollerslam.agent.communicative.specification.type.action;

import java.util.HashSet;
import java.util.Set;

import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.infrastructure.specification.service.Message;

public class TellAction extends Message {

	private Set<WorldObject> objects = new HashSet<WorldObject>();

	public String toString() {
		return super.toString() + " # TELL_ACTION [" + objects + "]";
	}

	public Set<WorldObject> getObjects() {
		return objects;
	}

	public void setObjects(Set<WorldObject> objects) {
		this.objects = objects;
	}

}
