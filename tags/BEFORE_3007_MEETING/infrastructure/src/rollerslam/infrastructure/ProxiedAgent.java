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
import java.util.Iterator;
import java.util.Vector;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.server.Message;

/**
 * Proxied agent
 * 
 * @author maas
 */
public class ProxiedAgent implements Agent, Runnable {

	Vector<Message> queue = new Vector<Message>();
	Object target;
	boolean nonBlockingDispatching;
	
	/**
	 * Default constructor 
	 * @param target proxy target
	 */
	public ProxiedAgent(Object target, boolean nonBlockingDispatching) {
		this.target = target;
		this.nonBlockingDispatching = nonBlockingDispatching;
		if (nonBlockingDispatching) {
			new Thread(this).start();
		}
	}

	/**
	 * Default constructor 
	 * @param target proxy target
	 */
	public ProxiedAgent(Object target) {
		this(target, true);
	}
	
	/**
	 * @see rollerslam.agents.Agent#sendPerception(rollerslam.infrastructure.server.Message)
	 */
	public void sendPerception(Message m) throws RemoteException {
		if (this.nonBlockingDispatching) {
			this.queue.add(m);
			synchronized (this) {
				this.notifyAll();
			}
		} else {
			processMessage(m);			
		}
	}

	@SuppressWarnings("unchecked")
	private void processMessage(Message m) throws RemoteException {
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
				targetMethod.invoke(target, (Object[])mcm.parameters);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RemoteException("Exception on proxied agent!", e);
			}
		}		
	}

	public void run() {
		while (true) {
			while (queue.isEmpty()) {
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			synchronized (this) {
				Iterator<Message> it = queue.iterator();
				while (it.hasNext()) {
					Message m = it.next();
					try {
						processMessage(m);
					} catch (RemoteException e) {
						e.printStackTrace();
					}

					it.remove();
				}
			}
		}
	}

}
