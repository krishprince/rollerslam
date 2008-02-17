package rollerslam.fluxinferenceengine.realization.type;

import java.io.File;

import rollerslam.fluxinferenceengine.specification.type.FluxSpecification;

public class EclipsePrologFluxSpecification extends FluxSpecification {

	public File fluxFile;
	public String agentName;
	
	public EclipsePrologFluxSpecification(File fluxFile, String agentName) {
		super();
		this.fluxFile = fluxFile;
		this.agentName = agentName;
	}

}
