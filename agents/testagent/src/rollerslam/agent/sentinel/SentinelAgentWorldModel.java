package rollerslam.agent.sentinel;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;

public class SentinelAgentWorldModel extends GoalBasedEnvironmentStateModel {

	public SentinelAgentGoal currentGoal;
	public boolean gameStarted = false;
	public int myID = -1;
	public PlayerTeam myTeamA;
	public PlayerTeam myTeamB;
	
	public SentinelAgentWorldModel(EnvironmentStateModel model) {
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
