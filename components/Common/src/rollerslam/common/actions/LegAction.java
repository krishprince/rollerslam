package rollerslam.common.actions;

import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.infrastructure.specification.service.Message;

@SuppressWarnings("serial")
public abstract class LegAction extends Action {
	public LegAction(PlayerOID playerOID){
		super(playerOID);
	}
}
