package rollerslam.agent.goalbased;

import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;

public class AgentWorldModel extends GoalBasedEnvironmentStateModel {

	public AgentGoal currentGoal;
	public boolean gameStarted = false;
	public boolean joinMessageSent = false;
	public int myID = -1;
	public PlayerTeam myTeam;
	
	public AgentWorldModel(EnvironmentStateModel model) {
		super(model);
	}

	
	public Player getMe() {
		
		if(environmentStateModel == null){
			return null;
		}
		
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
