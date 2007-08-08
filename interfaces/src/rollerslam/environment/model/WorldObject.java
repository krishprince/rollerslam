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

	public boolean collidesWith(WorldObject obj) {
		int minx = s.x - width/2;
		int miny = s.y - height/2;
		int maxx = s.x + width/2;
		int maxy = s.y + height/2;
		
		int ominx = obj.s.x - obj.width/2;
		int ominy = obj.s.y - obj.height/2;
		int omaxx = obj.s.x + obj.width/2;
		int omaxy = obj.s.y + obj.height/2;
		
		boolean intetsect_x = (minx > ominx && minx < omaxx) ||
		  					  (maxx > ominx && maxx < omaxx)
		; 

		if (intetsect_x) {
			boolean intetsect_y = (miny > ominy && miny < omaxy) ||
			  					  (maxy > ominy && maxy < omaxy)
			  ;
			return intetsect_y;
		} else {
			return false;
		}
	}

	public void accept(Visitor visitor) {
		visitor.visit((WorldObject)this);
	}
}
