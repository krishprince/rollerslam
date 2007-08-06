package rollerslam.agent.dummy;

import java.util.Set;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;

@SuppressWarnings("serial")
public class RollerslamDummyAgent implements Agent, Runnable {
	public ClientFacade          facade      = null;	
	public Agent                 remoteThis  = null;

	public boolean gameStarted = false;
	
	public RollerslamDummyAgent() throws Exception {
		facade = ClientFacadeImpl.getInstance();
		remoteThis = (Agent) facade.getClientInitialization().exportObject(this);
		facade.getAgentRegistry().register(remoteThis);
	}

	public void run() {
		try {
			facade.getAgentEffector(remoteThis).doAction(
					new JoinGameAction(remoteThis, PlayerTeam.TEAM_A));

			while (!gameStarted) {
				Set<Message> actions = facade.getAgentSensor(remoteThis).getActions();

				if (!actions.isEmpty()) {
					for (Message message : actions) {
						if (message instanceof GameStartedPerception) {
							gameStarted = true;
						}
					}
				} else {
					Thread.sleep(1000);
				}
			}

			facade.getAgentEffector(remoteThis).doAction(
					new DashAction(remoteThis, new Vector(900, 500)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ClientFacadeImpl.getInstance().getClientInitialization().init("localhost");		
		new Thread(new RollerslamDummyAgent()).run();		
	}
}
