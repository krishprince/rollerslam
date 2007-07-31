package rollerslam.display.gui.mvc;

public interface Controller {
	void connect(String host) throws Exception;
	void startSimulation() throws Exception;
	void stopSimulation() throws Exception;
}
