package rollerslam.common.objects;

import rollerslam.agent.communicative.specification.type.fluent.FluentObject;
import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.common.objects.state.PlayerState;

public class Player extends FluentObject {

	public Player(PlayerOID oid, PlayerState  state) {
		super(oid, state);
	}

}
