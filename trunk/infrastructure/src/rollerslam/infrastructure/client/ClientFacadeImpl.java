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

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Sensor;
import rollerslam.infrastructure.server.AgentRegistry;
import rollerslam.infrastructure.server.DisplayRegistry;
import rollerslam.infrastructure.server.SimulationAdmin;

/**
 * Default implementation for the client facade
 * 
 * @author maas
 */
public final class ClientFacadeImpl implements ClientFacade, ClientInitialization {

	private static ClientFacadeImpl instance;

	private AgentRegistry ar;
	private DisplayRegistry dr;
	private SimulationAdmin sa;
	
	private Effector agEff;
	private Sensor   agSensor;

	private Registry registry;
	
	//stores a reference to all exported object so they will not ever be available to garbage collection
	static private Vector<Object> exportedObjects = new Vector<Object>();
	
	private static String host = null;	

	/**
	 * Default constructor
	 */
	private ClientFacadeImpl() {
	}
	
	public void init(String nameserver) {
		host = nameserver;

		try {
			registry = LocateRegistry.getRegistry(host);
			
			ar = (AgentRegistry) registry.lookup(AgentRegistry.class.getSimpleName());
			dr = (DisplayRegistry) registry.lookup(DisplayRegistry.class.getSimpleName());
			sa = (SimulationAdmin) registry.lookup(SimulationAdmin.class.getSimpleName());
			
			agEff    = (Effector) registry.lookup(Effector.class.getSimpleName()+"_agent");
			agSensor = (Sensor)   registry.lookup(Sensor.class.getSimpleName()+"_agent");						
		} catch (Exception e) {
			e.printStackTrace();
			
			ar = null;
			dr = null;
			sa = null;
			
			agEff = null;
			agSensor = null;
		}		
	}
	
	/**
	 * @return the unique instance for this object
	 */
	public static ClientFacade getInstance() {
		if (instance == null) {
			instance = new ClientFacadeImpl();
		}
		return instance;
	}
	
	/**
	 * @see rollerslam.infrastructure.client.ClientFacade#exportObject(java.rmi.Remote)
	 */
	public Remote exportObject(Remote obj) throws RemoteException, AlreadyBoundException {
		Remote ret = UnicastRemoteObject.exportObject(obj, 0);		
		registry.bind(obj.getClass().getSimpleName()+"_" + obj.hashCode()+"_"+(System.currentTimeMillis()), ret);
		
		exportedObjects.add(obj);
		exportedObjects.add(ret);
		
		return ret;
	}		
	/**
	 * @see rollerslam.infrastructure.client.ClientFacade#getAgentRegistry()
	 */
	public AgentRegistry getAgentRegistry() throws RemoteException {
		return ar;
	}

	/**
	 * @see rollerslam.infrastructure.client.ClientFacade#getDisplayRegistry()
	 */
	public DisplayRegistry getDisplayRegistry() throws RemoteException {
		return dr;
	}

	/**
	 * @see rollerslam.infrastructure.client.ClientFacade#getSimulationAdmin()
	 */
	public SimulationAdmin getSimulationAdmin() throws RemoteException {
		return sa;
	}

	public Effector getAgentEffector() throws RemoteException {
		return agEff;
	}

	public Sensor getAgentSensor() throws RemoteException {
		return agSensor;
	}

	public ClientInitialization getClientInitialization()
			throws RemoteException {
		return this;
	}

	
}
