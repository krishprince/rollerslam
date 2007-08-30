package rollerslam.environment;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

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
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.environment.visitor.SampleJavaPrologWorldVisitor;

public class RamifiableObjectsVisitor implements Visitor {

	private Set<WorldObject> ramifiable = new HashSet<WorldObject>();
	
	@Override
	public void visit(World obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(WorldObject obj) {
		ramifiable.add(obj);
	}

	@Override
	public void visit(AnimatedObject obj) {
		ramifiable.add(obj);
	}

	@Override
	public void visit(Ball obj) {
		ramifiable.add(obj);
	}

	@Override
	public void visit(OutTrack obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Player obj) {
		ramifiable.add(obj);
	}

	@Override
	public void visit(Basket obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Goal obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Ramp obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Trampoline obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Scoreboard obj) {
		// TODO Auto-generated method stub

	}

	public String getRamifiableObjects(World w) {
		ramifiable.clear();
		w.accept(this);
		
		StringBuffer sb = new StringBuffer();
		
		Vector<WorldObject> v = new Vector<WorldObject>(ramifiable);
		for (int i=0;i<v.size();++i) {
			sb.append(SampleJavaPrologWorldVisitor.getIDForObject(v.elementAt(i)));
			if (i!=v.size()-1) {
				sb.append(",");
			}
		}
		
		return sb.toString();
	}
}
