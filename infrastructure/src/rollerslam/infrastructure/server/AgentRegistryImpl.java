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
import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.agent.Agent;

/**
 * Default implementation for the AgentRegistry.
 * 
 * @author maas
 */
public class AgentRegistryImpl implements AgentRegistryServer {

	private Set<Agent> agents = new HashSet<Agent>();
		
	/**
	 * @throws RemoteException 
	 * @see rollerslam.infrastructure.server.AgentRegistry#register(rollerslam.agents.Agent)
	 */
	public void register(Agent a) throws RemoteException {
			agents.add(a);
		ServerFacadeImpl.getInstance().getSensorEffectorManager()
				.registerAgent(a);
	}

	/**
	 * @see rollerslam.infrastructure.server.AgentRegistry#unregister(rollerslam.agents.Agent)
	 */
	public void unregister(Agent a) {
		agents.remove(a);
		try {
			ServerFacadeImpl.getInstance().getSensorEffectorManager().unregisterAgent(a);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see rollerslam.infrastructure.server.AgentRegistryExtended#getRegisteredAgents()
	 */
	public Set<Agent> getRegisteredAgents() {
		return agents;
	}

}
