package rollerslam.environment.model.actions;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class JoinGameAction extends Message {	
	public PlayerTeam team;
	
	public JoinGameAction(Agent a, PlayerTeam team) {
		super(a);
		this.team = team;
	}
}
