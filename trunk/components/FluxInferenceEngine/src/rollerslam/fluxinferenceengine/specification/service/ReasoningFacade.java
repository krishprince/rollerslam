package rollerslam.fluxinferenceengine.specification.service;

import java.util.List;

import rollerslam.fluxinferenceengine.specification.service.type.ReasoningException;
import rollerslam.fluxinferenceengine.specification.type.Action;
import rollerslam.fluxinferenceengine.specification.type.FluxSpecification;
import rollerslam.fluxinferenceengine.specification.type.State;

public abstract class ReasoningFacade {

	public abstract State updateModel(FluxSpecification spec, State state)
			throws ReasoningException;

	public abstract Action computeNextAction(FluxSpecification spec, State state)
			throws ReasoningException;

	public abstract State processAction(FluxSpecification spec, State state,
			Action action) throws ReasoningException;
	
	public abstract State initializeModel(FluxSpecification spec,
			List<Object> initialData) throws ReasoningException;
}
