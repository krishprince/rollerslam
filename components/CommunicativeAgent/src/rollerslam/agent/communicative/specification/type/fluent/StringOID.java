package rollerslam.agent.communicative.specification.type.fluent;

public class StringOID extends OID {
	private String oid = "";
	
	public StringOID(String oid) {
		this.oid = "" + oid;
	}
	
	public int hashCode() {
		return oid.hashCode();
	}
	
	public boolean equals(Object o) {
		return this == o
				|| (o != null && o instanceof StringOID && ((StringOID) o).oid
						.equals(oid)); 
	}

	public String toString() {
		return this.oid;
	}
}
