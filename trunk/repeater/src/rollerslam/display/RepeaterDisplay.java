package rollerslam.display;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JOptionPane;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.DisplayRegistryServer;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.repeater.server.RepeaterServer;

/**
* Essa classe representa um display, mas as mensagens enviadas pela simulação
* são reencaminhadas para o conjunto de displays registrados pelo repetidor
* 
* @author Pablo
*/
@SuppressWarnings("serial")
public class RepeaterDisplay implements Display, Runnable {
	
	private DisplayRegistryServer displayRegistry;
	private Vector<Message> messages = new Vector<Message>();

	public RepeaterDisplay(DisplayRegistryServer displayRegistry) {
		this.displayRegistry = displayRegistry;
		
		new Thread(this).start();
	}

	/**
	 * @see rollerslam.infrastructure.server.Display#update(Message m)
	 */
	public void update(Message m) throws RemoteException {
		synchronized (messages) {
			messages.add(m);
			messages.notifyAll();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		RepeaterServer server = RepeaterServer.getInstance();
		try {
			String host = null;
			
			if (args.length > 0) {
				if (args[0].toUpperCase().equals("AUTO")) {
					host = null;
				} else {
					host = args[0];
				}
			} else {
				host = JOptionPane.showInputDialog("Simulation:", "localhost");
			}
			
			server.init(host);			
		} catch (RemoteException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
		} 
	}

	@Override
	public void run() {
		
		while(true) {
			while(messages.isEmpty()) {
				synchronized (messages) {
					try {
						messages.wait();						
					} catch(Exception e) {
						
					}
				}
			}
			
			synchronized (messages) {
				synchronized (displayRegistry) {
					Vector<Display> toRemove = new Vector<Display>();		
					
					try {
						for (Message m : messages) {
							for (Display display : displayRegistry
									.getRegisteredDisplays()) {
								try {
									display.update(m);
								} catch (Exception e) {
									if (PrintTrace.TracePrint) {
										e.printStackTrace();
									}

									toRemove.add(display);
									break;
								}
							}

							for (Display display : toRemove) {
								displayRegistry.unregister(display);
							}
						}

						messages.clear();
					} catch (RemoteException e) {
						e.printStackTrace();
					}										
				}				
			}
		}		
	}	
}