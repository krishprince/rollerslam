package rollerslam.environment;


import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.World;
import rollerslam.environment.model.WorldObject;
import rollerslam.environment.model.visitor.Visitor;

/**
 * @author Marcos Aurélio
 *
 */
public class RamificationWorldVisitor implements Visitor {

	private static final int MAX_SPEED = 1000;
	
	@Override
	public void visit(World obj) {
	}

	@Override
	public void visit(WorldObject obj) {

	}

	@Override
	public void visit(AnimatedObject obj) {
		obj.sx = obj.sx + obj.vx;
		obj.sy = obj.sy + obj.vy;

		obj.vx = Math.min(obj.vx + obj.ax, MAX_SPEED);
		obj.vy = Math.min(obj.vy + obj.ay, MAX_SPEED);		
	}

	@Override
	public void visit(Ball obj) {
		this.visit((AnimatedObject)obj);
	}

	@Override
	public void visit(OutTrack obj) {
	}

	@Override
	public void visit(Player obj) {
		this.visit((AnimatedObject)obj);
	}
}
