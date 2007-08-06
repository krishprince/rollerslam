package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class OutTrack extends WorldObject {
	public static final int WIDTH  = 188000;
	public static final int HEIGHT = 138000;

	public OutTrack(World w) {
		super(w, 0, 0, WIDTH, HEIGHT);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((OutTrack)this);
	}	
}
