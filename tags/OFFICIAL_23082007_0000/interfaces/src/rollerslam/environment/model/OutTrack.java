package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class OutTrack extends WorldObject {
	public OutTrack(World w) {
		super(w, 0, 0, SimulationSettings.OUTTRACK_WIDTH, SimulationSettings.OUTTRACK_HEIGHT);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((OutTrack)this);
	}	
}
