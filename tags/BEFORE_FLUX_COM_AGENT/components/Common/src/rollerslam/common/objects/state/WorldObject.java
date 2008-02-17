package rollerslam.common.objects.state;

import rollerslam.agent.communicative.specification.type.fluent.FluentState;
import rollerslam.utils.Vector;

@SuppressWarnings("serial")
public class WorldObject extends FluentState {
	// center position
	public Vector s = new Vector(0,0); 
	
	public int width = 0;
	public int height = 0;
		
	public WorldObject() {
		
	}
	
	public WorldObject(int psx, int psy, int pwidth, int pheight) {
		s.x = psx;
		s.y = psy;
		
		width = pwidth;
		height = pheight;
	}
	
	public String toString() {
		return "s=" + s + ",width="+width+",height="+height;
	}

}
