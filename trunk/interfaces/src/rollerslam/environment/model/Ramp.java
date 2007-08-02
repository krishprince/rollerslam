package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Ramp extends WorldObject implements Serializable, Visitable {
	public static final int WIDTH  = 30000;
	public static final int HEIGHT = 30000;

	public Ramp(int psx, int psy) {
		super(psx, psy, WIDTH, HEIGHT);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Ramp)this);
	}
}
