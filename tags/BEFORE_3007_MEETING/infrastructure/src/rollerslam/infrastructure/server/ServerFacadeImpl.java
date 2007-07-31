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
import java.util.Vector;

import rollerslam.infrastructure.ProxyHelperImpl;
import rollerslam.infrastructure.agent.Agent;

/**
 * Default implementation for the server facade
 * 
 * @author maas
 */
public class ServerFacadeImpl implements Server, ServerFacade {
	
	private static ServerFacade instance = null;
	private static AgentRegistryServer ari;
	private static DisplayRegistryServer dri;
	private static SimulationAdmin sai;
	
	//stores a reference to all exported object so they will not ever be available to garbage collection
	static private Vector<Object> exportedObjects = new Vector<Object>();
	
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
	 * @see rollerslam.infrastructure.server.ServerFacade#init(int, rollerslam.infrastructure.server.EnvironmentAgent)
	 */
	public void init(int port, EnvironmentAgent environmentAgent) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1099);

		EnvironmentAgent eas = (EnvironmentAgent) UnicastRemoteObject
				.exportObject(environmentAgent, 0);

		exportedObjects.add(environmentAgent);
		exportedObjects.add(eas);
		
		dri = new DisplayRegistryImpl();
		sai = new SimulationAdminImpl(eas, dri);
		ari = new AgentRegistryImpl(sai);
		
		SimulationAdmin sas = (SimulationAdmin) UnicastRemoteObject
				.exportObject(sai, 0);
		AgentRegistry ars = (AgentRegistry) UnicastRemoteObject
				.exportObject((AgentRegistry)ari, 0);
		DisplayRegistry drs = (DisplayRegistry) UnicastRemoteObject
				.exportObject(dri, 0);

		exportedObjects.add(sai);
		exportedObjects.add(ari);
		exportedObjects.add(dri);
		exportedObjects.add(sas);
		exportedObjects.add(ars);
		exportedObjects.add(drs);

		registry.bind(AgentRegistry.class.getSimpleName(),   ars);
		registry.bind(DisplayRegistry.class.getSimpleName(), drs);
		registry.bind(SimulationAdmin.class.getSimpleName(), sas);
		registry.bind(EnvironmentAgent.class.getSimpleName(), eas);
		
		//http://forum.java.sun.com/thread.jspa?threadID=5161052&tstart=180
		Object o = new Object();
		
		synchronized (o) {
			o.wait();			
		}
	}

	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#initProxiedEnvironment(int, rollerslam.infrastructure.server.EnvironmentAgent)
	 */
	public void initProxiedEnvironment(int port,
			EnvironmentCycleProcessor environmentAgent) throws Exception {
		init(port, new ProxiedEnvironmentAgent(environmentAgent));		
	}
	
	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#init(int)
	 */
	public void init(int port) throws Exception {
		init(port, new EnvironmentAgentImpl());
	}

	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#getProxyForRemoteAgent(java.lang.Class, rollerslam.infrastructure.agent.Agent)
	 */
	@SuppressWarnings("unchecked")
	public Object getProxyForRemoteAgent(Class proxyInterface,	Agent remoteAgent) {
		return ProxyHelperImpl.getInstance().getProxyForRemoteAgent(proxyInterface, remoteAgent);
	}
	
	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#getAgentRegistry()
	 */
	public AgentRegistry getAgentRegistry() throws RemoteException {
		return ari;
	}

	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#getDisplayRegistry()
	 */
	public DisplayRegistry getDisplayRegistry() throws RemoteException {
		return dri;
	}

	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#getSimulationAdmin()
	 */
	public SimulationAdmin getSimulationAdmin() throws RemoteException {
		return sai;
	}
		
}