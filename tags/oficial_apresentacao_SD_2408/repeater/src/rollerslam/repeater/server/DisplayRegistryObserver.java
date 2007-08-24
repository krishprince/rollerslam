package rollerslam.repeater.server;

import rollerslam.infrastructure.display.Display;

public interface DisplayRegistryObserver {
	void notifyRegistered(Display d);
	void notifyUnregistered(Display d);
}
