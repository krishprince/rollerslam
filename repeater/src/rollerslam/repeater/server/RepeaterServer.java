package rollerslam.repeater.server;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import rollerslam.display.RepeaterDisplay;
import rollerslam.infrastructure.agent.SensorEffectorManager;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.AgentRegistry;
import rollerslam.infrastructure.server.DisplayRegistry;
import rollerslam.infrastructure.server.SimulationAdmin;

/**
*  Classe usada para iniciar o Repetidor 
* 
* @author Pablo
*/
public class RepeaterServer {
	private static RepeaterServer instance = null;
	
	ClientFacade clientFacade;
	RepeaterDisplayRegistryServer repeaterRegistryServer;
	RepeaterDisplay repeaterDisplay;
	String host;
	
	private RepeaterServer() {
		this.clientFacade = ClientFacadeImpl.getInstance();
		this.repeaterRegistryServer = new RepeaterDisplayRegistryServer();
		this.repeaterDisplay = new RepeaterDisplay(this.repeaterRegistryServer);
	}	
	
	/**
     * @return the unique instance for the server facade
     */
	public static RepeaterServer getInstance() {
		if(instance == null) {
			instance = new RepeaterServer();
		}
		
		return instance;
	}
	
	public void init(String serverHost) throws RemoteException {
		host = serverHost;
		clientFacade.getClientInitialization().init(serverHost);		
		
		try {
			clientFacade.getDisplayRegistry().register(
					(Display) clientFacade.getClientInitialization().exportObject(
							this.repeaterDisplay
						)
				);
			
        	LocateRegistry.createRegistry(1099);
        } catch(Exception e) {
        	e.printStackTrace();
        	System.exit(-1);
        }
        
        DisplayRegistry drs = (DisplayRegistry) UnicastRemoteObject
        		.exportObject(repeaterRegistryServer, 0);        
        
        bind(DisplayRegistry.class.getSimpleName(), drs);
        
        // Redireciona para o servidor
        bind(AgentRegistry.class.getSimpleName(), clientFacade.getAgentRegistry());        
        bind(SimulationAdmin.class.getSimpleName(), clientFacade.getSimulationAdmin());
        
        //Stub do sensor manager
        SensorEffectorManager sems = (SensorEffectorManager) lookup(SensorEffectorManager.class.getSimpleName());        
        bind(SensorEffectorManager.class.getSimpleName(), sems);       
	}
	
    private Object lookup(String simpleName) throws RemoteException {
		try {
			return Naming.lookup("rmi://" + host + "/" + simpleName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.toString());
		}
	}
	
	private void bind(String string, Remote ret) throws RemoteException {
    	try {
			Naming.bind(string, ret);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.toString());
		}
	}

}
