package rollerslam.environment.model.strategy;

import java.util.HashMap;

import rollerslam.environment.model.utils.Vector;

public class DefensivePositionCoord {
	private static HashMap<PlayerPosition, Vector> posCoord = new HashMap<PlayerPosition, Vector>();
	
	public static int maxArea = 25000;
	public static double maxPlayers = 0.25;
	
	public static Vector getCoord(PlayerPosition position){
		return posCoord.get(position);
	}
	
	static{
		posCoord.put(PlayerPosition.GOALKEEPER, new Vector(-64200, 0));  //GOALKEEPER
		
		//BACKS
		posCoord.put(PlayerPosition.ENDBACK, new Vector(-78000, 0));  //ENDBACK
		posCoord.put(PlayerPosition.RIGHTBACK, new Vector(-64350, -33750));  //RIGHTBACK
		posCoord.put(PlayerPosition.LEFTBACK, new Vector(-64350, 33750));  //LEFTBACK
		posCoord.put(PlayerPosition.CENTERBACK, new Vector(-50500, 0));  //CENTERBACK
		posCoord.put(PlayerPosition.FREEBACKS1, new Vector(-57425, -16875));  //FREEBACKS1
		posCoord.put(PlayerPosition.FREEBACKS2, new Vector(-57425, 16875));  //FREEBACKS2

		//MIDFIELDER
		posCoord.put(PlayerPosition.RIGHTTRACKER, new Vector(-25250, -33750));  //RIGHTTRACKER
		posCoord.put(PlayerPosition.HALFBACK, new Vector(-25250, 0));  //HALFBACK
		posCoord.put(PlayerPosition.CENTERTRACKER, new Vector(0, 0));  //CENTERTRACKER
		posCoord.put(PlayerPosition.HALFFORWARD, new Vector(25250, 0));  //HALFFORWARD
		posCoord.put(PlayerPosition.LEFTTRACKER, new Vector(-25250, 33750));  //LEFTTRACKER
		posCoord.put(PlayerPosition.ROVER1, new Vector(-25250, -22500));  //ROVER1
		posCoord.put(PlayerPosition.ROVER2, new Vector(-25250, 22500));  //ROVER2
		
		//FORWARDS
		posCoord.put(PlayerPosition.RIGHTFORWARD, new Vector(0, -33750));  //RIGHTFORWARD
		posCoord.put(PlayerPosition.CENTERFORWARD, new Vector(50500, 0));  //CENTERFORWARD
		posCoord.put(PlayerPosition.LEFTFORWARD, new Vector(0, 33750));  //LEFTFORWARD
		posCoord.put(PlayerPosition.FREEFORWARD1, new Vector(57425, -16875));  //FREEFORWARD1
		posCoord.put(PlayerPosition.FREEFORWARD2, new Vector(57425, 16875));  //FREEFORWARD2
		posCoord.put(PlayerPosition.ENDFORWARD, new Vector(78000, 0));  //ENDFORWARD
	}

}
