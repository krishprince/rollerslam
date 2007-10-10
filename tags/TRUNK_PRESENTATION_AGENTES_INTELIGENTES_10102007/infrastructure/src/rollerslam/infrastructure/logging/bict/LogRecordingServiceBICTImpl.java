/*
 * LogRecordingServiceBICTImpl.java
 * 
 * Created on 15/09/2007, 18:55:45
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging.bict;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Properties;
import orcas.logcomponents.basiclog.AbstractLog;
import rollerslam.infrastructure.logging.LogEntry;
import rollerslam.infrastructure.logging.LogRecordingServiceImpl;

/**
 *
 * @author Weslei
 */
public class LogRecordingServiceBICTImpl extends LogRecordingServiceImpl implements LogRecordingServiceBICT {

    public LogRecordingServiceBICTImpl(Properties p) {
        super(p);
    }
    
    boolean addEntryCalled = false;
    
    public void addEntry(LogEntry entry) throws RemoteException {
        addEntryCalled = true;
        super.addEntry(entry);
    }
    
    public boolean testAddEntry(LogEntry entry) throws RemoteException {
        this.addEntry(entry);
        //this.addEntry(null);
        return addEntryCalled && ((LogClass)this.log).entries.contains(entry);
    }
    
    public static void main(String args[]) throws Exception {
        Properties p = new Properties();
        
        p.setProperty("concrete.log.class", "rollerslam.infrastructure.logging.bict.LogRecordingServiceBICTImpl$LogClass");
        p.setProperty("log.level", "0");
        
        LogRecordingServiceBICTImpl lrsbi = new LogRecordingServiceBICTImpl(p);
        
        LogEntry le = new TestLogEntry(10, 20);
        boolean res = lrsbi.testAddEntry(le);
        if (!res) {
            System.out.println("Test not satisfied.");
        } else {
            System.out.println("Test satisfied.");
        }
    }
    
    public static class LogClass extends AbstractLog {
        protected ArrayList entries = new ArrayList();
        protected void init(Properties logProperties) {}

        protected void doLog(Serializable message) {
            entries.add(message);
        }
    }
    private static class TestLogEntry extends LogEntry {
        protected TestLogEntry(int x, int y) {
            super(x, y);
        }
    }

}
