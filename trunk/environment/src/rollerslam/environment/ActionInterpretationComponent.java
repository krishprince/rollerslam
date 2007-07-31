package rollerslam.environment;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;

public interface ActionInterpretationComponent {
	void processAction(World w, Message m);
}
