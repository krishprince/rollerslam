package rollerslam.infrastructure.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;


public interface Sensor extends Remote {
	Set<Message> getActions() throws RemoteException;
}
