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

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.annotations.message;

/**
 * Handles invocations to agent proxies
 * 
 * @author maas
 */
public class RemoteAgentInvocationHandler implements InvocationHandler {

	private Agent remoteAgent = null;
	
	/**
	 * @param remoteAgent the agent to be proxied
	 */
	public RemoteAgentInvocationHandler(Agent remoteAgent) {
		this.remoteAgent = remoteAgent;
	}
	
	/**
	 * The called method should have previously been annotated with <code>@message</code> 
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		if (method.isAnnotationPresent(message.class)) {
			MethodCallMessage msg = new MethodCallMessage();
			msg.methodName = method.getName();
			if (args != null) {
				msg.parameters = new Serializable[args.length];
				for(int i=0;i<args.length;++i) {
					msg.parameters[i] = (Serializable) args[i];				
				}
			} else {
				msg.parameters = new Serializable[0];
			}
			
			remoteAgent.sendPerception(msg);
		}
		
		return null;
	}

}
