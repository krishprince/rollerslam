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

import java.lang.reflect.Method;
import java.rmi.RemoteException;

import rollerslam.agents.Agent;
import rollerslam.infrastructure.server.Message;
import rollerslam.infrastructure.server.MethodCallMessage;

/**
 * Proxied agent
 * 
 * @author maas
 */
public class ProxiedAgent implements Agent {

	Object target;
	
	/**
	 * Default constructor 
	 * @param target proxy target
	 */
	public ProxiedAgent(Object target) {
		this.target = target;
	}
	
	/**
	 * @see rollerslam.agents.Agent#sendPerception(rollerslam.infrastructure.server.Message)
	 */
	public void sendPerception(Message m) throws RemoteException {
		if (m instanceof MethodCallMessage) {
			MethodCallMessage mcm = (MethodCallMessage) m;
			
			Class targetClass = target.getClass();
			Method[] methods = targetClass.getMethods();
			
			Method targetMethod = null;
			for (Method method : methods) {
				if (method.getName().equals(mcm.methodName)) {
					targetMethod = method;
					break;
				}
			}
			
			try {
				targetMethod.invoke(target, mcm.parameters);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RemoteException("Exception on proxied agent!", e);
			}
		}
	}

}
