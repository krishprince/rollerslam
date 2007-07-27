/*
 *  Rollerslam - The Global Fusion Sport of the Third Millennium
 *  Copyright (C) 2007  ORCAS Research Group
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  http://code.google.com/p/rollerslam
 *  
 */
package rollerslam.infrastructure.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rollerslam.infrastructure.ProxyHelperImpl;
import rollerslam.infrastructure.agent.Agent;

/**
 * Default implementation for the server facade
 * 
 * @author maas
 */
public class ServerFacadeImpl implements Server, ServerFacade {
	
	private static ServerFacade instance = null;
	private static AgentRegistry ari;
	private static DisplayRegistryServer dri;
	private static SimulationAdmin sai;
	
	/**
	 * @return the unique instance for the server facade
	 */
	public static ServerFacade getInstance() {
		if (instance == null) {
			instance = new ServerFacadeImpl();
		}
		return instance;
	}
	
	private ServerFacadeImpl() {
		
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

		dri = new DisplayRegistryImpl();
		sai = new SimulationAdminImpl(eas, dri);
		ari = new AgentRegistryImpl(sai);
		
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

	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.server.ServerFacade#getAgentRegistry()
	 */
	public AgentRegistry getAgentRegistry() throws RemoteException {
		return ari;
	}

	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.server.ServerFacade#getDisplayRegistry()
	 */
	public DisplayRegistry getDisplayRegistry() throws RemoteException {
		return dri;
	}

	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.server.ServerFacade#getSimulationAdmin()
	 */
	public SimulationAdmin getSimulationAdmin() throws RemoteException {
		return sai;
	}
	
	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.server.ServerFacade#getProxyForRemoteAgent(java.lang.Class, rollerslam.infrastructure.agent.Agent)
	 */
	public Object getProxyForRemoteAgent(Class proxyInterface, Agent remoteAgent) {
		return ProxyHelperImpl.getInstance().getProxyForRemoteAgent(proxyInterface, remoteAgent);
	}
	
}
