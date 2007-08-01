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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.EffectorSensorImpl;
import rollerslam.infrastructure.agent.AutomataAgent;
import rollerslam.infrastructure.agent.Sensor;

/**
 * Default implementation for the server facade
 * 
 * @author maas
 */
public class ServerFacadeImpl implements ServerFacade, ServerInitialization {
	
	private static ServerFacade instance = null;
	
	// exported objects
	private static SimulationAdmin sai;
	private static DisplayRegistryImpl dri;
	private static AgentRegistryImpl ari;
	private static Sensor agSensor;
	private static Effector agEffector;
	
	// local objects
	private static Sensor envSensor;
	private static Effector envEffector;	
	
	// required interfaces
	private SimulationStateProvider envStateProvider;
	
	// display updating thread
	private DisplayUpdateThread displayUpdateThread;

	//stores a reference to all exported object so they will not ever be available to garbage collection
	static private Vector<Object> exportedObjects = new Vector<Object>();
	
	/**
	 * @return the unique instance for the server facade
	 */
	public static ServerFacade getInstance() {
		if (instance == null) {
			instance = new ServerFacadeImpl();
		}
		return instance;
	}
	
	private ServerFacadeImpl() {
		dri = new DisplayRegistryImpl();
		ari = new AgentRegistryImpl(sai);
				
		agSensor    = new EffectorSensorImpl(true, false);
		agEffector  = new EffectorSensorImpl(true, false);
		envSensor   = (Sensor) agEffector;
		envEffector = (Effector) agSensor;		
	}
	
	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#init(int, rollerslam.infrastructure.server.AutomataAgent)
	 */
	public void init(int port, AutomataAgent environmentAgent) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1099);

		sai = environmentAgent.getSimulationStateProvider();
		envStateProvider = environmentAgent.getSimulationStateProvider();

		SimulationAdmin sas = (SimulationAdmin) UnicastRemoteObject
				.exportObject(sai, 0);
		AgentRegistry ars = (AgentRegistry) UnicastRemoteObject
				.exportObject((AgentRegistry)ari, 0);
		DisplayRegistry drs = (DisplayRegistry) UnicastRemoteObject
				.exportObject(dri, 0);
		Sensor agSensors = (Sensor) UnicastRemoteObject.exportObject(agSensor,
				0);
		Effector agEffectors = (Effector) UnicastRemoteObject.exportObject(agEffector,
				0);
				
		exportedObjects.add(sai);
		exportedObjects.add(ari);
		exportedObjects.add(dri);
		exportedObjects.add(agSensor);
		exportedObjects.add(agEffector);
		
		exportedObjects.add(sas);
		exportedObjects.add(ars);
		exportedObjects.add(drs);
		exportedObjects.add(agSensors);
		exportedObjects.add(agEffectors);

		registry.bind(AgentRegistry.class.getSimpleName(),   ars);
		registry.bind(DisplayRegistry.class.getSimpleName(), drs);
		registry.bind(SimulationAdmin.class.getSimpleName(), sas);
		registry.bind(Sensor.class.getSimpleName()+"_agent", agSensors);
		registry.bind(Effector.class.getSimpleName()+"_agent", agEffectors);
		
		displayUpdateThread = new DisplayUpdateThread(dri);
		displayUpdateThread.start();
		
		//http://forum.java.sun.com/thread.jspa?threadID=5161052&tstart=180
		Object o = new Object();
		
		synchronized (o) {
			o.wait();			
		}
	}
	
	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#getAgentRegistry()
	 */
	public AgentRegistry getAgentRegistry() throws RemoteException {
		return ari;
	}

	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#getDisplayRegistry()
	 */
	public DisplayRegistry getDisplayRegistry() throws RemoteException {
		return dri;
	}

	/**
	 * @see rollerslam.infrastructure.server.ServerFacade#getSimulationAdmin()
	 */
	public SimulationAdmin getSimulationAdmin() throws RemoteException {
		return sai;
	}

	public Effector getEnvironmentEffector() throws RemoteException {
		return envEffector;
	}

	public Sensor getEnvironmentSensor() throws RemoteException {
		return envSensor;
	}

	public ServerInitialization getServerInitialization()
			throws RemoteException {
		return this;
	}

	public SimulationStateProvider getSimulationStateProvider()
			throws RemoteException {
		return envStateProvider;
	}

	public void setSimulationStateProvider(SimulationStateProvider e)
			throws RemoteException {
		this.envStateProvider = e;
	}
		
}
