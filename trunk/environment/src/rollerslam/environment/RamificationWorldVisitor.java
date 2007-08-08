package rollerslam.environment;


import java.util.ArrayList;

import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.Basket;
import rollerslam.environment.model.Fact;
import rollerslam.environment.model.Goal;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.Ramp;
import rollerslam.environment.model.Trampoline;
import rollerslam.environment.model.World;
import rollerslam.environment.model.WorldObject;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.RamificationComponent;

/**
 * @author Marcos Aurélio
 *
 */
public class RamificationWorldVisitor implements Visitor, RamificationComponent {

	private static final int MAX_SPEED = 2000;
	
	public void visit(World obj) {
		ArrayList<Fact> factsToRemove = new ArrayList<Fact>();
		for (Fact fact : obj.saidMessages) {
			if (fact.cycle < obj.currentCycle) {
				factsToRemove.add(fact);
			}
		}
		
		obj.saidMessages.removeAll(factsToRemove);
		obj.currentCycle++;		
	}

	public void visit(WorldObject obj) {

	}

	public void visit(AnimatedObject obj) {		
		Vector new_s = obj.s.sumVector(obj.v);
		Vector new_v = obj.v.sumVector(obj.a).limitModuloTo(MAX_SPEED);
		
		if(MathGeometry.calculePointIntoEllipse(World.WIDTH, World.FOCUS1X, World.FOCUS1Y,
				World.FOCUS2X, World.FOCUS2Y, new_s.x, new_s.y)){
			obj.s = new_s;
			obj.v = new_v;
		}else{
			obj.a = new Vector(0,0);
			obj.v = new Vector(0,0);
		}		
	}

	public void visit(Ball obj) {
		this.visit((AnimatedObject)obj);
		
		if (!obj.withPlayer && obj.v.getModule() > 0) {
			obj.a = new Vector(0,0).subtract(obj.v).setModulo(obj.v.getModule() / 10);			
		}
	}

	public void visit(OutTrack obj) {
	}

	public void visit(Player obj) {
		this.visit((AnimatedObject) obj);

		if (obj.hasBall) {
			obj.world.ball.s = obj.s;
		}

		if (obj.inGround) {
			obj.a = new Vector(0, 0);
			obj.v = new Vector(0, 0);
		}
	}

	public void processRamifications(EnvironmentStateModel world) {
		((World)world).accept(this);
	}

	public void visit(Basket obj) {
		
	}

	public void visit(Goal obj) {
		
	}

	public void visit(Ramp obj) {
		
	}

	public void visit(Trampoline obj) {
		
	}

}
