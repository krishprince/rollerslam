package rollerslam.infrastructure.agent.sentinel;

import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalInitializationComponent;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;


public abstract class SentinelAgent extends AutomataAgent {
	
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
