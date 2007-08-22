package rollerslam.display.gui.mvc;

import java.util.Vector;

public interface Controller {
	void connect(String host) throws Exception;
	Vector<String> getAvailableHosts() throws Exception;
}
