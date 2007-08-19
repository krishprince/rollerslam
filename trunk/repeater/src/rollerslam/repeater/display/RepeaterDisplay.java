package rollerslam.repeater.display;
import java.rmi.RemoteException;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.DisplayRegistryServer;

/**
* Essa classe representa um display, mas as mensagens enviadas pela simulação
* são reencaminhadas para o conjunto de displays registrados pelo repetidor
* 
* @author Pablo
*/
@SuppressWarnings("serial")
public class RepeaterDisplay implements Display {
	
	private DisplayRegistryServer displayRegistry;
	
	public RepeaterDisplay(DisplayRegistryServer displayRegistry) {
		this.displayRegistry = displayRegistry;
	}

	/**
	 * @see rollerslam.infrastructure.server.Display#update(Message m)
	 */
	public void update(Message m) throws RemoteException {
		for (Display display : displayRegistry.getRegisteredDisplays()) {			
			try{
				display.update(m);
			} catch(Exception e) {
				e.printStackTrace();
				displayRegistry.unregister(display);
			}
		}
	}

}
