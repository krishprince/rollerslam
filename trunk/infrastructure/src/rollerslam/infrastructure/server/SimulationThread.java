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

import rollerslam.infrastructure.display.Display;

/**
 * Thread that runs the simulation. Controls the cycles. 
 * Calls the environment {@link EnvironmentAgent#think()} method each cycle. 
 * 
 * @author maas
 */
public class SimulationThread extends Thread {
	private boolean running = false;
	private long THINKING_INTERVAL = 500;
	private EnvironmentAgent environment = new EnvironmentAgentImpl();
	private DisplayRegistryExtended displayRegistry = null;
	
	/**
	 * Default constructor
	 * 
	 * @param environment the environment Agent
	 * @param dri 
	 */
	public SimulationThread(EnvironmentAgent environment, DisplayRegistryExtended dri) {
		this.environment = environment;
		this.displayRegistry = dri;
	}

	/**
	 * @param b if the simulation should run or be paused
	 */
	public void setRunning(boolean b) {
		this.running = b;
		synchronized (this) {
			notifyAll();
		}
	}

	/**
	 * @return if the simulation is currently running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Runs the simulation
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		
		while(true) {
			if (!running) {
				synchronized (this) {
					try {
						System.out.println("WAITING...");
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			System.out.println("THINKING... " + environment);
			try {
				environment.think();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}

			Message state = null;			
			try {
				state = environment.getEnvironmentState();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
			System.out.println("SENDING STATUS...");
			if (state != null) {
				try {
					for (Display d : displayRegistry.getRegisteredDisplays()) {
						try {
							d.update(state);
						} catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("SLEEPING...");
			try {
				Thread.sleep(THINKING_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
