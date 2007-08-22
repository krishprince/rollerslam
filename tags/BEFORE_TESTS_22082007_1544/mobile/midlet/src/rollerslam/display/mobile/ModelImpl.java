package rollerslam.display.mobile;

import rollerslam.display.mobile.gui.mvc.Model;
import rollerslam.infrastructure.client.communication.SimulationState;

public class ModelImpl implements Model {

	private SimulationState simulationState;
	
	public SimulationState getSimulationState() {
		return simulationState;
	}

	public void setSimulationState(SimulationState ss) {
		simulationState = ss;
	}

}
