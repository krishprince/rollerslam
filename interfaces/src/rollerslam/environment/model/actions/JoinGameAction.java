package rollerslam.environment.model.actions;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

public class JoinGameAction implements Message {	
	public Agent agent;
	public PlayerTeam team;
	
	public JoinGameAction(Agent a, PlayerTeam team) {
		this.agent = a;
		this.team = team;
	}
}
