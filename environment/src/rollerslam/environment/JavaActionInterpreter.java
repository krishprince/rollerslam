package rollerslam.environment;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.World;

public class JavaActionInterpreter implements ActionInterpreter {

	private static final int MAX_ACCELERATION = 500;

	@Override
	public void dash(World w, Player p, int ax, int ay) {
		//TODO test if p is in w
		
		p.ax = Math.min(ax, MAX_ACCELERATION);
		p.ay = Math.min(ay, MAX_ACCELERATION);
	}

}
