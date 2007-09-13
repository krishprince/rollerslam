package rollerslam.agent.player;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.strategy.AgentRole;
import rollerslam.environment.model.strategy.PlayerPosition;
import rollerslam.environment.model.strategy.PositionCoord;
import rollerslam.environment.model.utils.Vector;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;

public class AgentWorldModel extends GoalBasedEnvironmentStateModel {

	public AgentGoal currentGoal;
	public boolean gameStarted = false;
	public boolean joinMessageSent = false;
	public int myID = -1;
	public PlayerTeam myTeam;
	public AgentRole role = null;
	public PlayerPosition position = null;
	public Vector posCoord = null;
	public int cycleLastMsg = -1;
	public int myMaxArea = PositionCoord.maxArea;
		
	public AgentWorldModel(EnvironmentStateModel model) {
		super(model);
	}

	
	public Player getMe() {
		for (Player player : ((World)environmentStateModel).playersA) {
			if (player.id == myID) {
               	return player;
			}
		}

		for (Player player : ((World)environmentStateModel).playersB) {
			if (player.id == myID) {
				return player;
			}
		}

		return null;
	}	
}
