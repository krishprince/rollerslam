package rollerslam.common.actions;

import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.utils.Vector;

@SuppressWarnings("serial")
public class DashAction extends LegAction {

	public Vector acceleration;

	public DashAction(PlayerOID playerOID, Vector acceleration){
		super(playerOID);
		this.acceleration = acceleration;
	}
	
	public String toString() {
		return "Dash("+acceleration+")";
	}
	
}
