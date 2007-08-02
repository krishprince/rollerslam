package rollerslam.infrastructure.agent.automata;

import rollerslam.infrastructure.agent.Message;


public interface ActionInterpretationComponent {
	void processAction(EnvironmentStateModel w, Message m);
}
