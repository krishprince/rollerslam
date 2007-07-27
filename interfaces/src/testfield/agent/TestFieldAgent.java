package testfield.agent;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;

public @agent interface TestFieldAgent extends Serializable, Remote {
	@message void feel(int sx, int sy, int vx, int vy, int ax, int ay) throws RemoteException;
}
