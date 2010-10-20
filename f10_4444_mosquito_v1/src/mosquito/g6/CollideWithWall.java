package mosquito.g6;
import java.awt.geom.*;
import java.util.HashSet;
import java.util.Set;

import mosquito.sim.Light;

public class CollideWithWall {

	public static boolean isCollideWithWall(Point2D first, Point2D next, Set<Line2D> walls){
		Line2D line = new Line2D.Double(first.getX(), first.getY(), next.getX(), next.getY());
		for (Line2D l : walls) {
			if(l.intersectsLine(line)){
				return true;
			}
		}
		return false;
	}
	//Jon's function in Board
	public static boolean isCollideWithWall(Point2D point, Set<Line2D> walls){
		double MOSQUITO_EPSILON = 0.5;
		Rectangle2D pointRectangle = new Rectangle2D.Double(
				point.getX() - MOSQUITO_EPSILON, point.getY() - MOSQUITO_EPSILON,
				MOSQUITO_EPSILON * 2, MOSQUITO_EPSILON * 2);
		for (Line2D l : walls) {
			if(l.intersects(pointRectangle)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isCollideWithOtherLights(HelperLight current, Set<HelperLight> lights, Set<Line2D> walls){
		
		HashSet<HelperLight> otherLights = new HashSet<HelperLight>();
		
		for (HelperLight l : lights) {
			// skip the base, and skip siblings (who share the same base)
			if (current.getBase() == l || current.getBase() == l.getBase())
				continue;
			otherLights.add(l);
//			double distance = Math.sqrt(Math.pow(current.getX()-l.getX(), 2)+(Math.pow(current.getY()-l.getY(), 2)));
//			double distance = new Point2D.Double(current.getX(), current.getY()).distance(l.getX(), l.getY());
			
//			if(distance<=40){
		}
		if (!(SightChecker.getInSight(current, otherLights, walls).isEmpty())) {
			return true;
		}
		return false;
		
	}
	
}
