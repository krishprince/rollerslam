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

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import rollerslam.agents.Agent;

/**
 * Main class of the server.
 * 
 * @author maas
 */
public class Main {

	/**
	 * Exports the server object.
	 * 
	 * @param args
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException,
			AlreadyBoundException {

		Registry registry = LocateRegistry.createRegistry(1099);

		EnvironmentAgentImpl eai = new EnvironmentAgentImpl();

		EnvironmentAgent eas = (EnvironmentAgent) UnicastRemoteObject
				.exportObject(eai, 0);

		SimulationAdmin sai = new SimulationAdminImpl(eas);
		
		SimulationAdmin sas = (SimulationAdmin) UnicastRemoteObject
				.exportObject(sai, 0);
		AgentRegistry ars = (AgentRegistry) UnicastRemoteObject
				.exportObject(new AgentRegistryImpl(sai), 0);
		DisplayRegistry drs = (DisplayRegistry) UnicastRemoteObject
				.exportObject(new DisplayRegistryImpl(), 0);

		registry.bind(AgentRegistry.class.getSimpleName(),   ars);
		registry.bind(DisplayRegistry.class.getSimpleName(), drs);
		registry.bind(SimulationAdmin.class.getSimpleName(), sas);
		registry.bind(Agent.class.getSimpleName(), eas);
		
		System.out.println("Server ready!");
	}

}
