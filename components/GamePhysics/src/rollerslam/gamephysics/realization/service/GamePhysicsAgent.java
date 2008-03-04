package rollerslam.gamephysics.realization.service;


import java.io.File;

import rollerslam.fluxcommunicativeagent.realization.service.FluxCommunicativeAgent;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOID;
import rollerslam.infrastructure.specification.service.Agent;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.CompoundTermImpl;

public class GamePhysicsAgent extends FluxCommunicativeAgent {

	private static final String ADDRESS_FLUX_FILE = GamePhysicsAgent.class.getResource("gamePhysics.pl").getFile();

	public GamePhysicsAgent(Agent port, long cycleLength, int playersPerTeam) throws Exception {
		super(port, new File(ADDRESS_FLUX_FILE), "gamePhysics", cycleLength);
		createObject("ball",0,0);
		//TODO pegar a posição da tela dos players conforme versão 1.0
		createPlayer("1","TEAM_A",10000,10000);
		createPlayer("2","TEAM_B",-10000,-10000);
	}

	private void createObject(String id, int x, int y) {
		CompoundTermImpl compoundTerm = new CompoundTermImpl("vector",x,y);
		FluxOID oid = new FluxOID(new Atom(id));
		super.declareFAtom(oid, "position", compoundTerm);
	}

	private void createPlayer(String id, String team, int x, int y) {
		CompoundTerm compoundTerm = new CompoundTermImpl("player",new Atom(id), team);
		FluxOID oid = new FluxOID(compoundTerm);
		super.declareFAtom(oid, "position", new CompoundTermImpl("vector",x,y));

	}

}
