package rollerslam.common.actions;

import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.infrastructure.specification.service.Message;

public abstract class Action extends Message {

	public PlayerOID actor;

	public Action(PlayerOID playerOID) {
		actor = playerOID;
	}

}
