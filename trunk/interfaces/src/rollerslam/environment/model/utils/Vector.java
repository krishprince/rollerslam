package rollerslam.environment.model.utils;

@SuppressWarnings("serial")
public class Vector implements java.io.Serializable {
	public int x;
	public int y;
	
	public Vector(int px, int py){
		x = px;
		y = py;
	}
	 
	public double getModule(){
		return Math.sqrt((x * x) + (y * y));
	}
	
	public Vector sumVector(Vector vet){
		return new Vector(this.x + vet.x, this.y + vet.y);
	}
	
	public Vector multVector(double num){
		return new Vector((int)Math.abs(this.x * num), (int)Math.abs(this.y * num));
	}
}
