package rollerslam.environment;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.World;

public interface ActionInterpreter {
	void dash(World w, Player p, int ax, int ay);	
}
