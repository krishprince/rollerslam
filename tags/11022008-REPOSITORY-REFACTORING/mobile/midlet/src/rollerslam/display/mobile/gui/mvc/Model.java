package rollerslam.display.mobile.gui.mvc;

import rollerslam.infrastructure.client.communication.SimulationState;

public interface Model {
	void setSimulationState(SimulationState ss);
	SimulationState getSimulationState();
}
