package rollerslam.environment.model;

import java.io.Serializable;
import java.lang.Math;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Goal extends WorldObject implements Serializable, Visitable {
	public Basket[] baskets = new Basket[3];
	public Vector ls = new Vector(0, 0);
	
	public Goal(World w, int psx, int psy) {
		super(w, psx, psy, SimulationSettings.GOAL_WIDTH, SimulationSettings.GOAL_HEIGHT);
		
		baskets[0] = new Basket(w, this.s.x, this.s.y);
		baskets[1] = new Basket(w, this.s.x, this.s.y + (-SimulationSettings.DISTANCE_BETWEEN_BASKETS));
		baskets[2] = new Basket(w, this.s.x, this.s.y + (SimulationSettings.DISTANCE_BETWEEN_BASKETS));
	}

	public boolean insideGoal(int px1, int px2, int py1, int py2){
		int bx1 = px1;
		int bx2 = px2;
		int by1 = py1;
		int by2 = py2;
		
		int gx1 = s.x;
		int gx2 = s.x;
		int gy1 = s.y - Math.abs(height / 2);
		int gy2 = s.y + Math.abs(height / 2);

		if(MathGeometry.calculeIntersectLines(bx1, bx2, by1, by2, gx1, gx2, gy1, gy2))
			return true;

		return false;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit((Goal)this);
	}	
}
