package rollerslam.agent.communicative.specification.type.object;

public class WorldObject {
	public OID oid;
	public ObjectState state;
	
	public WorldObject(OID oid, ObjectState state) {
		this.oid = oid;
		this.state = state;
	}
	
	public String toString() {
		return oid + "[" + state + "]";
	}
}
