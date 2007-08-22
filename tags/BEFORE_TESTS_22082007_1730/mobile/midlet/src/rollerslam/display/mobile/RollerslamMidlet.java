package rollerslam.display.mobile;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import rollerslam.display.mobile.gui.ConnectionForm;
import rollerslam.display.mobile.gui.mvc.Controller;
import rollerslam.display.mobile.gui.mvc.Model;

public class RollerslamMidlet extends MIDlet {

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		Display d = Display.getDisplay(this);
		Model m = new ModelImpl();
		Controller c = ControllerImpl.getInstance();				
		c.setModel(m);
		
		d.setCurrent(new ConnectionForm(d, c, m));
	}

}
