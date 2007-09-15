/*
 * LogRecordingServiceImpl.java
 *
 * Created on 10/08/2007, 18:11:10
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import orcas.logcomponents.basiclog.Log;
import orcas.logcomponents.basiclog.LogFactory;
import rollerslam.infrastructure.settings.GeneralSettings;
import rollerslam.infrastructure.settings.GeneralSettingsImpl;


/**
 *
 * @author Weslei
 */
public class LogRecordingServiceImpl implements LogRecordingService {

    private static LogRecordingService instance;
    
    private Log log = null;
    
    private LogRecordingServiceImpl() {
        try {
            Properties p = new Properties();
            GeneralSettings gs = GeneralSettingsImpl.getInstance();
            p.setProperty("concrete.log.class", (String) gs.getSetting(GeneralSettings.LOG_CLASS));

            p.setProperty("log.level", "0");//not used propety, but is required for the background log component
            p.setProperty("db.url", (String) gs.getSetting(GeneralSettings.DB_URL));

            log = LogFactory.getInstance(p).getLog();
        } catch (RemoteException ex) {
            throw new RuntimeException("Error initalizing log service. Details: " + ex, ex);
        }
    }

    public void addEntry(LogEntry entry) throws RemoteException {
        log.log(entry);
    }

    public static LogRecordingService getInstance() {
        if (instance == null) {
            throw new RuntimeException("LogRecording instance not found! Initialize it first.");
        }
        return instance;
    }

    public static synchronized LogRecordingService init() {
        instance = new LogRecordingServiceImpl();
        return getInstance();
    }
    
    public static void main(String[] str) throws Exception {
    	LogRecordingServiceImpl.init();
    	LogRecordingService logRecordingService = LogRecordingServiceImpl.getInstance();

    	LocateRegistry.createRegistry(1099);
    	
    	Remote logExp = UnicastRemoteObject.exportObject(logRecordingService, 0);
    	Naming.rebind(LogRecordingService.class.getSimpleName(), logExp);
    	
    	System.out.println("Logging service exported!");

        Object o = new Object();

        synchronized (o) {
            o.wait();
        }
    }
}
