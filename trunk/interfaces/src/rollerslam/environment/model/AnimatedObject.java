package rollerslam.environment.model;

import java.io.Serializable;

public class AnimatedObject extends WorldObject implements Updateable, Serializable {
	
	public AnimatedObject(int psx, int psy, int pwidth, int pheight) {
		super(psx, psy, pwidth, pheight);
	}

	public int vx;
	public int vy;
	
	public int ax;
	public int ay;
		
	public void update() {
		sx = sx + vx;
		sy = sy + vy;

		vx = vx + ax;
		vy = vy + ay;		
	}
	
}
