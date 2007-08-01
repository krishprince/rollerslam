package rollerslam.infrastructure.agent;

public interface ModelBasedBehaviorStrategyComponent {
	Message computeAction(EnvironmentStateModel w);
}
