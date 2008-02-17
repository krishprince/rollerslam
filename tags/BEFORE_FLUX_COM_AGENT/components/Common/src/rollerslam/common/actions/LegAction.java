package rollerslam.common.actions;

import rollerslam.common.objects.oid.PlayerOID;

@SuppressWarnings("serial")
public abstract class LegAction extends Action {
	public LegAction(PlayerOID playerOID){
		super(playerOID);
	}
}
