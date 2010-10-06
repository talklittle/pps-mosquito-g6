package mosquito.f10.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

public class Geometry {
	
	private static final Logger logger = Logger.getLogger(Geometry.class);

	
	/**
	 * http://mathworld.wolfram.com/Point-LineDistance2-Dimensional.html
	 * Eqn. 14
	 * 
	 * @param point
	 * @param line
	 * @return
	 */
	public static double getPointLineDistance(Point2D point, Line2D line) {
		double x0 = point.getX();
		double y0 = point.getY();
		double x1 = line.getX1();
		double y1 = line.getY1();
		double x2 = line.getX2();
		double y2 = line.getY2();
		
		double distance = Math.abs((x2 - x1)*(y1 - y0) - (x1 - x0)*(y2 - y1))
						/ Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		
		logger.debug("point ("+x0+","+y0+") line (("+x1+","+y1+"),("+x2+","+y2+")) distance="+distance);
		
		return distance;
	}
}
