package rollerslam.player.realization.service;


import java.io.File;

import com.parctechnologies.eclipse.Atom;

import rollerslam.common.datatype.PlayerTeam;
import rollerslam.fluxcommunicativeagent.realization.service.FluxCommunicativeAgent;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOID;
import rollerslam.infrastructure.specification.service.Agent;

public class PlayerAgent extends FluxCommunicativeAgent {
	
	private static final String ADDRESS_FLUX_FILE = PlayerAgent.class.getResource("player.pl").getFile();

	public PlayerAgent(Agent port, long cycleLength, PlayerTeam team, int playerID) throws Exception {
		super(port, new File(ADDRESS_FLUX_FILE), "player", cycleLength);
		
		FluxOID me = new FluxOID(new Atom("me"));

		super.declareFAtom(me, "id", ""+playerID);
		super.declareFAtom(me, "team", team.toString());
		super.declareFAtom(me, "senseCycle", "true");
	}

}
