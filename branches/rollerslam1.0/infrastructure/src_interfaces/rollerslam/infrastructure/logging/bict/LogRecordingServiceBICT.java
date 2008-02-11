/*
 * LogRecordingServiceBICT.java
 * 
 * Created on 15/09/2007, 18:42:11
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging.bict;

import java.rmi.RemoteException;
import rollerslam.infrastructure.logging.LogEntry;
import rollerslam.infrastructure.logging.LogRecordingService;

/**
 *
 * @author Weslei
 */
public interface LogRecordingServiceBICT extends LogRecordingService {

    public boolean testAddEntry(LogEntry entry) throws RemoteException;

}
