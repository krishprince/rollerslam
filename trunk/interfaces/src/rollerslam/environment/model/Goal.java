package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Goal extends WorldObject implements Serializable, Visitable {
	public static final int WIDTH  = 7000;
	public static final int HEIGHT = 200;

	public Basket[] baskets = new Basket[3];
	
	public Goal(World w, int psx, int psy) {
		super(w, psx, psy, WIDTH, HEIGHT);
		
		baskets[0] = new Basket(w, this.s.x, this.s.y);
		baskets[1] = new Basket(w, this.s.x, this.s.y + (-3500));
		baskets[2] = new Basket(w, this.s.x, this.s.y + (3500));
	}

	public void accept(Visitor visitor) {
		visitor.visit((Goal)this);
	}	
}
