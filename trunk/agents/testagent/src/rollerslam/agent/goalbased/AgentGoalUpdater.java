package rollerslam.agent.goalbased;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.DashAction;
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
				System.out.println("GOING TO CENTER");
				model.currentGoal = AgentGoal.GO_TO_CENTER;
			}			
		} else if (model.currentGoal == AgentGoal.GO_TO_CENTER) { 
			Player me = getMeFromModel(model);
			
			double mex = me.sx / 1000.0;
			double mey = me.sy / 1000.0;
			
			double d = Math.sqrt(mex*mex + mey*mey);
			
			System.out.println("DISTANCE TO CENTER: " + d);
			
			if (d < 10.0) {
				System.out.println("GOING TO GOAL");
				model.currentGoal = AgentGoal.GO_TO_GOAL;				
			}
		}
	}

	private Player getMeFromModel(AgentWorldModel model) {
		for (Player player : ((World)model.environmentStateModel).playersA) {
			if (player.id == model.myID) {
				return player;
			}
		}

		for (Player player : ((World)model.environmentStateModel).playersB) {
			if (player.id == model.myID) {
				return player;
			}
		}

		return null;
	}
	
}
