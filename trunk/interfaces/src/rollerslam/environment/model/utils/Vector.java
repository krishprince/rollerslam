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
		
		double dx = x/1000.0;
		double dy = y/1000.0; // avoiding overflow
		
		return Math.sqrt((dx * dx) + (dy * dy)) * 1000.0;
	}
	
	public Vector sumVector(Vector vet){
		return new Vector(this.x + vet.x, this.y + vet.y);
	}
	
	public Vector subtract(Vector vet) {
		return new Vector(this.x - vet.x, this.y - vet.y);
	}
	
	public Vector multVector(double num){
		return new Vector((int)Math.floor(this.x * num), (int)Math.floor(this.y * num));
	}
	
	public Vector setModulo(double newModulo) {
		double current = getModule();
		double ratio = newModulo / current;
		
		return multVector(ratio);
	}
	
	public Vector limitModuloTo(double newModuloLimit) {
		double current = getModule();
		
		if (current > newModuloLimit) {
			return setModulo(newModuloLimit);
		} else {
			return this.clone();
		}
	}
	
	public Vector clone() {
		return new Vector(x,y);
	}
}
