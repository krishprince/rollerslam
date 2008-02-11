package rollerslam.agent.fluxplayer;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;

public class FluxPlayerWorldModel extends GoalBasedEnvironmentStateModel {
	public boolean goalIsJoinGame = true;
	public boolean gameStarted = false;
	public boolean joinMessageSent = false;
	public int myID = -1;
	public PlayerTeam myTeam;
	public boolean changed = false;

	public FluxPlayerWorldModel(EnvironmentStateModel environmentStateModel) {
		super(environmentStateModel);
		this.environmentStateModel = environmentStateModel;	
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
