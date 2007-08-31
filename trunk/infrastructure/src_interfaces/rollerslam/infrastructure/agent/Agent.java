package rollerslam.infrastructure.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Agent extends Remote {
	public int getName() throws RemoteException;;
	public void setName(int name) throws RemoteException;;
}