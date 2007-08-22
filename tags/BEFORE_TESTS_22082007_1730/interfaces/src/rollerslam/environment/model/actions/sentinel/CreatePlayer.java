package rollerslam.environment.model.actions.sentinel;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.actions.SentinelAction;
import rollerslam.infrastructure.agent.Agent;

@SuppressWarnings("serial")
public class CreatePlayer extends SentinelAction {
	public PlayerTeam team;
	
	public CreatePlayer(Agent a, PlayerTeam team){
		super(a);
		this.team = team;
	}
}
