package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Ramp extends WorldObject implements Serializable, Visitable {
	public Ramp(World w, int psx, int psy) {
		super(w, psx, psy, SimulationSettings.RAMP_WIDTH, SimulationSettings.RAMP_HEIGHT);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Ramp)this);
	}
}
