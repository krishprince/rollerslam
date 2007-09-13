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


/**
 *
 * @author Weslei
 */
public class LogRecordingServiceImpl implements LogRecordingService {

    private static LogRecordingService instance;
    
    private Log log = null;
    
    private LogRecordingServiceImpl() {
        Properties p = new Properties();

        //TODO change to generate log
        p.setProperty("concrete.log.class", "rollerslam.logging.HSQLDBLog");
        //p.setProperty("concrete.log.class", "rollerslam.logging.NullLog");
        p.setProperty("log.level", "0");
        p.setProperty("db.url", "file:///c://temp/");

        log = LogFactory.getInstance(p).getLog();
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
