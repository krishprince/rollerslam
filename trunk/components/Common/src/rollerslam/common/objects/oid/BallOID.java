package rollerslam.common.objects.oid;

import rollerslam.agent.communicative.specification.type.fluent.OID;

public class BallOID extends OID {

	public int hashCode() {
		return 0;
	}
	
	public boolean equals(Object o) {
		return o instanceof BallOID;
	}
	
	public String toString() {
		return "ball";
	}
}
