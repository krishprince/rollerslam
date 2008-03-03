/**
 *
 */
package rollerslam.display.realization.service.gui;

import java.util.Vector;

import rollerslam.display.realization.service.DisplayAgent;
import rollerslam.display.realization.service.gui.mvc.Controller;
import rollerslam.display.realization.service.gui.mvc.Model;

/**
 * @author Marcos Aurélio
 *
 */
public class ControllerImpl implements Controller {

    private Model model = null;
    private DisplayAgent agent = null;

    public ControllerImpl(Model model, DisplayAgent agent) {
        this.model = model;
        this.agent = agent;
    }

    /**
     * @see rollerslam.display.realization.service.gui.mvc.Controller#connect(java.lang.String)
     */
    public void connect(String host) throws Exception {
    	new Thread("RollerslamDisplay - Agent >> Frame") {
    		public void run() {
    			while(true) {
    				model.setModel(agent.getStateForDisplay());

    				try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    			}
    		}
    	}.start();
    }

	/**
	 * @see rollerslam.display.realization.service.gui.mvc.Controller#getAvailableHosts()
	 */
	public Vector<String> getAvailableHosts() throws Exception {
		Vector<String> ret = new Vector<String>();
		ret.add("localhost");
		return ret;
	}


}
