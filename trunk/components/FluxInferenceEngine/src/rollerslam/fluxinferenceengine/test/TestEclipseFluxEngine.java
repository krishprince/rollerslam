package rollerslam.fluxinferenceengine.test;

import java.io.File;

import rollerslam.fluxinferenceengine.realization.service.EclipsePrologFluxEngine;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluxSpecification;
import rollerslam.fluxinferenceengine.specification.service.FluxInferenceEngine;
import rollerslam.fluxinferenceengine.specification.service.ReasoningFacade;
import rollerslam.fluxinferenceengine.specification.type.FluxSpecification;
import rollerslam.fluxinferenceengine.specification.type.State;

public class TestEclipseFluxEngine {

	public static void main(String[] args) throws Exception {
		FluxSpecification spec = new EclipsePrologFluxSpecification(
				new File("/documents/rollerslam/workspace/FluxInferenceEngine/src/rollerslam/fluxinferenceengine/test/testflux.pl"),
				"test");
		State state = new State();

		FluxInferenceEngine fie = new EclipsePrologFluxEngine();
		ReasoningFacade rf = fie.getReasoningFacade();

		System.out.println(""+rf.updateModel(spec, state));
	}

}
