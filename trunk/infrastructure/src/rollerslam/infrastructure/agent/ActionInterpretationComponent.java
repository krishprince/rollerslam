package rollerslam.infrastructure.agent;


public interface ActionInterpretationComponent {
	void processAction(EnvironmentStateModel w, Message m);
}
