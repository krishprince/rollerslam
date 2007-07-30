package rollerslam.environment;

import java.rmi.RemoteException;
import java.util.Hashtable;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.server.Message;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

@SuppressWarnings("serial")
public class RollerslamEnvironmentAgent implements RollerslamEnvironment {
	public ServerFacade               facade                        = ServerFacadeImpl.getInstance();
	public World 		              worldModel                    = new World();
	public RamificationComponent      ramificationsHandler          = new RamificationWorldVisitor();
	public ActionInterpretationComponent          actionInterpretationComponent = new JavaActionInterpretationComponent();
	
	public Hashtable<Integer, Player> playersMap                  = new Hashtable<Integer, Player>();
	public Hashtable<Player, Integer> idsMap                      = new Hashtable<Player, Integer>();
	public int 						  nextAgentID 				  = 0;
	
	public void dash(int agentID, int ax, int ay) throws RemoteException {
		Player p = playersMap.get(agentID);
		if (p != null) {
			actionInterpretationComponent.dash(worldModel, p, ax, ay);
		}
	}

	public void joinWorld(Agent agent, PlayerTeam playerTeam)
			throws RemoteException {							
		Player body = null;
		if (playerTeam == PlayerTeam.TEAM_A) {
			body = getBodyForAgent(worldModel.playersA);
		} else {
			body = getBodyForAgent(worldModel.playersB);
		}
		
		if (body != null) {
			int id = nextAgentID++;
			playersMap.put(id, body);

			actionInterpretationComponent.joinWorld(agent, playerTeam, id);
		}
	}
	
	private Player getBodyForAgent(Player[] players) {
		for (int i=0;i<players.length;++i) {
			Integer id = idsMap.get(players[i]);
			if (id == null) {
				return players[i];
			}
		}
		return null;
	}

	public void think() throws RemoteException {
		ramificationsHandler.processRamifications(worldModel);
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
