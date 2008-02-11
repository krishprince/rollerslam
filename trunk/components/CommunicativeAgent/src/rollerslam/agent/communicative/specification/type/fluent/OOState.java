package rollerslam.agent.communicative.specification.type.fluent;

import java.util.HashMap;

public class OOState {
	public HashMap<OID, FluentObject> objects = new HashMap<OID, FluentObject>();
	
	public String toString() {
		return "" + objects;
	}
}
