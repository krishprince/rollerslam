/**
 *
 */
package rollerslam.display.gui;

import java.rmi.RemoteException;
import rollerslam.display.gui.mvc.Controller;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.mvc.View;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.display.Display;

/**
 * @author Marcos Aurélio
 *
 */
public class ControllerImpl implements Controller {

    ClientFacade facade = ClientFacadeImpl.getInstance();
    View view = null;
    Model model = null;

    public ControllerImpl(View view, Model model) {
        this.view = view;
        this.model = model;
    }

    /**
     * @see rollerslam.display.gui.mvc.Controller#connect(java.lang.String)
     */
    @Override
    public void connect(String host) throws Exception {
        facade.getClientInitialization().init(host);
        facade.getDisplayRegistry().register(
                (Display) facade.getClientInitialization().exportObject(
                     new Display() {
                         @Override
                         public void update(Message m) throws RemoteException {
                             if (m instanceof StateMessage) {
                                 ControllerImpl.this.model.setModel((World) ((StateMessage) m).model);
                             }
                         }
                     }
                )
            );
    }
}
