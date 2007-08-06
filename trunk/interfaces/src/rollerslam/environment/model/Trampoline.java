package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Trampoline extends WorldObject implements Serializable, Visitable {
	public static final int WIDTH  = 23000;
	public static final int HEIGHT = 23000;

	public Trampoline(World w, int psx, int psy) {
		super(w, psx, psy, WIDTH, HEIGHT);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Trampoline)this);
	}	
}
