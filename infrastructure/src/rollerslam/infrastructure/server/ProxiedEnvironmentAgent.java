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
import java.util.List;
import java.util.Vector;

import rollerslam.infrastructure.ProxiedAgent;

/**
 * Default implementation for the EnvironmentAgent
 * 
 * @author maas
 */
public class ProxiedEnvironmentAgent implements EnvironmentAgent {

	List<Message> messages = new Vector<Message>();
	ProxiedAgent  realAgentProxy = null;
	EnvironmentCycleProcessor realAgent = null;
	
	public ProxiedEnvironmentAgent(EnvironmentCycleProcessor proxyTarget) {		
		realAgent = proxyTarget;
		realAgentProxy = new ProxiedAgent(proxyTarget, false);
	}
	
	/**
	 * @see rollerslam.agents.Agent#sendPerception(rollerslam.infrastructure.server.Message)
	 */
	public void sendPerception(Message m) throws RemoteException {
		synchronized (messages) {
			messages.add(m);
		}
	}

	/**
	 * @throws RemoteException 
	 * @see rollerslam.infrastructure.server.EnvironmentAgent#think()
	 */
	public void think() throws RemoteException {
		synchronized (messages) {
			for (Message m : messages) {
				processMessage(m);
			}
			messages.clear();		
		}
		
		realAgent.think();
	}

	/**
	 * @see rollerslam.infrastructure.server.EnvironmentCycleProcessor#getEnvironmentState()
	 */
	public Message getEnvironmentState() throws RemoteException {
		return realAgent.getEnvironmentState();
	}

	private void processMessage(Message m) throws RemoteException {
		realAgentProxy.sendPerception(m);
	}


}
