/**
 * 
 */
package rollerslam.environment;

import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.Basket;
import rollerslam.environment.model.Goal;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.Trampoline;
import rollerslam.environment.model.Ramp;
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
	public void visit(World obj) {
		System.out.println("WORLD");
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.WorldObject)
	 */
	public void visit(WorldObject obj) {
		System.out.println("\tsx: " + obj.s.x + " sy: " + obj.s.y);
		System.out.println("\twx: " + obj.width + " wy: " + obj.height);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.AnimatedObject)
	 */
	public void visit(AnimatedObject obj) {
		this.visit((WorldObject)obj);
		System.out.println("\tvx: " + obj.v.x + " vy: " + obj.v.y);
		System.out.println("\tax: " + obj.a.x + " ay: " + obj.a.y);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.Ball)
	 */
	public void visit(Ball obj) {
		System.out.println("BALL");
		this.visit((AnimatedObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.OutTrack)
	 */
	public void visit(OutTrack obj) {
		System.out.println("OUTTRACK");
		this.visit((WorldObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.Player)
	 */
	public void visit(Player obj) {
		System.out.println("PLAYER");
		System.out.println("\t" + obj.team);
		this.visit((AnimatedObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.Goal)
	 */
	public void visit(Goal obj) {
		System.out.println("GOAL");
		this.visit((WorldObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.RAMP)
	 */
	public void visit(Ramp obj) {
		System.out.println("RAMP");
		this.visit((WorldObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.Trampoline)
	 */
	public void visit(Trampoline obj) {
		System.out.println("TRAMPOLINE");
		this.visit((WorldObject)obj);
	}

	/* (non-Javadoc)
	 * @see rollerslam.environment.model.visitor.Visitor#visit(rollerslam.environment.model.OutTrack)
	 */
	public void visit(Basket obj) {
		System.out.println("BASKET");
		this.visit((WorldObject)obj);
	}
}
