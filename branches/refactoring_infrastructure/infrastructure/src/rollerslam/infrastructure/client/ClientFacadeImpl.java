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

package rollerslam.infrastructure.client;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rollerslam.infrastructure.ProxiedAgent;
import rollerslam.infrastructure.ProxyHelperImpl;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.server.AgentRegistry;
import rollerslam.infrastructure.server.DisplayRegistry;
import rollerslam.infrastructure.server.SimulationAdmin;

/**
 * Default implementation for the client facade
 * 
 * @author maas
 */
public final class ClientFacadeImpl implements ClientFacade {

	private static ClientFacadeImpl instance;
	private AgentRegistry ar;
	private DisplayRegistry dr;
	private SimulationAdmin sa;
	private Registry registry = null;
		
	private static String host = null;
	
	/**
	 * Default constructor
	 */
	private ClientFacadeImpl() {
		
		try {
			registry = LocateRegistry.getRegistry(host);
			
			ar = (AgentRegistry) registry.lookup(AgentRegistry.class.getSimpleName());
			dr = (DisplayRegistry) registry.lookup(DisplayRegistry.class.getSimpleName());
			sa = (SimulationAdmin) registry.lookup(SimulationAdmin.class.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
			
			ar = null;
			dr = null;
			sa = null;
		}		
	}
	/**
	 * Call this method before invoking getInstance.
	 * 
	 * @param nameserver the name service host
	 */
	public static void init(String nameserver) {
		host = nameserver;
	}
	
	/**
	 * @see rollerslam.infrastructure.client.ClientFacade#exportAgent(java.lang.Object, java.lang.Class)
	 */
	public void exportAgent(Object realAgent, Class agentInterface) throws Exception {
		ClientFacade server = getInstance();

		Agent proxiedAgent = new ProxiedAgent(realAgent);		
		Agent stubAgent = (Agent) server.exportObject(proxiedAgent);
		server.getAgentRegistry().register(stubAgent);
	}
	
	/**
	 * @return the unique instance for this object
	 */
	public static ClientFacade getInstance() {
		if (instance == null) {
			instance = new ClientFacadeImpl();
		}
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.client.ClientFacade#getAgentRegistry()
	 */
	public AgentRegistry getAgentRegistry() throws RemoteException {
		return ar;
	}

	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.client.ClientFacade#getDisplayRegistry()
	 */
	public DisplayRegistry getDisplayRegistry() throws RemoteException {
		return dr;
	}

	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.client.ClientFacade#getSimulationAdmin()
	 */
	public SimulationAdmin getSimulationAdmin() throws RemoteException {
		return sa;
	}

	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.client.ClientFacade#exportObject(java.rmi.Remote)
	 */
	public Remote exportObject(Remote obj) throws RemoteException, AlreadyBoundException {
		Remote ret = UnicastRemoteObject.exportObject(obj, 0);		
		registry.bind(obj.getClass().getSimpleName()+"_" + obj.hashCode()+"_"+(System.currentTimeMillis()), ret);
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.client.ClientFacade#getProxyForRemoteAgent(java.lang.Class, rollerslam.infrastructure.agent.Agent)
	 */
	public Object getProxyForRemoteAgent(Class proxyInterface, Agent remoteAgent) {
		return ProxyHelperImpl.getInstance().getProxyForRemoteAgent(proxyInterface, remoteAgent);
	}

	/* (non-Javadoc)
	 * @see rollerslam.infrastructure.client.ClientFacade#getProxiedEnvironment(java.lang.Class)
	 */
	public Object getProxiedEnvironment(Class environmentInterface)
			throws RemoteException {
		return getProxyForRemoteAgent(environmentInterface,
				getSimulationAdmin().getEnvironmentAgent());
	}

}
