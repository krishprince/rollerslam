package rollerslam.infrastructure.agent;

import java.io.Serializable;
import java.rmi.RemoteException;

@SuppressWarnings("serial")
public class SimpleAgent implements Agent, Serializable {
	private int name;
	
	public int getName() throws RemoteException{
		return name;
	}
	
	public void setName(int name) throws RemoteException{
		this.name = name;
	}
}
