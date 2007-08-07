package rollerslam.infrastructure.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Agent extends Remote {
    public int getID() throws RemoteException;
    public void setID(int id) throws RemoteException;
}