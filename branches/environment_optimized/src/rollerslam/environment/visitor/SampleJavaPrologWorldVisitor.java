package rollerslam.environment.visitor;

import java.util.Collection;

import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.Basket;
import rollerslam.environment.model.Goal;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.Ramp;
import rollerslam.environment.model.Scoreboard;
import rollerslam.environment.model.Trampoline;
import rollerslam.environment.model.World;
import rollerslam.environment.model.WorldObject;
import rollerslam.environment.model.utils.Vector;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.CompoundTermImpl;

public class SampleJavaPrologWorldVisitor implements JavaPrologWorldVisitor {

	private java.util.Vector<CompoundTerm> accumulator = new java.util.Vector<CompoundTerm>();

	public static CompoundTerm getIDForObject(WorldObject obj) {
		if (obj instanceof Ball) {
			return new Atom("ball");
		} else if (obj instanceof Player) {
			return new CompoundTermImpl("player", ((Player) obj).id);
		} else {
			return new CompoundTermImpl("obj", obj.hashCode());
		}
	}

	private CompoundTerm vectorToString(Vector v) {
		return new CompoundTermImpl("vector",  v.x, v.y);
	}

	public void visit(World obj) {

	}

	public void visit(WorldObject obj) {

		accumulator.add( new CompoundTermImpl("scoreA", obj.world.scoreboard.scoreTeamA));
		accumulator.add( new CompoundTermImpl("scoreB", obj.world.scoreboard.scoreTeamB));
		accumulator.add( new CompoundTermImpl("position", getIDForObject(obj), vectorToString(obj.s)));
		
	}

	public void visit(AnimatedObject obj) {
		visit((WorldObject) obj);

		accumulator.add( new CompoundTermImpl("acceleration" , getIDForObject(obj) ,
				vectorToString(obj.a) ));
		accumulator.add( new CompoundTermImpl("speed", getIDForObject(obj) ,
				vectorToString(obj.v)));
		accumulator.add( new CompoundTermImpl("maxSpeed", getIDForObject(obj), obj.maxV
				));
		accumulator.add( new CompoundTermImpl("maxAcceleration", getIDForObject(obj),
				obj.maxA ));
	}

	public void visit(Ball obj) {
		visit((AnimatedObject) obj);
		if (obj.withPlayer()) {
			accumulator.add( new CompoundTermImpl("withPlayer", getIDForObject(obj) ));
		}
		accumulator.add( new CompoundTermImpl("attrition", 10));
	}

	public void visit(OutTrack obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Player obj) {
		visit((AnimatedObject) obj);
		if (obj.hasBall()) {
			 accumulator.add( new CompoundTermImpl("hasBall", getIDForObject(obj)));
		}
		if (obj.inGround) {
			accumulator.add( new CompoundTermImpl("inGround", getIDForObject(obj)));
		}
		if (obj.counterTackle) {
			accumulator.add( new CompoundTermImpl("counterTackle", getIDForObject(obj) ));
		}

	}

	public void visit(Basket obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Goal obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Ramp obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Trampoline obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Scoreboard obj) {
		// TODO Auto-generated method stub

	}

	public Collection getPrologRepresentation(World world) {
		accumulator.clear();
		world.accept(this);
		return accumulator;
	}

}
