package rollerslam.display.mobile;

import rollerslam.display.mobile.gui.mvc.Controller;
import rollerslam.display.mobile.gui.mvc.Model;
import rollerslam.infrastructure.client.communication.SocketCommunicationFacade;
import rollerslam.infrastructure.client.communication.SimulationObserver;
import rollerslam.infrastructure.client.communication.SimulationState;

public class ControllerImpl implements Controller, SimulationObserver {

	private static Controller instance;
	private 	   Model      model;
	
	
	public void connect(String host) throws Exception {
		SocketCommunicationFacade.getInstance().connect(host, this);
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new ControllerImpl();
		}
		return instance;
	}

	public void notify(SimulationState simulationState) {
		model.setSimulationState(simulationState);
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
