package rollerslam.infrastructure.agent.goalbased;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;

public interface GoalBasedBehaviorStrategyComponent extends
		ModelBasedBehaviorStrategyComponent {
	Message computeAction(GoalBasedEnvironmentStateModel w);

}
