package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Goal extends WorldObject implements Serializable, Visitable {
	public static final int WIDTH  = 7000;
	public static final int HEIGHT = 200;

	public Basket[] baskets = new Basket[3];
	
	public Goal(int psx, int psy) {
		super(psx, psy, WIDTH, HEIGHT);
		for(int i = 0; i < 3; ++i)
			baskets[i] = new Basket(this.sx, this.sy + (i * 3500));
	}

	public void accept(Visitor visitor) {
		visitor.visit((Goal)this);
	}	
}
