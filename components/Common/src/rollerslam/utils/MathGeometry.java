package rollerslam.utils;

import java.awt.geom.Line2D;

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
	

	public static boolean calculeIntersectLines(double line1x1, double line1x2, double line1y1, 
			double line1y2, double line2x1, double line2x2, double line2y1, double line2y2) {
		
		Line2D.Double l1 = new Line2D.Double(line1x1, line1y1, line1x2, line1y2);
		Line2D.Double l2 = new Line2D.Double(line2x1, line2y1, line2x2, line2y2);
		
		return l1.intersectsLine(l2);
	}

}
