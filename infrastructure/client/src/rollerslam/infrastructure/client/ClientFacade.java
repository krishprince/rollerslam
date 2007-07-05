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

import java.lang.reflect.Proxy;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rollerslam.agents.Agent;
import rollerslam.infrastructure.RemoteAgentInvocationHandler;
import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.server.AgentRegistry;
import rollerslam.infrastructure.server.DisplayRegistry;
import rollerslam.infrastructure.server.Server;
import rollerslam.infrastructure.server.SimulationAdmin;

/**
 * Main access point for most of the clients. This class locates the
 * name registry and finds the services provided by the server automatically.
 * 
 * @author maas
 */
public final class ClientFacade implements Server {

	private static ClientFacade instance;
	private AgentRegistry ar;
	private DisplayRegistry dr;
	private SimulationAdmin sa;
	private Registry registry = null;
		
	private static String host = null;
	
	/**
	 * Default constructor
	 */
	private ClientFacade() {
		
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
	 * @return the unique instance for this object
	 */
	public static ClientFacade getInstance() {
		if (instance == null) {
			instance = new ClientFacade();
		}
		return instance;
	}
	
	/**
	 * @see rollerslam.infrastructure.server.Server#getAgentRegistry()
	 */
	public AgentRegistry getAgentRegistry() throws RemoteException {
		return ar;
	}

	/**
	 * @see rollerslam.infrastructure.server.Server#getDisplayRegistry()
	 */
	public DisplayRegistry getDisplayRegistry() throws RemoteException {
		return dr;
	}

	/**
	 * @see rollerslam.infrastructure.server.Server#getSimulationAdmin()
	 */
	public SimulationAdmin getSimulationAdmin() throws RemoteException {
		return sa;
	}

	/**
	 * Helps clients on registering remote objects. Every object that is supposed to be
	 * accessed remotely should be exported first. 
	 * 
	 * @param obj the object to be exported 
	 * @return the Stub for the object. Pass this object to remote objects.
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public Remote exportObject(Remote obj) throws RemoteException, AlreadyBoundException {
		Remote ret = UnicastRemoteObject.exportObject(obj, 0);		
		registry.bind(obj.getClass().getSimpleName()+"_" + obj.hashCode()+"_"+(System.currentTimeMillis()), ret);
		return ret;
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
