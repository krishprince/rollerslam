package rollerslam.common.objects.oid;

import rollerslam.agent.communicative.specification.type.fluent.OID;
import rollerslam.common.datatype.PlayerTeam;

public class PlayerOID extends OID {

	public PlayerTeam team;	
	public int        number;	
	
	public PlayerOID(PlayerTeam team, int playerNumber) {
		this.team = team;
		this.number = playerNumber;
	}
	
	public boolean equals(Object o) {
		return o != null && o instanceof PlayerOID && o.hashCode() == hashCode();
	}
	
	public int hashCode() {
		if (team == PlayerTeam.TEAM_A) {
			return number;
		} else {
			return -number;
		}
	}
	
	public String toString() {
		return "player-" + team + "-"+number;
	}
}
