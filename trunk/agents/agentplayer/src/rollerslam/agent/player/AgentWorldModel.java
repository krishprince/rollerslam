package rollerslam.agent.player;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.strategy.AgentRole;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;

public class AgentWorldModel extends GoalBasedEnvironmentStateModel {

	public AgentGoal currentGoal;
	public boolean gameStarted = false;
	public boolean joinMessageSent = false;
	public int myID = -1;
	public PlayerTeam myTeam;
	public AgentRole role = null;
	public int position = -1;
	
		
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
