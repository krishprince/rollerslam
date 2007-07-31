package rollerslam.agent;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;

public @agent interface RollerslamAgent extends Serializable, Remote {
	@message void gameStarted(int yourAgentID) throws RemoteException;
}
