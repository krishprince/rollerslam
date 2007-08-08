package rollerslam.agent.goalbased;

import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.environment.model.PlayerTeam;

public class AgentWorldModel extends GoalBasedEnvironmentStateModel {

	public AgentGoal currentGoal;
	public boolean gameStarted = false;
	public boolean joinMessageSent = false;
	public int myID = -1;
	public PlayerTeam myTeam;
	
	public boolean messageSent = false;
	
	public AgentWorldModel(EnvironmentStateModel model) {
		super(model);
	}

}
