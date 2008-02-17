package rollerslam.fluxinferenceengine.realization.type;

import com.parctechnologies.eclipse.CompoundTerm;

import rollerslam.fluxinferenceengine.specification.type.Fluent;

public class EclipsePrologFluent extends Fluent {
	public CompoundTerm term;

	public EclipsePrologFluent(CompoundTerm term) {
		super();
		this.term = term;
	}
	
	public String toString() {
		return ""+ term;
	}
}
