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

import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Sensor;

/**
 * Server facade.
 * Used mainly to start the server using default configurations.
 * 
 * @author maas
 */
public interface ServerFacade {
	/**
	 * @return the DisplayRegistry port.
	 * @throws RemoteException
	 */
	DisplayRegistry getDisplayRegistry() throws RemoteException;
	/**
	 * @return the AgentRegistry port.
	 * @throws RemoteException
	 */
	AgentRegistry   getAgentRegistry() throws RemoteException;
	/**
	 * @return the SimulationAdmin port.
	 * @throws RemoteException
	 */
	SimulationAdmin getSimulationAdmin() throws RemoteException;
		
	Effector getEnvironmentEffector() throws RemoteException;
	
	Sensor getEnvironmentSensor() throws RemoteException;
	
	ServerInitialization getServerInitialization() throws RemoteException;

	SimulationStateProvider getSimulationStateProvider() throws RemoteException;
	
	void setSimulationStateProvider(SimulationStateProvider e) throws RemoteException;
}