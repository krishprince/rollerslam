package rollerslam.display.mobile.gui.mvc;

public interface Controller {
	void connect(String host) throws Exception;
	
	Model getModel();
	void setModel(Model model);
}
