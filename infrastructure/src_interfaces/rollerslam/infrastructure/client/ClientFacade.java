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
package rollerslam.infrastructure.client;

import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Sensor;
import rollerslam.infrastructure.logging.LogRecordingService;
import rollerslam.infrastructure.server.AgentRegistry;
import rollerslam.infrastructure.server.DisplayRegistry;
import rollerslam.infrastructure.server.SimulationAdmin;


/**
 * Main access point for most of the clients. This class locates the
 * name registry and finds the services provided by the server automatically.
 * 
 * @author maas
 */
public interface ClientFacade {
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
	
	/**
	 * @param ag the agent
	 * @return the Effector associated to the given agent
	 * @throws RemoteException
	 */
	Effector getAgentEffector(Agent ag) throws RemoteException;
	
	/**
	 * @param ag the agent
	 * @return the Sensor associated to the given agent
	 * @throws RemoteException
	 */
	Sensor   getAgentSensor(Agent ag) throws RemoteException;
	
	/**
	 * @return the client initialization interface
	 * @throws RemoteException
	 */
	ClientInitialization getClientInitialization() throws RemoteException;
        
        /**
	 * @return the logging service interface
	 * @throws RemoteException
	 */
        LogRecordingService getLogRecordingService() throws RemoteException;
	
}