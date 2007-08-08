package rollerslam.environment.model.utils;

 public class MathGeometry {
	public static boolean calculePointIntoEllipse(int majorAxis, int f1x, int f1y, int f2x, int f2y, int px, int py){
		int dist1 = calculeDistancePoints(f1x, px, f1y, py);
		int dist2 = calculeDistancePoints(f2x, px, f2y, py);

		if(dist1 + dist2 > majorAxis)
			return false;
		else
			return true;
		
	}
	
	public static int calculeDistancePoints(int px1, int px2, int py1, int py2){
		long dx = px1 - px2;
		long dy = py1 - py2;
		
		dx *= dx;
		dy *= dy;
		
		return (int)java.lang.Math.floor(java.lang.Math.sqrt(dx + dy));
	}
}
