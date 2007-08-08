package rollerslam.environment.model;

import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class AnimatedObject extends WorldObject {
	
	public Vector v = new Vector(0, 0);
	public Vector a = new Vector(0, 0);
	
	public AnimatedObject() {
		
	}
	
	public AnimatedObject(World w, int psx, int psy, int pwidth, int pheight) {
		super(w, psx, psy, pwidth, pheight);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((AnimatedObject)this);
	}
	
}
