package rollerslam.agent.communicative.specification.type.object;

import java.util.HashMap;

public class OOState {
	public HashMap<OID, WorldObject> objects = new HashMap<OID, WorldObject>();
	
	public String toString() {
		return "" + objects;
	}
}
