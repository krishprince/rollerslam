package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Basket extends WorldObject implements Serializable, Visitable {
	public static final int WIDTH  = 500;
	public static final int HEIGHT = 500;

	public Basket(World w, int psx, int psy) {
		super(w, psx, psy, WIDTH, HEIGHT);
	}

	public void accept(Visitor visitor) {
		visitor.visit((Basket)this);
	}	
}
