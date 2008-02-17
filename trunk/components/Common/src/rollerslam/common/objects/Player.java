package rollerslam.common.objects;

import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.common.objects.state.PlayerState;

public class Player extends WorldObject {

	public Player(PlayerOID oid, PlayerState  state) {
		super(oid, state);
	}

}
