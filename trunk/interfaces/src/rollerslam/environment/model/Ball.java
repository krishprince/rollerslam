package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Ball extends AnimatedObject implements Visitable {
        
    public boolean withPlayer = false;
        
	public Ball() {}
	
	public Ball(World w, int psx, int psy) {
		super(w, 0, 0, SimulationSettings.BALL_WIDTH, SimulationSettings.BALL_HEIGHT);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Ball)this);
	}	
}
