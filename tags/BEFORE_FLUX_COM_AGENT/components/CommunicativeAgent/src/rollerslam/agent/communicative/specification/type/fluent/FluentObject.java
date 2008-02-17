package rollerslam.agent.communicative.specification.type.fluent;

public class FluentObject {
	public OID oid;
	public FluentState state;
	
	public FluentObject(OID oid, FluentState state) {
		this.oid = oid;
		this.state = state;
	}
	
	public String toString() {
		return oid + "[" + state + "]";
	}
}
