package rollerslam.gamephysics.realization.service;


import java.io.File;
import java.util.Arrays;

import rollerslam.fluxcommunicativeagent.realization.service.FluxCommunicativeAgent;
import rollerslam.infrastructure.specification.service.Agent;

public class GamePhysicsAgent extends FluxCommunicativeAgent {

	private static final String ADDRESS_FLUX_FILE = GamePhysicsAgent.class.getResource("gamePhysics.pl").getFile();

	public GamePhysicsAgent(Agent port, long cycleLength, int playersPerTeam) throws Exception {
		super(port, new File(ADDRESS_FLUX_FILE), "gamePhysics", cycleLength, Arrays.asList(new Object[] { playersPerTeam }));
	}
}
