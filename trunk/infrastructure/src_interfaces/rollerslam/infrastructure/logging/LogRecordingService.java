package rollerslam.infrastructure.logging;

import rollerslam.loggingservices.recording.LogEntry;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface LogRecordingService extends Remote {

	void addEntry(LogEntry entry) throws RemoteException;
        
}
