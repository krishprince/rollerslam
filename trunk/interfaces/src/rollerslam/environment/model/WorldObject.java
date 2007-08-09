package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class WorldObject implements Serializable, Visitable {
	// center position
	public Vector s = new Vector(0,0); 
	
	public int width = 0;
	public int height = 0;
	
	public World world = null;
	
	public WorldObject() {
		
	}
	
	public WorldObject(World w, int psx, int psy, int pwidth, int pheight) {
		s.x = psx;
		s.y = psy;
		
		width = pwidth;
		height = pheight;
		
		world = w;
	}

	public void accept(Visitor visitor) {
		visitor.visit((WorldObject)this);
	}
}
