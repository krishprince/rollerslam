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
import rollerslam.environment.model.utils.MathGeometry;

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

		if(MathGeometry.calculePointIntoEllipse(World.WIDTH, World.FOCUS1X, World.FOCUS1Y,
				World.FOCUS2X, World.FOCUS2Y, sx, sy)){
			obj.sx = sx;
			obj.sy = sy;

			obj.vx = vx;
			obj.vy = vy;
		}else{
			obj.ax = 0;
			obj.ay = 0;

			obj.vx = 0;
			obj.vy = 0;
		}
		
	}

	public void visit(Ball obj) {
		this.visit((AnimatedObject)obj);
	}

	public void visit(OutTrack obj) {
	}

	public void visit(Player obj) {
		this.visit((AnimatedObject)obj);
                
		if(obj.hasBall){
			obj.world.ball.sx = obj.sx;
			obj.world.ball.sy = obj.sy;
		}
                
                if(obj.inGround){
                        obj.ax = 0;
                        obj.ay = 0;
                        
                        obj.vx = 0;
                        obj.vy = 0;
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
