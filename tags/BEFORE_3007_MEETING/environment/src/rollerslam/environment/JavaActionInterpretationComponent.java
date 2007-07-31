package rollerslam.environment;

import java.rmi.RemoteException;

import rollerslam.agent.RollerslamAgent;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

public class JavaActionInterpretationComponent implements ActionInterpretationComponent {
	private static final int MAX_ACCELERATION = 500;

	public ServerFacade facade = ServerFacadeImpl.getInstance();

	public void dash(World w, Player p, int ax, int ay) {
		//TODO test if p is in w
		
		p.ax = Math.min(ax, MAX_ACCELERATION);
		p.ay = Math.min(ay, MAX_ACCELERATION);
	}

	public void joinWorld(Agent agent, PlayerTeam playerTeam, int id) {
		try {
			((RollerslamAgent)facade.getProxyForRemoteAgent(RollerslamAgent.class, agent)).gameStarted(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}

}
