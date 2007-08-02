package rollerslam.infrastructure.agent.goalbased;

import rollerslam.infrastructure.agent.automata.AutomataAgent;

public abstract class GoalBasedAgent extends AutomataAgent {

	protected GoalInitializationComponent goalInitializationComponent;
	protected GoalUpdateComponent		  goalUpdateComponent;
	
	protected void initialize() {
		super.initialize();		
		goalInitializationComponent.initialize((GoalBasedEnvironmentStateModel) worldModel);
	}
	
	protected void think() {
		super.think();
		goalUpdateComponent.updateGoal((GoalBasedEnvironmentStateModel) worldModel);
	}
}
