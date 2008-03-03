package rollerslam.agent.communicative.specification.type.object;

public class WorldObject {

	private OID oid;
	private ObjectState state;

	public OID getOid() {
		return oid;
	}

	public void setOid(OID oid) {
		this.oid = oid;
	}

	public ObjectState getState() {
		return state;
	}

	public void setState(ObjectState state) {
		this.state = state;
	}

	public WorldObject(OID oid, ObjectState state) {
		this.oid = oid;
		this.state = state;
	}

	public String toString() {
		return oid + "[" + state + "]";
	}
}
