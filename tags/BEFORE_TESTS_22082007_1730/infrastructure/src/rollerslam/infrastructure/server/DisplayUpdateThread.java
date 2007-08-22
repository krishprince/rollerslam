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
import java.util.Vector;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.display.Display;

/**
 * Thread that runs the simulation. Controls the cycles. 
 * Calls the environment {@link AutomataAgent#think()} method each cycle. 
 * 
 * @author maas
 */
public class DisplayUpdateThread extends Thread {
	private long DISPLAY_UPDATE_INTERVAL = 100;
	private DisplayRegistryExtended displayRegistry = null;
	
	/**
	 * Default constructor
	 * 
	 * @param environment the environment Agent
	 * @param dri 
	 */
	public DisplayUpdateThread(DisplayRegistryExtended dri) {
		this.displayRegistry = dri;
	}

	/**
	 * Runs the simulation
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		while(true) {
			Message state = null;			
			try {
				state = ServerFacadeImpl.getInstance().getSimulationStateProvider().getEnvironmentState();
			} catch (RemoteException e1) {
				if (PrintTrace.TracePrint){
					e1.printStackTrace();
				}
			}
			
//			System.out.println("SENDING STATUS...");
			if (state != null) {
				try {
					Vector<Display> errorDisplay = new Vector<Display>();
					
					for (Display d : displayRegistry.getRegisteredDisplays()) {
						try {
							d.update(state);
						} catch (RemoteException e) {
							errorDisplay.add(d);
//							e.printStackTrace();
						}
					}
					
					for (Display display : errorDisplay) {
						((DisplayRegistry)displayRegistry).unregister(display);
					}
				} catch (RemoteException e) {
					if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
				}
			}
			
//			System.out.println("SLEEPING...");
			try {
				Thread.sleep(DISPLAY_UPDATE_INTERVAL);
			} catch (InterruptedException e) {
				if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
			}
		}
	}
}
