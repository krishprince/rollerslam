package rollerslam.environment;


import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.World;
import rollerslam.environment.model.WorldObject;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.RamificationComponent;

/**
 * @author Marcos Aurélio
 *
 */
public class RamificationWorldVisitor implements Visitor, RamificationComponent {

	private static final int MAX_SPEED = 1000;
	
	public void visit(World obj) {
	}

	public void visit(WorldObject obj) {

	}

	public void visit(AnimatedObject obj) {
		obj.sx = obj.sx + obj.vx;
		obj.sy = obj.sy + obj.vy;

		obj.vx = Math.min(obj.vx + obj.ax, MAX_SPEED);
		obj.vy = Math.min(obj.vy + obj.ay, MAX_SPEED);		
	}

	public void visit(Ball obj) {
		this.visit((AnimatedObject)obj);
	}

	public void visit(OutTrack obj) {
	}

	public void visit(Player obj) {
		this.visit((AnimatedObject)obj);
	}

	public void processRamifications(EnvironmentStateModel world) {
		((World)world).accept(this);
	}

}
