package rollerslam.environment.model.visitor;

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

public interface Visitor {
	void visit(World obj);
	void visit(WorldObject obj);
	void visit(AnimatedObject obj);
	void visit(Ball obj);
	void visit(OutTrack obj);
	void visit(Player obj);
	void visit(Basket obj);
	void visit(Goal obj);
	void visit(Ramp obj);
	void visit(Trampoline obj);
}
