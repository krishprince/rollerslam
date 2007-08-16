/*
 * LogRecordingServiceImpl.java
 *
 * Created on 10/08/2007, 18:11:10
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging;

import java.rmi.RemoteException;
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
        log = LogFactory.getInstance().getLog();
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
}
