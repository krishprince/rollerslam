package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Trampoline extends WorldObject implements Serializable, Visitable {
	public Trampoline(World w, int psx, int psy) {
		super(w, psx, psy, SimulationSettings.TRAMPOLINE_WIDTH, SimulationSettings.TRAMPOLINE_HEIGHT);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Trampoline)this);
	}	
}
