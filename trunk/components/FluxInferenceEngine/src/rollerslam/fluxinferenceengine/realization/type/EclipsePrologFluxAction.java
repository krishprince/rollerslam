package rollerslam.fluxinferenceengine.realization.type;

import com.parctechnologies.eclipse.CompoundTerm;

import rollerslam.fluxinferenceengine.specification.type.Action;

public class EclipsePrologFluxAction extends Action {

	public CompoundTerm actionTerm;

	public EclipsePrologFluxAction(CompoundTerm actionTerm) {
		super();
		this.actionTerm = actionTerm;
	}
	
}
