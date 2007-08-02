package rollerslam.agents.goalbased;

import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;

public class AgentWorldModel extends GoalBasedEnvironmentStateModel {

	public AgentGoal currentGoal;
	public boolean gameStarted = false;
	public boolean joinMessageSent = false;
	public int myID = -1;
	
	public AgentWorldModel(EnvironmentStateModel model) {
		super(model);
	}

}
