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
package rollerslam.infrastructure;

import java.lang.reflect.Proxy;
import java.rmi.Remote;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.annotations.agent;

/**
 * Default implementation for the ProxyHelper
 * 
 * @author maas
 */
public class ProxyHelperImpl implements ProxyHelper {

	private static ProxyHelperImpl instance = null;
	
	private ProxyHelperImpl() {
		
	}
	
	public static ProxyHelper getInstance() {
		if (instance == null) {
			instance = new ProxyHelperImpl();
		}
		return instance;
	}
		
	/**
	 * @param proxyInterface the interface implemented by the proxy
	 * @param remoteAgent the remote agent
	 * @return a proxy for the remote agent
	 */
	@SuppressWarnings("unchecked")
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
	private static boolean validateAgentProxy(Class proxyInterface) {
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
