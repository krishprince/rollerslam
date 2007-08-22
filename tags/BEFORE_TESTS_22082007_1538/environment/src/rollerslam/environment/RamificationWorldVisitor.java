package rollerslam.environment;


import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.Basket;
import rollerslam.environment.model.Goal;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.Ramp;
import rollerslam.environment.model.Scoreboard;
import rollerslam.environment.model.SimulationSettings;
import rollerslam.environment.model.Trampoline;
import rollerslam.environment.model.World;
import rollerslam.environment.model.WorldObject;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.RamificationComponent;

/**
 * @author Marcos Aurelio
 *
 */
public class RamificationWorldVisitor implements Visitor, RamificationComponent {

	public void visit(World obj) {
		obj.actions.clear();
		obj.actions.addAll(obj.newActions);
		obj.newActions.clear(); // TODO ajeitar essa gambiarra!
		
		obj.currentCycle++;		
	}

	public void visit(WorldObject obj) {

	}

	public void visit(AnimatedObject obj) {		
		Vector new_s = obj.s.sumVector(obj.v);
		Vector new_v;
		
		new_v = obj.v.sumVector(obj.a).limitModuloTo(obj.maxV);
		
		if (MathGeometry.calculePointIntoEllipse(obj.world.outTrack.width,
				SimulationSettings.FOCUS1X, SimulationSettings.FOCUS1Y,
				SimulationSettings.FOCUS2X, SimulationSettings.FOCUS2Y,
				new_s.x, new_s.y)) {
			obj.s = new_s;
			obj.v = new_v;
		} else {
			obj.a = new Vector(0, 0);
			obj.v = new Vector(0, 0);
		}		
	}

	public void visit(Ball obj) {
		this.visit((AnimatedObject)obj);

		if (!obj.withPlayer && obj.v.getModule() > 0) {
			obj.a = new Vector(0,0).subtract(obj.v).setModulo(obj.v.getModule() / 10);			
		}

		if(obj.world.goalA.insideGoal(obj.ls.x, obj.s.x, obj.ls.y, obj.s.y))
			obj.world.scoreboard.scoreTeamB += 1;

		if(obj.world.goalB.insideGoal(obj.ls.x, obj.s.x, obj.ls.y, obj.s.y))
			obj.world.scoreboard.scoreTeamA += 1;
		
		obj.ls.x = obj.s.x;
		obj.ls.y = obj.s.y;

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
		
		obj.counterTackle = false;
		
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

	public void visit(Scoreboard obj) {
		
	}

}
