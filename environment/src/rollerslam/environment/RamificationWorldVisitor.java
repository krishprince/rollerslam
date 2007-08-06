package rollerslam.environment;


import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.Basket;
import rollerslam.environment.model.Goal;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.Ramp;
import rollerslam.environment.model.Trampoline;
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
		int sx = obj.sx + obj.vx;
		int sy = obj.sy + obj.vy;
		
		int vx = Math.min(obj.vx + obj.ax, MAX_SPEED);
		int vy = Math.min(obj.vy + obj.ay, MAX_SPEED);
System.out.println("Dentro? => " + obj.world.calculePointIntoEllipse(sx, sy));		
		if(obj.world.calculePointIntoEllipse(sx, sy)){
			obj.sx = obj.sx + obj.vx;
			obj.sy = obj.sy + obj.vy;
	
			obj.vx = Math.min(obj.vx + obj.ax, MAX_SPEED);
			obj.vy = Math.min(obj.vy + obj.ay, MAX_SPEED);
System.out.println("Valores? " + obj.sx + ", " + obj.sy + ", " + obj.vx + ", " + obj.vy);
		}else{
			obj.vx = 0;
			obj.vy = 0;
System.out.println("Valores? " + obj.sx + ", " + obj.sy + ", " + obj.vx + ", " + obj.vy);
		}
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

	public void visit(Basket obj) {
		
	}

	public void visit(Goal obj) {
		
	}

	public void visit(Ramp obj) {
		
	}

	public void visit(Trampoline obj) {
		
	}

}
