package rollerslam.infrastructure.server;

import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rollerslam.agents.Agent;
import rollerslam.infrastructure.RemoteAgentInvocationHandler;
import rollerslam.infrastructure.annotations.agent;

/**
 * Server facade.
 * Used mainly to init the server using default configurations.
 * 
 * @author maas
 */
public class ServerFacade implements Server {
	
	private static ServerFacade instance = null;
	private static AgentRegistry ari;
	private static DisplayRegistry dri;
	private static SimulationAdmin sai;
	
	/**
	 * @return the unique instance for the server facade
	 */
	public static ServerFacade getInstance() {
		if (instance == null) {
			instance = new ServerFacade();
		}
		return instance;
	}
	
	private ServerFacade() {
		
	}
	
	/**
	 * Initializes the server using the passed agent as the environment agent
	 * 
	 * @param port the name server port
	 * @param environmentAgent the environment agent
	 * @throws Exception
	 */
	public static void init(int port, EnvironmentAgent environmentAgent) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1099);

		EnvironmentAgent eas = (EnvironmentAgent) UnicastRemoteObject
				.exportObject(environmentAgent, 0);

		sai = new SimulationAdminImpl(eas);
		ari = new AgentRegistryImpl(sai);
		dri = new DisplayRegistryImpl();
		
		SimulationAdmin sas = (SimulationAdmin) UnicastRemoteObject
				.exportObject(sai, 0);
		AgentRegistry ars = (AgentRegistry) UnicastRemoteObject
				.exportObject((AgentRegistry)ari, 0);
		DisplayRegistry drs = (DisplayRegistry) UnicastRemoteObject
				.exportObject(dri, 0);

		registry.bind(AgentRegistry.class.getSimpleName(),   ars);
		registry.bind(DisplayRegistry.class.getSimpleName(), drs);
		registry.bind(SimulationAdmin.class.getSimpleName(), sas);
		registry.bind(EnvironmentAgent.class.getSimpleName(), eas);	
	}

	/**
	 * Initializes the server with the default environment agent
	 * 
	 * @param port the name server port
	 * @throws Exception
	 */
	public static void init(int port) throws Exception {
		init(port, new EnvironmentAgentImpl());
	}

	/**
	 * @see rollerslam.infrastructure.server.Server#getAgentRegistry()
	 */
	public AgentRegistry getAgentRegistry() throws RemoteException {
		return ari;
	}

	/**
	 * @see rollerslam.infrastructure.server.Server#getDisplayRegistry()
	 */
	public DisplayRegistry getDisplayRegistry() throws RemoteException {
		return dri;
	}

	/**
	 * @see rollerslam.infrastructure.server.Server#getSimulationAdmin()
	 */
	public SimulationAdmin getSimulationAdmin() throws RemoteException {
		return sai;
	}
	
	/**
	 * @param proxyInterface the interface implemented by the proxy
	 * @param remoteAgent the remote agent
	 * @return a proxy for the remote agent
	 */
	public Object getProxyForRemoteAgent(Class proxyInterface, Agent remoteAgent) {
		if (validateAgentProxy(proxyInterface)) {
			return Proxy.newProxyInstance(proxyInterface
					.getClassLoader(), new Class[] { proxyInterface },
					new RemoteAgentInvocationHandler(remoteAgent));
		} else {
			throw new IllegalArgumentException(proxyInterface + " is not a valid proxy interface.");
		}
	}

	@SuppressWarnings("unchecked")
	private boolean validateAgentProxy(Class proxyInterface) {
		if (proxyInterface.isAnnotationPresent(agent.class) &&
		proxyInterface.isInterface()) {
			
			Class[] supers = proxyInterface.getInterfaces();
			
			boolean found = true;
			for (Class class1 : supers) {
				if (class1.equals(Remote.class)) {
					found = true;
					break;
				}
			}
			
			return found;
		}
		return false;
	}
	
}
