package rollerslam.agents.goalbased;

import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;

public class AgentGoalUpdater implements GoalUpdateComponent {

	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		AgentWorldModel model = (AgentWorldModel) goal;
		
		if (model.currentGoal == AgentGoal.JOIN_GAME) {
			if (model.joinMessageSent) {
				model.currentGoal = AgentGoal.WAIT_JOIN_GAME;
			}
		} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
			if (model.gameStarted) {
				model.currentGoal = AgentGoal.GO_TO_CENTER;
			}			
		}
	}

}
