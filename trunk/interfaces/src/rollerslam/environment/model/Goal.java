package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Goal extends WorldObject implements Serializable, Visitable {
	public Basket[] baskets = new Basket[3];
	
	public Goal(World w, int psx, int psy) {
		super(w, psx, psy, SimulationSettings.GOAL_WIDTH, SimulationSettings.GOAL_HEIGHT);
		
		baskets[0] = new Basket(w, this.s.x, this.s.y);
		baskets[1] = new Basket(w, this.s.x, this.s.y + (-SimulationSettings.DISTANCE_BETWEEN_BASKETS));
		baskets[2] = new Basket(w, this.s.x, this.s.y + (SimulationSettings.DISTANCE_BETWEEN_BASKETS));
	}

	public void accept(Visitor visitor) {
		visitor.visit((Goal)this);
	}	
}
