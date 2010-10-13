package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	
	public static HashSet<HashSet<Line2D> > getRegions(Set<Line2D> walls) {
		Set<Line2D> extendedWalls = getExtendedWalls(walls);
		
		// Map of intersection points to set of lines intersecting at that point
		Map<Point2D, Set<Line2D> > intersectionMap = getIntersectionMap(extendedWalls);
		// Trim the extended walls to share intersection points
		Set<Line2D> trimmedWalls = trimToIntersections(extendedWalls);
		
		HashSet<HashSet<Line2D> > regions = new HashSet<HashSet<Line2D> >();
		
		Set<Point2D> checkedIntersections = new HashSet<Point2D>();
		for (Point2D intersectionPoint : intersectionMap.keySet()) {
			// check points that have not been checked yet
			if (!checkedIntersections.contains(intersectionPoint)) {
				checkedIntersections.add(intersectionPoint);
				
				for (Line2D wall : intersectionMap.get(intersectionPoint)) {
					// get the other endpoint of the line
					Point2D otherPoint = intersectionPoint.equals(wall.getP1()) ? wall.getP2() : wall.getP1();
					checkedIntersections.add(intersectionPoint);
				}
			}
		}
		return regions;
	}
	
//	private void getRegionsCheckingHelper(Map<Point2D, Set<Line2D> > intersectionMap,
//			???, Set<Point2D> checkedIntersections) {
//		
//	}
	
	public static Set<Line2D> getExtendedWalls(Set<Line2D> walls) {
		// TODO
		return null;
	}
	
	public static Map<Point2D, Set<Line2D> > getIntersectionMap(Set<Line2D> walls) {
		// TODO
		return null;
	}
	
	public static Set<Line2D> trimToIntersections(Set<Line2D> walls) {
		// TODO
		return null;
	}
}
