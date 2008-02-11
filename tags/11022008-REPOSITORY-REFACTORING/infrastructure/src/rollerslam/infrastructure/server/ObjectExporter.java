package rollerslam.infrastructure.server;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectExporter {
    // this should be visible only IN the server component...
    Remote exportObject(Remote obj) throws RemoteException, AlreadyBoundException;
}
