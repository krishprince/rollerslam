package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Basket extends WorldObject implements Serializable, Visitable {
	public Basket(World w, int psx, int psy) {
		super(w, psx, psy, SimulationSettings.BASKET_WIDTH, SimulationSettings.BASKET_HEIGHT);
	}

	public void accept(Visitor visitor) {
		visitor.visit((Basket)this);
	}	
}
