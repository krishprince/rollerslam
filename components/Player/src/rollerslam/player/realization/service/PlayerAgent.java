package rollerslam.player.realization.service;


import java.io.File;
import java.util.Arrays;

import rollerslam.fluxcommunicativeagent.realization.service.FluxCommunicativeAgent;
import rollerslam.infrastructure.specification.service.Agent;

public class PlayerAgent extends FluxCommunicativeAgent {
	
	private static final String ADDRESS_FLUX_FILE = PlayerAgent.class.getResource("player.pl").getFile();

	public PlayerAgent(Agent port, long cycleLength, String team, int playerID) throws Exception {
		super(port, new File(ADDRESS_FLUX_FILE), "player", cycleLength, Arrays.asList(new Object[] { playerID, team, computeOid(playerID, team) }));
	}

	private static Object computeOid(int playerID, String team) {
		String oid = "p";
		
		if (team.equals("TEAM_A")) {
			oid += "a";
		} else {
			oid += "b";			
		}
		
		oid += playerID;
		return oid;
	}

}
