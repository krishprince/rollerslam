package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.environment.model.SimulationSettings;

@SuppressWarnings("serial")
public class Ball extends AnimatedObject implements Visitable {
        
    public boolean withPlayer = false;
        
	public Ball() {}
	
	public Ball(World w, int psx, int psy) {
		super(w, 0, 0, SimulationSettings.BALL_WIDTH, SimulationSettings.BALL_HEIGHT);
		maxA = SimulationSettings.MAX_ACCELERATION * 5;
		maxV = SimulationSettings.MAX_VELOCITY * 5;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Ball)this);
	}	
}