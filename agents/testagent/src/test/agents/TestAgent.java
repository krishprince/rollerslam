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

package test.agents.testagent;

import java.rmi.RemoteException;
import java.util.Random;

import rollerslam.agents.Agent;
import rollerslam.infrastructure.server.Message;
import test.environment.TestEnvironment;

/**
 * Simple agent for testing purposes.
 * 
 * @author maas
 */
public class TestAgent extends Thread implements Agent {

	private TestEnvironment server;
	
	public TestAgent(TestEnvironment server) {
		this.server = server;
	}
	
	/* (non-Javadoc)
	 * @see rollerslam.agents.Agent#sendPerception(rollerslam.infrastructure.server.Message)
	 */
	public void sendPerception(Message m) throws RemoteException {

	}
	
	public void run() {
		Random r = new Random();
		
		while(true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			int msg = r.nextInt(1000);
			
			System.out.println("SENDING MSG " + msg);
			try {
				server.notifyValue(msg);
			} catch (RemoteException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}
