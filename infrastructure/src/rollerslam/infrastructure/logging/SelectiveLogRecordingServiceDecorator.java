/*
 * SelectiveLogRecordingServiceDecorator.java
 * 
 * Created on 15/09/2007, 15:45:21
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import rollerslam.infrastructure.settings.GeneralSettings;

/**
 *
 * @author Weslei
 */
public class SelectiveLogRecordingServiceDecorator implements LogRecordingService {

    LogRecordingService lrs = null;
    ArrayList<String> classes = null;
    
    public SelectiveLogRecordingServiceDecorator(LogRecordingService other, GeneralSettings setts) {
        try {
            lrs = other;
            String availableClasses = (String) setts.getSetting("INSTANCES_TO_BE_LOGGED");
            classes = new java.util.ArrayList<String>();
            if (!"*".equals(availableClasses)) { //if not logging everything (log everything = empty classes list
                classes.addAll(Arrays.asList(availableClasses.split(",")));
            }
        } catch (RemoteException ex) {
            throw new RuntimeException("Error intializing Selective Log Service Decorator. Details: " + ex, ex);
        }
    }

    public void addEntry(LogEntry entry) throws RemoteException {
        if (!classes.isEmpty()) {//if not set to log everything
            if (classes.contains(entry.getClass().getName())) {
                lrs.addEntry(entry);
                return;
            }
            return;
        } else {
            lrs.addEntry(entry);
        }
    }

}