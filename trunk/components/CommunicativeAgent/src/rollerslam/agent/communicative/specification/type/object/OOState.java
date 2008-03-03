package rollerslam.agent.communicative.specification.type.object;

import java.util.HashMap;

public class OOState {

	private HashMap<OID, WorldObject> objects = new HashMap<OID, WorldObject>();

	public HashMap<OID, WorldObject> getObjects() {
		return objects;
	}

	public void setObjects(HashMap<OID, WorldObject> objects) {
		this.objects = objects;
	}

	public String toString() {
		return "" + objects;
	}
}
