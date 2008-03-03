package rollerslam.fluxinferenceengine.specification.service;

public abstract class FluxInferenceEngine {

	private ReasoningFacade reasoningFacade;

	public ReasoningFacade getReasoningFacade() {
		return reasoningFacade;
	}

	public void setReasoningFacade(ReasoningFacade reasoningFacade) {
		this.reasoningFacade = reasoningFacade;
	}

}
