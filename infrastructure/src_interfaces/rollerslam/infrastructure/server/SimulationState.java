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

/**
 * Represents the current simulation state.
 * 
 * @see <a href="http://www.cin.ufpe.br/~in1006/2006/slides/AOKR.ppt">http://www.cin.ufpe.br/~in1006/2006/slides/AOKR.ppt</a>
 * 
 * @author maas
 */
public enum SimulationState {
	/**
	 * Simulation has been paused.
	 */
	STOPPED,
	/**
	 * Simulation is running.
	 */
	RUNNING,
	/**
	 * Simulation has not been started yet.
	 * Agents can only be added during this state.
	 */
	CREATED
}
