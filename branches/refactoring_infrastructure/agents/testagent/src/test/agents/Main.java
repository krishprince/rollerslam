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
package test.agents;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import test.environment.TestEnvironment;

/**
 * Main class of the test agent
 * 
 * @author maas
 */
public class Main {

	/**
	 * @param args
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException,
			AlreadyBoundException {
		ClientFacadeImpl.init(args[0]);

		ClientFacade server = ClientFacadeImpl.getInstance();
		TestAgent realAgent = new TestAgent(
				(TestEnvironment) server.getProxyForRemoteAgent(
						TestEnvironment.class, 
						server.getSimulationAdmin().getEnvironmentAgent()));
		
		Agent testAgent = (Agent) server.exportObject(realAgent);

		server.getAgentRegistry().register(testAgent);
		server.getSimulationAdmin().run();
		
		realAgent.start();
	}
}
