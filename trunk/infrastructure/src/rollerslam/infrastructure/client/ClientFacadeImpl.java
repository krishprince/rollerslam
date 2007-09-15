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

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Sensor;
import rollerslam.infrastructure.agent.SensorEffectorManager;
import rollerslam.infrastructure.discoverer.client.MulticastServerDiscoverer;
import rollerslam.infrastructure.discoverer.client.ServiceDiscoverer;
import rollerslam.infrastructure.logging.LogRecordingService;
import rollerslam.infrastructure.logging.SelectiveLogRecordingServiceDecorator;
import rollerslam.infrastructure.server.AgentRegistry;
import rollerslam.infrastructure.server.DisplayRegistry;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationAdmin;
import rollerslam.infrastructure.settings.GeneralSettings;

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

    private LogRecordingService lrs;

    private SensorEffectorManager sem;

    private ServiceDiscoverer sd;
    
    private GeneralSettings gs;

    //stores a reference to all exported object so they will not ever be available to garbage collection
    private static Vector<Object> exportedObjects = new Vector<Object>();

    private static String host = null;

    /**
     * Default constructor
     */
    private ClientFacadeImpl() {
        try {
            sd = new MulticastServerDiscoverer();
        } catch (IOException e1) {
            if (PrintTrace.TracePrint) {
                e1.printStackTrace();
            }
        }
    }

    public void init(String nameserver) {
        System.out.println("CONNECTING TO " + nameserver);
        host = nameserver;

        try {

            ar = (AgentRegistry) lookup(AgentRegistry.class.getSimpleName());
            dr = (DisplayRegistry) lookup(DisplayRegistry.class.getSimpleName());
            sa = (SimulationAdmin) lookup(SimulationAdmin.class.getSimpleName());

            sem = (SensorEffectorManager) lookup(SensorEffectorManager.class.getSimpleName());

            gs = (GeneralSettings) lookup(GeneralSettings.class.getSimpleName());
            
            lrs = new SelectiveLogRecordingServiceDecorator((LogRecordingService) lookup(LogRecordingService.class.getSimpleName()), gs);
            
        } catch (Exception e) {
            if (PrintTrace.TracePrint) {
                e.printStackTrace();
            }

            ar = null;
            dr = null;
            sa = null;

            sem = null;
            gs = null;
        }
    }

    private Object lookup(String simpleName) throws RemoteException {
        try {
            return Naming.lookup("rmi://" + host + "/" + simpleName);
        } catch (Exception e) {
            if (PrintTrace.TracePrint) {
                e.printStackTrace();
            }
            throw new RemoteException(e.toString());
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

    /**
     * @see rollerslam.infrastructure.client.ClientFacade#getClientInitialization()
     */
    public ClientInitialization getClientInitialization() throws RemoteException {
        return this;
    }

    /**
     * @see rollerslam.infrastructure.client.ClientFacade#getAgentEffector(rollerslam.infrastructure.agent.Agent)
     */
    public Effector getAgentEffector(Agent ag) throws RemoteException {
        return sem.getAgentEffector(ag);
    }

    /**
     * @see rollerslam.infrastructure.client.ClientFacade#getAgentSensor(rollerslam.infrastructure.agent.Agent)
     */
    public Sensor getAgentSensor(Agent ag) throws RemoteException {
        return sem.getAgentSensor(ag);
    }

    /**
     * @see rollerslam.infrastructure.client.ClientFacade#getLogRecordingService()
     */
    public LogRecordingService getLogRecordingService() throws RemoteException {
        return lrs;
    }

    public ServiceDiscoverer getServiceDiscoverer() throws RemoteException {
        return sd;
    }

    public String getRemoteHost() {
        return host;
    }

    public void init() {
        System.out.println("STARTING AUTO DETECTION");

        try {
            sd.findServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<InetAddress> found = null;

        while ((found = sd.getFoundAddresses()).isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("FOUND: " + found);

        Random r = new Random();
        host = new ArrayList<InetAddress>(found).get(r.nextInt(found.size())).getHostName();
        init(host);
    }

    public GeneralSettings getGeneralSettings() throws RemoteException {
        return gs;
    }
}
