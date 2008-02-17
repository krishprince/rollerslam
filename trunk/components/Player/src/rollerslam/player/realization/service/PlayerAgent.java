package rollerslam.player.realization.service;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.common.datatype.PlayerTeam;
import rollerslam.fluxcommunicativeagent.realization.service.FluxCommunicativeAgent;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOID;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOOState;
import rollerslam.fluxinferenceengine.specification.type.Fluent;
import rollerslam.infrastructure.specification.service.Agent;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTermImpl;

// TODO remove references to EclipseProlog and to InferenceEngine
public class PlayerAgent extends FluxCommunicativeAgent {

	public PlayerAgent(Agent port, long cycleLength, PlayerTeam team, int playerID) throws Exception {
		super(port, new File("/documents/rollerslam/workspace/Player/src/rollerslam/player/realization/service/flux/player.pl"), "player", cycleLength);
		
		FluxOID oid = new FluxOID(new Atom("me"));
		Set<Fluent> fs = new HashSet<Fluent>();
		
		fs.add(makeFluent(oid, "id", new CompoundTermImpl("player", playerID, team.toString())));
		fs.add(makeFluent(oid, "senseCycle", new Atom("true")));
		
		FluxOOState state = new FluxOOState(fs);
		WorldObject wo = new WorldObject(oid, state);
		
		kb.objects.put(oid, wo);
	}

}
