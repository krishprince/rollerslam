package rollerslam.infrastructure.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Effector extends Remote {

	void doAction(Message m) throws RemoteException;
}
