package rollerslam.fluxinferenceengine.realization.type;

import com.parctechnologies.eclipse.CompoundTerm;

import rollerslam.fluxinferenceengine.specification.type.Action;

public class EclipsePrologFluxAction extends Action {

	private CompoundTerm actionTerm;

	public CompoundTerm getActionTerm() {
		return actionTerm;
	}

	public void setActionTerm(CompoundTerm actionTerm) {
		this.actionTerm = actionTerm;
	}

	public EclipsePrologFluxAction(CompoundTerm actionTerm) {
		super();
		this.actionTerm = actionTerm;
	}

}
