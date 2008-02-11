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

import rollerslam.infrastructure.display.Display;

/**
 * Represents the service that keeps track of all the list of
 * registered displays. Every display should be registered 
 * in order to be able to receive state messages. 
 * 
 * @author maas
 */
public interface DisplayRegistry extends Remote {
	/**
	 * Register a new display
	 * 
	 * @param d the Display to be registered
	 * @throws RemoteException
	 */
	void register(Display d) throws RemoteException;
	/**
	 * Unregister a display. If the passed display has not 
	 * been registered yet this method should have any effect.
	 * 
	 * @param d the Display to be unregistered
	 * @throws RemoteException
	 */
	void unregister(Display d) throws RemoteException;
}
