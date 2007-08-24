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
package rollerslam.infrastructure.display;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.agent.Message;

/**
 * Represents a display. The simulation sends messages containing 
 * the state of the simulation at the end of each simulation cycle.
 * Displays can be registered at any moment.
 * 
 * @author maas
 */
public interface Display extends Remote {
	/**
	 * @param m the status message
	 * @throws RemoteException
	 */
	void update(Message m) throws RemoteException;
}
