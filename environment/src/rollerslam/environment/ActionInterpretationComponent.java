package rollerslam.environment;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Agent;

public interface ActionInterpretationComponent {
	void dash(World w, Player p, int ax, int ay);
	void joinWorld(Agent agent, PlayerTeam playerTeam, int id);
}
