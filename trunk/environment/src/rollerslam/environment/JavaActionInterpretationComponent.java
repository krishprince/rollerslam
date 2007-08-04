package rollerslam.environment;

import java.rmi.RemoteException;
import java.util.Hashtable;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.DashAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.environment.model.utils.Vector;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

public class JavaActionInterpretationComponent implements ActionInterpretationComponent {
	private static final int MAX_ACCELERATION = 500;

	public ServerFacade facade = ServerFacadeImpl.getInstance();

	public Hashtable<Agent, Player> playersMap                  = new Hashtable<Agent, Player>();
	public Hashtable<Player, Agent> idsMap                      = new Hashtable<Player, Agent>();
	public int 						  nextAgentID 				  = 0;

	
	private void dash(World w, Player p, Vector vet) {
		//TODO test if p is in w
		double oax = vet.x / 1000.0;
		double oay = vet.y / 1000.0;
		
		double modulo = Math.sqrt(oax*oax + oay*oay);
		
		if (modulo > MAX_ACCELERATION) modulo = MAX_ACCELERATION;
		
		double nax = (vet.x/modulo);
		double nay = (vet.y/modulo);
		
		p.ax = (int) nax;
		p.ay = (int) nay;
	}

	public void processAction(EnvironmentStateModel w, Message m) {
		if (m instanceof DashAction) {
			DashAction mt = (DashAction) m;
			this.dash((World)w, playersMap.get(mt.sender), mt.acceleration);
		} else if (m instanceof JoinGameAction) {
			JoinGameAction mt = (JoinGameAction) m;
			this.joinWorld((World)w, mt.sender, mt.team);			
		}
	}

	public void joinWorld(World worldModel, Agent agent, PlayerTeam playerTeam){
		
		Player body = playersMap.get(agent);
		
		if (body == null) {
			if (playerTeam == PlayerTeam.TEAM_A) {
				body = getBodyForAgent(worldModel.playersA);
			} else {
				body = getBodyForAgent(worldModel.playersB);
			}
		}

		if (body != null) {
			playersMap.put(agent, body);
			idsMap.put(body, agent);
			
			try {
				facade.getEnvironmentEffector().doAction(new GameStartedPerception(null, body.id));
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
