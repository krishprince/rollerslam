/**
 * 
 */
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
public class DumpWorldVisitor implements Visitor {

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.World)
	 */
	@Override
	public void visit(World obj) {
		System.out.println("WORLD");
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.WorldObject)
	 */
	@Override
	public void visit(WorldObject obj) {
		System.out.println("\tsx: " + obj.sx + " sy: " + obj.sy);
		System.out.println("\twx: " + obj.width + " wy: " + obj.height);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.AnimatedObject)
	 */
	@Override
	public void visit(AnimatedObject obj) {
		this.visit((WorldObject)obj);
		System.out.println("\tvx: " + obj.vx + " vy: " + obj.vy);
		System.out.println("\tax: " + obj.ax + " ay: " + obj.ay);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.Ball)
	 */
	@Override
	public void visit(Ball obj) {
		System.out.println("BALL");
		this.visit((AnimatedObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.OutTrack)
	 */
	@Override
	public void visit(OutTrack obj) {
		System.out.println("OUTTRACK");
		this.visit((WorldObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.Player)
	 */
	@Override
	public void visit(Player obj) {
		System.out.println("PLAYER");
		System.out.println("\t" + obj.team);
		this.visit((AnimatedObject)obj);
	}

}
