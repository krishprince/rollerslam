package rollerslam.fluxcommunicativeagent.realization.type;

import rollerslam.agent.communicative.specification.type.object.OID;

import com.parctechnologies.eclipse.CompoundTerm;

public class FluxOID extends OID {
	public CompoundTerm term;

	public FluxOID(CompoundTerm term) {
		super();
		this.term = term;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((term == null) ? 0 : term.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final FluxOID other = (FluxOID) obj;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

	public String toString() {
		return ToStringPrinterUtility.toString(term);
	}
	
}
