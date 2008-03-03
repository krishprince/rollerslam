package rollerslam.fluxinferenceengine.realization.type;

import com.parctechnologies.eclipse.CompoundTerm;

import rollerslam.fluxinferenceengine.specification.type.Fluent;

public class EclipsePrologFluent extends Fluent {

	private CompoundTerm term;

	public CompoundTerm getTerm() {
		return term;
	}

	public void setTerm(CompoundTerm term) {
		this.term = term;
	}

	public EclipsePrologFluent(CompoundTerm term) {
		super();
		this.term = term;
	}

	public String toString() {
		return ""+ term;
	}
}
