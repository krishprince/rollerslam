package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class AnimatedObject extends WorldObject {
	
	public int vx = 0;
	public int vy = 0;
	
	public int ax = 0;
	public int ay = 0;
	
	public AnimatedObject() {
		
	}
	
	public AnimatedObject(int psx, int psy, int pwidth, int pheight) {
		super(psx, psy, pwidth, pheight);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((AnimatedObject)this);
	}
	
}
