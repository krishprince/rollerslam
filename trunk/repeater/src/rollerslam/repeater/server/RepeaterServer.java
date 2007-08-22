package rollerslam.repeater.server;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import rollerslam.display.RepeaterDisplay;
import rollerslam.infrastructure.agent.SensorEffectorManager;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.discoverer.server.MulticastClientListener;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.logging.LogRecordingService;
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
		
		if (serverHost == null) {
			clientFacade.getClientInitialization().init();			
			serverHost = clientFacade.getClientInitialization().getRemoteHost();
		} else {
			clientFacade.getClientInitialization().init(serverHost);
		}
		
		try {
			clientFacade.getDisplayRegistry().register(
					(Display) clientFacade.getClientInitialization().exportObject(
							this.repeaterDisplay
						)
				);
		} catch (AlreadyBoundException e1) {
			e1.printStackTrace();
        	System.exit(-1);
		}
		
		try {			
        	LocateRegistry.createRegistry(1099);
        } catch(Exception e) {
        	e.printStackTrace();
        	LocateRegistry.getRegistry(1099);
        }
        
        DisplayRegistry drs = (DisplayRegistry) UnicastRemoteObject
        		.exportObject(repeaterRegistryServer, 0);        
        
        bind(DisplayRegistry.class.getSimpleName(), drs);
        
        // Redireciona para o servidor
        bind(AgentRegistry.class.getSimpleName(), clientFacade.getAgentRegistry());        
        bind(SimulationAdmin.class.getSimpleName(), clientFacade.getSimulationAdmin());
        bind(LogRecordingService.class.getSimpleName(), clientFacade.getLogRecordingService());
        
        //Stub do sensor manager
        SensorEffectorManager sems = (SensorEffectorManager) lookup(SensorEffectorManager.class.getSimpleName());        
        bind(SensorEffectorManager.class.getSimpleName(), sems);  
        
        // starts multicast listener
        try {
			new MulticastClientListener().start();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RemoteException(e.getMessage());
		}        
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
