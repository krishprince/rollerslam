package rollerslam.agent.coach;

import java.util.HashMap;
import java.util.Vector;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.strategy.PlayerPosition;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;

public class CoachAgentWorldModel extends GoalBasedEnvironmentStateModel {

	public CoachAgentGoal currentGoal;
	public boolean gameStarted = false;
	public PlayerTeam myTeam;
	
	public HashMap<Integer, PlayerPosition> playersPosition = new HashMap<Integer, PlayerPosition>();
	public Vector<Integer> playersToSetPosition = new Vector<Integer>();
	public Vector<PlayerPosition> positionToSet = new Vector<PlayerPosition>();
	public PlayerPosition lastPosition = null;
	public Integer lastPlayers = null;
		
	public CoachAgentWorldModel(EnvironmentStateModel model) {
		super(model);
	
		//Strategy
		positionToSet.add(PlayerPosition.CENTERFORWARD);	 // 15 - A	
		positionToSet.add(PlayerPosition.GOALKEEPER);   // 0 - D
		positionToSet.add(PlayerPosition.CENTERBACK);   // 4 - D
		positionToSet.add(PlayerPosition.CENTERTRACKER);   // 9 - M
		positionToSet.add(PlayerPosition.ENDFORWARD);   // 19 - A
		positionToSet.add(PlayerPosition.ENDBACK);   // 1 - D
		positionToSet.add(PlayerPosition.HALFBACK);   // 8 - M
		positionToSet.add(PlayerPosition.FREEFORWARD1);   // 17 - A
		positionToSet.add(PlayerPosition.FREEBACKS1);   // 5 - D
		positionToSet.add(PlayerPosition.HALFFORWARD);   // 10 - M
		positionToSet.add(PlayerPosition.FREEFORWARD2);   // 18 - A
		positionToSet.add(PlayerPosition.FREEBACKS2);   // 6 - D
		positionToSet.add(PlayerPosition.ROVER1);   // 12 - M
		positionToSet.add(PlayerPosition.RIGHTFORWARD);   // 14 - A
		positionToSet.add(PlayerPosition.ROVER2);   // 13 - D
		positionToSet.add(PlayerPosition.RIGHTBACK);   // 2 - M
		positionToSet.add(PlayerPosition.LEFTFORWARD);   // 16 - A
		positionToSet.add(PlayerPosition.LEFTBACK);   // 3 - D
		positionToSet.add(PlayerPosition.RIGHTTRACKER);   // 7 - M
		positionToSet.add(PlayerPosition.LEFTTRACKER);   // 11 - M
		
	}
}
