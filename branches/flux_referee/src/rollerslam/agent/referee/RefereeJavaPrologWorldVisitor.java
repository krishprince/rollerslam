package rollerslam.agent.referee;

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
import rollerslam.environment.visitor.JavaPrologWorldVisitor;

public class RefereeJavaPrologWorldVisitor implements JavaPrologWorldVisitor {
	
	private java.util.Vector<String> accumulator = new java.util.Vector<String>();
	
	public String getPrologRepresentation(World world) {
		accumulator.clear();
		world.accept(this);
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<accumulator.size();++i) {
			sb.append(accumulator.elementAt(i));
			if (i!=accumulator.size()-1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public void visit(World obj) {
		accumulator.add("team(" + obj.goalA.hashCode() + ")");
		accumulator.add("score(" + obj.scoreboard.scoreTeamA + ", team(" +
				obj.goalA.hashCode() + "))");
		accumulator.add("team(" + obj.goalB.hashCode() + ")");
		accumulator.add("score(" + obj.scoreboard.scoreTeamB + ", team(" +
				obj.goalB.hashCode() + "))");	
	}

	public void visit(WorldObject obj) {
		// TODO Auto-generated method stub		
	}

	public void visit(AnimatedObject obj) {
		// TODO Auto-generated method stub
	}

	public void visit(Ball obj) {
		accumulator.add("ballPosition(" + obj.s.x + "," + obj.s.y + ")");
		accumulator.add("lastBallPosition(" + obj.ls.x + "," + obj.ls.y + ")");
	}

	public void visit(OutTrack obj) {
		// TODO Auto-generated method stub
	}

	public void visit(Player obj) {
		// TODO Auto-generated method stub
	}

	public void visit(Basket obj) {
		// TODO Auto-generated method stub
	}

	public void visit(Goal obj) {
		int y1 = obj.s.y - Math.abs(obj.height/2);
		int y2 = obj.s.y + Math.abs(obj.height/2);
		accumulator.add("golLine(" + obj.s.x + ", " + y1 + ", " + obj.s.x + ", " + y2 + ", team(" + obj.hashCode() + "))");
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

}
