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

import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Agent;

/**
 * Represents the service that keeps track of all the list of
 * registered agents. Every agent should be registered 
 * in order to be able to take part of the simulation. 
 * 
 * @author maas
 */
public interface AgentRegistry extends Remote {
	/**
	 * Register a new agent. Agents can only be registered during the CREATED state.
	 * 
	 * @param d the Agent to be registered
	 * @throws RemoteException
	 */
	void register(Agent a) throws RemoteException;
	/**
	 * Unregister an agent. If the passed agent has not 
	 * been registered yet this method should have any effect.
	 * 
	 * @param d the Agent to be unregistered
	 * @throws RemoteException
	 */
	void unregister(Agent a) throws RemoteException;
	
}
