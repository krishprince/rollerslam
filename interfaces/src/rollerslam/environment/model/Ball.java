package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Ball extends AnimatedObject implements Visitable {

	public Ball() {}
	
	public Ball(int psx, int psy) {
		super(0, 0, 500, 500);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Ball)this);
	}	
}
