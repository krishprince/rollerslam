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

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.server.Server;

/**
 * Main access point for most of the clients. This class locates the
 * name registry and finds the services provided by the server automatically.
 * 
 * @author maas
 */
public interface ClientFacade extends Server {

	/**
	 * Helps clients on registering remote objects. Every object that is supposed to be
	 * accessed remotely should be exported first. 
	 * 
	 * @param obj the object to be exported 
	 * @return the Stub for the object. Pass this object to remote objects.
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	Remote exportObject(Remote obj) throws RemoteException,
			AlreadyBoundException;

	/**
	 * @param proxyInterface the interface implemented by the proxy
	 * @param remoteAgent the remote agent
	 * @return a proxy for the remote agent
	 */
	Object getProxyForRemoteAgent(Class proxyInterface,	Agent remoteAgent);
	
	/**
	 * Creates a proxy for the agent and exports it
	 * 
	 * @param realAgent the agent to be exported
	 * @param agentInterface the interface implemented by the agent
	 * @throws Exception
	 */
	void exportAgent(Object realAgent, Class agentInterface) throws Exception;

	/**
	 * Creates a proxy for the environment and returns it
	 * 
	 * @param environmentInterface the interface implemented by the environment
	 * @throws RemoteException
	 */
	Object getProxiedEnvironment(Class environmentInterface) throws RemoteException;

}