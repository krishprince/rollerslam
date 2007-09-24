package rollerslam.environment.visitor;

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

public class SampleJavaPrologWorldVisitor implements JavaPrologWorldVisitor {

	private java.util.Vector<String> accumulator = new java.util.Vector<String>();

	public static String getIDForObject(WorldObject obj) {
		if (obj instanceof Ball) {
			return "ball";
		} else if (obj instanceof Player) {
			return "player(" + ((Player) obj).id + ")";
		} else {
			return "obj(" + obj.hashCode() + ")";
		}
	}

	private String vectorToString(Vector v) {
		return "vector(" + v.x + "," + v.y + ")";
	}

	public void visit(World obj) {
		// TODO Auto-generated method stub

	}

	public void visit(WorldObject obj) {
		accumulator.add("position(" + getIDForObject(obj) + ","
				+ vectorToString(obj.s) + ")");
	}

	public void visit(AnimatedObject obj) {
		visit((WorldObject) obj);

		accumulator.add("acceleration(" + getIDForObject(obj) + ","
				+ vectorToString(obj.a) + ")");
		accumulator.add("speed(" + getIDForObject(obj) + ","
				+ vectorToString(obj.v) + ")");
	}

	public void visit(Ball obj) {
		visit((AnimatedObject) obj);
		if (obj.withPlayer) {
			accumulator.add("withPlayer(" + getIDForObject(obj) + ")");
		}
		if (obj.isMoving){
			double error = Math.random();
			accumulator.add("isMoving("+getIDForObject(obj)+")");
			accumulator.add("attrition("+error+")");
		}
		accumulator.add("maxSpeed(" + obj.maxV + ")");
		accumulator.add("maxAcceleration(" + obj.maxA + ")");
	}

	public void visit(OutTrack obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Player obj) {
		visit((AnimatedObject) obj);
		if (obj.hasBall) {
			accumulator.add("hasBall(" + getIDForObject(obj) + ")");
		}
		if (obj.inGround) {
			accumulator.add("inGround(" + getIDForObject(obj) + ")");
		}
		if (obj.counterTackle) {
			accumulator.add("counterTackle(" + getIDForObject(obj) + ")");
		}

		accumulator.add("maxSpeed(" + obj.maxV + ")");
		accumulator.add("maxAcceleration(" + obj.maxA + ")");
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

	public String getPrologRepresentation(World world) {
		accumulator.clear();
		world.accept(this);

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < accumulator.size(); ++i) {
			sb.append(accumulator.elementAt(i));
			if (i != accumulator.size() - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

}
