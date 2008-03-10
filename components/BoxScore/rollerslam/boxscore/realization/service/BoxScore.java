package rollerslam.boxscore.realization.service;

import java.io.File;
import java.util.Arrays;

import rollerslam.fluxcommunicativeagent.realization.service.FluxCommunicativeAgent;
import rollerslam.infrastructure.specification.service.Agent;

public class BoxScore extends FluxCommunicativeAgent {
	private static final String ADDRESS_FLUX_FILE = BoxScore.class.getResource("boxscore.pl").getFile();
	public BoxScore(Agent port, long cycleLength) throws Exception {
		super(port, new File(ADDRESS_FLUX_FILE), "boxscore", cycleLength, Arrays.asList());
	}
}
