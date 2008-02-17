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
import com.parctechnologies.eclipse.CompoundTermImpl;

public class GamePhysicsAgent extends FluxCommunicativeAgent {

	public GamePhysicsAgent(Agent port, long cycleLength, int playersPerTeam) throws Exception {
		super(port, new File("/documents/rollerslam/workspace/GamePhysics/src/rollerslam/gamephysics/realization/service/flux/gamePhysics.pl"), "gamePhysics", cycleLength);
		
		FluxOID oid = new FluxOID(new Atom("ball"));
		Set<Fluent> fs = new HashSet<Fluent>();
		
		fs.add(makeFluent(oid, "position", new CompoundTermImpl("vector",0,0)));
		
		FluxOOState state = new FluxOOState(fs);
		WorldObject wo = new WorldObject(oid, state);
		
		kb.objects.put(oid, wo);
	}

}
