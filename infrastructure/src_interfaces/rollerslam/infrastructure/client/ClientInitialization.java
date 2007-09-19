package rollerslam.infrastructure.client;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.parctechnologies.eclipse.EclipseConnection;

public interface ClientInitialization {
	/**
	 * Call this method before any other method.
	 * 
	 * @param nameserver the name service host
	 */
	void init(String nameserver);

	/**
	 * Call this method before any other method.
	 * Tries to locate the simulation  
	 */
	void init();
	
	EclipseConnection getEclipseConnection();
	
	/**
	 * Helps clients on registering remote objects. Every object that is supposed to be
	 * accessed remotely should be exported first. 
	 * 
	 * @param obj the object to be exported 
	 * @return the Stub for the object. Pass this object to remote objects.
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	Remote exportObject(Remote obj) throws RemoteException,
			AlreadyBoundException;
	
	String getRemoteHost();
	
}
