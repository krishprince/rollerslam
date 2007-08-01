package rollerslam.environment;

import java.rmi.RemoteException;
import java.util.Hashtable;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.DashAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.infrastructure.agent.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.EnvironmentStateModel;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

public class JavaActionInterpretationComponent implements ActionInterpretationComponent {
	private static final int MAX_ACCELERATION = 500;

	public ServerFacade facade = ServerFacadeImpl.getInstance();

	public Hashtable<Agent, Player> playersMap                  = new Hashtable<Agent, Player>();
	public Hashtable<Player, Agent> idsMap                      = new Hashtable<Player, Agent>();
	public int 						  nextAgentID 				  = 0;

	
	private void dash(World w, Player p, int ax, int ay) {
		//TODO test if p is in w
		
		p.ax = Math.min(ax, MAX_ACCELERATION);
		p.ay = Math.min(ay, MAX_ACCELERATION);
	}

	public void processAction(EnvironmentStateModel w, Message m) {
		if (m instanceof DashAction) {
			DashAction mt = (DashAction) m;
			this.dash((World)w, playersMap.get(mt.agent), mt.ax, mt.ay);
		} else if (m instanceof JoinGameAction) {
			JoinGameAction mt = (JoinGameAction) m;
			this.joinWorld((World)w, mt.agent, mt.team);			
		}
	}

	public void joinWorld(World worldModel, Agent agent, PlayerTeam playerTeam){

		Player body = null;
		if (playerTeam == PlayerTeam.TEAM_A) {
			body = getBodyForAgent(worldModel.playersA);
		} else {
			body = getBodyForAgent(worldModel.playersB);
		}

		if (body != null) {
			playersMap.put(agent, body);
			idsMap.put(body, agent);
			
			try {
				facade.getEnvironmentEffector().doAction(new GameStartedPerception());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	private Player getBodyForAgent(Player[] players) {
		for (int i = 0; i < players.length; ++i) {
			Agent id = idsMap.get(players[i]);
			if (id == null) {
				return players[i];
			}
		}
		return null;
	}

}
