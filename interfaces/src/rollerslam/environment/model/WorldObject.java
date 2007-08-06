package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class WorldObject implements Serializable, Visitable {
	// center position
	public int sx = 0; 
	public int sy = 0;
	
	public int width = 0;
	public int height = 0;
	
	public World world = null;
	
	public WorldObject() {
		
	}
	
	public WorldObject(World w, int psx, int psy, int pwidth, int pheight) {
		sx = psx;
		sy = psy;
		
		width = pwidth;
		height = pheight;
		
		world = w;
	}

	public boolean collidesWith(WorldObject obj) {
		int minx = sx - width/2;
		int miny = sy - height/2;
		int maxx = sx + width/2;
		int maxy = sy + height/2;
		
		int ominx = obj.sx - obj.width/2;
		int ominy = obj.sy - obj.height/2;
		int omaxx = obj.sx + obj.width/2;
		int omaxy = obj.sy + obj.height/2;
		
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
