package rollerslam.infrastructure.agent.goalbased;

import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;

public abstract class GoalBasedEnvironmentStateModel extends EnvironmentStateModel {
	public EnvironmentStateModel environmentStateModel;
	
	public GoalBasedEnvironmentStateModel(EnvironmentStateModel model) {
		this.environmentStateModel = model;
	}
}
