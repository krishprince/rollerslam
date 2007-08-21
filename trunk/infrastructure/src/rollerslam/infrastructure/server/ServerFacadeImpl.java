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

import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Sensor;
import rollerslam.infrastructure.agent.SensorEffectorManager;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.discoverer.server.MulticastClientListener;
import rollerslam.infrastructure.logging.LogRecordingService;
import rollerslam.infrastructure.logging.LogRecordingServiceImpl;

/**
 * Default implementation for the server facade
 *
 * @author maas
 */
public class ServerFacadeImpl implements ServerFacade, ServerInitialization {

    private static ServerFacade instance = null;

    // exported objects
    private SimulationAdmin sai;
    private DisplayRegistryImpl dri;
    private AgentRegistryImpl ari;
    private Sensor envSensor;
    private Effector envEffector;
    private LogRecordingService logRecSrv;

    // local exported objects
    private SensorEffectorManager sem;

    // required interfaces
    private SimulationStateProvider envStateProvider;

    // display updating thread
    private DisplayUpdateThread displayUpdateThread;


    //stores a reference to all exported object so they will not ever be available to garbage collection
    private static Vector<Object> exportedObjects = new Vector<Object>();

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
        //initializing log service
        logRecSrv = LogRecordingServiceImpl.init();

        dri = new DisplayRegistryImpl();
        ari = new AgentRegistryImpl();
        sem = new SensorEffectorManagerImpl();

        try {
            envSensor = sem.getEnvironmentSensor();
            envEffector = sem.getEnvironmentEffector();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see rollerslam.infrastructure.server.ServerFacade#init(int, rollerslam.infrastructure.server.AutomataAgent)
     */
    public void init(int port, AutomataAgent environmentAgent) throws Exception {

    	try {
        	LocateRegistry.createRegistry(1099);
        } catch(Exception e) {
        	
        }

        sai = environmentAgent.getSimulationStateProvider();
        envStateProvider = environmentAgent.getSimulationStateProvider();

        SimulationAdmin sas = (SimulationAdmin) UnicastRemoteObject
				.exportObject(sai, 0);
        AgentRegistry ars = (AgentRegistry) UnicastRemoteObject
				.exportObject((AgentRegistry)ari, 0);
        DisplayRegistry drs = (DisplayRegistry) UnicastRemoteObject
				.exportObject(dri, 0);
        SensorEffectorManager sems = (SensorEffectorManager) UnicastRemoteObject
				.exportObject(sem, 0);

        //exporting log service
        
        UnicastRemoteObject.exportObject(logRecSrv, 0);

        exportedObjects.add(sai);
        exportedObjects.add(ari);
        exportedObjects.add(dri);
        exportedObjects.add(sem);

        exportedObjects.add(sas);
        exportedObjects.add(ars);
        exportedObjects.add(drs);
        exportedObjects.add(sems);
        
        //adding log to exported objs
        exportedObjects.add(logRecSrv);

        bind(AgentRegistry.class.getSimpleName(), ars);
        bind(DisplayRegistry.class.getSimpleName(), drs);
        bind(SimulationAdmin.class.getSimpleName(), sas);
        bind(SensorEffectorManager.class.getSimpleName(), sems);
        
        //binding log service to registry
        bind(LogRecordingService.class.getSimpleName(), logRecSrv);

        displayUpdateThread = new DisplayUpdateThread(dri);
        displayUpdateThread.start();

        // starts multicast listener
        new MulticastClientListener().start();
        
        //http://forum.java.sun.com/thread.jspa?threadID=5161052&tstart=180
        Object o = new Object();

        synchronized (o) {
            o.wait();
        }
    }

    /**
     * @see rollerslam.infrastructure.client.ClientFacade#exportObject(java.rmi.Remote)
     */
    public Remote exportObject(Remote obj) throws RemoteException, AlreadyBoundException {
        Remote ret = UnicastRemoteObject.exportObject(obj, 0);
        bind(obj.getClass().getSimpleName() + "_" + obj.hashCode() + "_" + (System.currentTimeMillis()), ret);

        exportedObjects.add(obj);
        exportedObjects.add(ret);

        return ret;
    }

    private void bind(String string, Remote ret) throws RemoteException {
    	try {
			Naming.bind(string, ret);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RemoteException(e.toString());
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

    /**
     * @see rollerslam.infrastructure.server.ServerFacade#getEnvironmentEffector()
     */
    public Effector getEnvironmentEffector() throws RemoteException {
        return envEffector;
    }

    /**
     * @see rollerslam.infrastructure.server.ServerFacade#getEnvironmentSensor()
     */
    public Sensor getEnvironmentSensor() throws RemoteException {
        return envSensor;
    }

    /**
     * @see rollerslam.infrastructure.server.ServerFacade#getServerInitialization()
     */
    public ServerInitialization getServerInitialization() throws RemoteException {
        return this;
    }

    /**
     * @see rollerslam.infrastructure.server.ServerFacade#getSimulationStateProvider()
     */
    public SimulationStateProvider getSimulationStateProvider() throws RemoteException {
        return envStateProvider;
    }

    /**
     * @see rollerslam.infrastructure.server.ServerFacade#setSimulationStateProvider(rollerslam.infrastructure.server.SimulationStateProvider)
     */
    public void setSimulationStateProvider(SimulationStateProvider e) throws RemoteException {
        this.envStateProvider = e;
    }

    /**
     * @see rollerslam.infrastructure.server.ServerFacade#getSensorEffectorManager()
     */
    public SensorEffectorManager getSensorEffectorManager() throws RemoteException {
        return sem;
    }

    public LogRecordingService getLogRecordingService() throws RemoteException {
        return logRecSrv;
    }
}
