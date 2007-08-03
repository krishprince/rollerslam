package rollerslam.environment.model.utils;

@SuppressWarnings("serial")
public class Vector implements java.io.Serializable {
	public int x;
	public int y;
	
	public Vector(int px, int py){
		x = px;
		y = py;
	}
	 
	public int getModule(){
		return (int)Math.abs(Math.sqrt((x * x) + (y * y)));
	}
}
