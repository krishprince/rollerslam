package rollerslam.infrastructure.agent.automata;

import rollerslam.infrastructure.agent.Message;

public interface ModelBasedBehaviorStrategyComponent {
	Message computeAction(EnvironmentStateModel w);
}
