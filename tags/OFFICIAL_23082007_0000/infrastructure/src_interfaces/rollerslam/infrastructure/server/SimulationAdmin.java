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

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Allows the control of the simulation
 * 
 * @author maas
 */
public interface SimulationAdmin extends Remote {
	/**
	 * <p>
	 * Starts current simulation. 
	 * If the simulation is already running this method should have no effect. 
	 * </p>
	 * 
	 * <code>
	 * context SimulationAdmin::run()<br>
	 * post: getState() = RUNNING
	 * </code>
	 *  
	 * @throws RemoteException
	 */
	void run() throws RemoteException;
	/**
	 * <p>
	 * Stops current simulation.
	 * If the simulation is already stopped this method should have no effect. 
	 * </p>
	 * 
	 * <code>
	 * context SimulationAdmin::stop()<br>
	 * post: getState() = STOPPED
	 * </code>
	 *  
	 * @throws RemoteException
	 */
	void stop() throws RemoteException;

	/**
	 * @return the current state
	 * @throws RemoteException
	 */
	SimulationState getState() throws RemoteException;	
}
