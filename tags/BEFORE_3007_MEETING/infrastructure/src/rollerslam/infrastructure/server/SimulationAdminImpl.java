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

import rollerslam.infrastructure.agent.Agent;

/**
 * Default implementation for the SimulationAdmin
 * 
 * @author maas
 */
public class SimulationAdminImpl implements SimulationAdmin {

	private SimulationState state = SimulationState.CREATED;
	private SimulationThread simulationThread;
	private EnvironmentAgent environment;
	
	/**
	 * Default constructor 
	 * 
	 * @param environmentAgent
	 * @param dri 
	 */
	public SimulationAdminImpl(EnvironmentAgent environmentAgent, DisplayRegistryServer dri) {
		this.environment = environmentAgent;
		this.simulationThread = new SimulationThread(environment, dri);
		simulationThread.start();
	}
	
	/**
	 * @see rollerslam.infrastructure.server.SimulationAdmin#getState()
	 */
	public SimulationState getState() throws RemoteException {
		return state;
	}

	/**
	 * @see rollerslam.infrastructure.server.SimulationAdmin#run()
	 */
	public void run() throws RemoteException {
		state = SimulationState.RUNNING;
		simulationThread.setRunning(true);
	}

	/**
	 * @see rollerslam.infrastructure.server.SimulationAdmin#stop()
	 */
	public void stop() throws RemoteException {
		state = SimulationState.STOPPED;
		simulationThread.setRunning(false);
	}

	/**
	 * @see rollerslam.infrastructure.server.SimulationAdmin#getEnvironmentAgent()
	 */
	public Agent getEnvironmentAgent() throws RemoteException {
		return environment;
	}

}
