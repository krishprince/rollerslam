package rollerslam.environment.model;

public class WorldObject {
	// center position
	public int sx; 
	public int sy;
	
	public int width;
	public int height;
	
	public WorldObject(int psx, int psy, int pwidth, int pheight) {
		sx = psx;
		sy = psy;
		
		width = pwidth;
		height = pheight;
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
}
