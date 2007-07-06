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

package test.environment;

import java.rmi.RemoteException;

import rollerslam.infrastructure.server.ProxiedEnvironmentAgent;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

/**
 * TicTacToe Agent
 * 
 * @author maas
 */
public class TestEnvironmentAgent implements TestEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8636889844418101095L;

	/**
	 * @see test.environment.TestEnvironment#notifyValue(int)
	 */
	public void notifyValue(int val) throws RemoteException {
		System.out.println("VALOR RECEBIDO!!!! " + val);
	}

	public void think() throws RemoteException {
		
	}
	
	public static void main(String[] args) throws Exception {
		ServerFacadeImpl.init(1099, new ProxiedEnvironmentAgent(new TestEnvironmentAgent()));
		ServerFacade server = ServerFacadeImpl.getInstance();
		server.getSimulationAdmin().run();
	}

}
