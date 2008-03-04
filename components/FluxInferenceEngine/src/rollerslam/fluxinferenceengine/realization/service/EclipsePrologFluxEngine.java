package rollerslam.fluxinferenceengine.realization.service;

import rollerslam.fluxinferenceengine.specification.service.FluxInferenceEngine;

@SuppressWarnings("unchecked")
public class EclipsePrologFluxEngine extends FluxInferenceEngine {

	public EclipsePrologFluxEngine() throws Exception {
		this.setReasoningFacade(new ReasoningFacadeImpl());
	}

}
