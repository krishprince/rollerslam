package rollerslam.fluxinferenceengine.realization.type;

import java.io.File;

import rollerslam.fluxinferenceengine.specification.type.FluxSpecification;

public class EclipsePrologFluxSpecification extends FluxSpecification {

	private File fluxFile;
	private String agentName;

	public File getFluxFile() {
		return fluxFile;
	}

	public void setFluxFile(File fluxFile) {
		this.fluxFile = fluxFile;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public EclipsePrologFluxSpecification(File fluxFile, String agentName) {
		super();
		this.fluxFile = fluxFile;
		this.agentName = agentName;
	}

}
