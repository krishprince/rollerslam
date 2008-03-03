package rollerslam.fluxcommunicativeagent.realization.type;

import rollerslam.agent.communicative.specification.type.CommunicativeAgentID;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;

public class FluxAgentID extends CommunicativeAgentID {

	private CompoundTerm term;

	public CompoundTerm getTerm() {
		return term;
	}

	public void setTerm(CompoundTerm term) {
		this.term = term;
	}

	public FluxAgentID(CompoundTerm term) {
		super(term.functor());
		this.term = term;
	}

	public FluxAgentID(String string) {
		this(new Atom(string));
	}

	@Override
	public int hashCode() {
		final int PRIME = 37;
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
		final FluxAgentID other = (FluxAgentID) obj;
		if (term == null) {
			if (other.term != null)
				return false;
		} else if (!term.equals(other.term))
			return false;
		return true;
	}

	public String toString() {
		if (term instanceof Atom) {
			return ""+ term.functor();
		} else {
			return "" + term;
		}
	}

}
