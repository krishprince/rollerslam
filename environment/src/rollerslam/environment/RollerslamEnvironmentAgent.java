package rollerslam.environment;

import java.rmi.RemoteException;

import rollerslam.environment.model.StateMessage;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.server.Message;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import tictactoe.environment.TieTacTorEnvironmentAgent;

public class RollerslamEnvironmentAgent implements RollerslamEnvironment {

	ServerFacade facade = ServerFacadeImpl.getInstance();
	World 		 worldModel = new World();
	
	public void dash(int agentID, int ax, int ay) throws RemoteException {
		// TODO Auto-generated method stub

	}

	public void think() throws RemoteException {
		// TODO Auto-generated method stub

	}

	public Message getEnvironmentState() throws RemoteException {
		return new StateMessage(worldModel);
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ServerFacadeImpl.getInstance().initProxiedEnvironment(1099, new RollerslamEnvironmentAgent());
	}

}
