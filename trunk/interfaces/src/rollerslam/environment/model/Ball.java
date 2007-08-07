package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Ball extends AnimatedObject implements Visitable {
        
        public boolean withPlayer = false;
        
	public Ball() {}
	
	public Ball(World w, int psx, int psy) {
		super(w, 0, 0, 500, 500);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Ball)this);
	}	
}
