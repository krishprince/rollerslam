package rollerslam.gamephysics.realization.service;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.fluxcommunicativeagent.realization.service.FluxCommunicativeAgent;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOID;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOOState;
import rollerslam.fluxinferenceengine.specification.type.Fluent;
import rollerslam.infrastructure.specification.service.Agent;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.CompoundTermImpl;

public class GamePhysicsAgent extends FluxCommunicativeAgent {

	private static final String ADDRESS_FLUX_FILE = GamePhysicsAgent.class.getResource("gamePhysics.pl").getFile();

	public GamePhysicsAgent(Agent port, long cycleLength, int playersPerTeam) throws Exception {
		super(port, new File(ADDRESS_FLUX_FILE), "gamePhysics", cycleLength);
		createObject("ball",0,0);
		createPlayer("1","TEAM_A",10000,10000);
		createPlayer("2","TEAM_B",-10000,-10000);
	}

	private void createObject(String id, int x, int y) {
		FluxOID oid = new FluxOID(new Atom(id));
		Set<Fluent> fs = new HashSet<Fluent>();

		fs.add(makeFluent(oid, "position", new CompoundTermImpl("vector",x,y)));

		FluxOOState state = new FluxOOState(fs);
		WorldObject worldObject = new WorldObject(oid, state);

		this.getKb().getObjects().put(oid, worldObject);
	}

	private void createPlayer(String id, String team, int x, int y) {
		CompoundTerm compoundTerm = new CompoundTermImpl("player",new Atom(id), team);
		FluxOID oid = new FluxOID(compoundTerm);

		//TODO pegar a posição da tela dos players conforme versão 1.0
		super.declareFAtom(oid, "position", new CompoundTermImpl("vector",x,y));

	}

}
