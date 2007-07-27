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

import rollerslam.infrastructure.agent.Agent;

/**
 * Server facade.
 * Used mainly to start the server using default configurations.
 * 
 * @author maas
 */
public interface ServerFacade extends Server {

	/**
	 * Initializes the server using the passed agent as the environment agent
	 * 
	 * @param port the name server port
	 * @param environmentAgent the environment agent
	 * @throws Exception
	 */
	void init(int port, EnvironmentAgent environmentAgent) throws Exception;

	/**
	 * Initializes the server using the passed agent as the environment agent.
	 * Assumes that the environment needs to be proxied.
	 * 
	 * @param port the name server port
	 * @param environmentAgent the environment agent
	 * @throws Exception
	 */
	void initProxiedEnvironment(int port, EnvironmentCycleProcessor environmentAgent) throws Exception;

	/**
	 * @param proxyInterface the interface implemented by the proxy
	 * @param remoteAgent the remote agent
	 * @return a proxy for the remote agent
	 */
	@SuppressWarnings("unchecked")
	Object getProxyForRemoteAgent(Class proxyInterface,	Agent remoteAgent);
}