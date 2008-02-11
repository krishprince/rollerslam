package rollerslam.common.objects.state;

import rollerslam.utils.Vector;

@SuppressWarnings("serial")
public class AnimatedObject extends WorldObject {
	
	public Vector v = new Vector(0, 0);
	public Vector a = new Vector(0, 0);
	
	public int maxV = 0;
	public int maxA = 0;
	
	public AnimatedObject() {
		
	}
	
	public AnimatedObject(int psx, int psy, int pwidth, int pheight) {
		super(psx, psy, pwidth, pheight);
	}

	public String toString() {
		return super.toString()+",v=" + v + ",a="+a+ ",maxV="+maxV+",maxA="+maxA;
	}
	
}
