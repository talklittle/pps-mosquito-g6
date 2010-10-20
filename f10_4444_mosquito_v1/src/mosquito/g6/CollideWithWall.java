package mosquito.g6;
import java.awt.geom.*;
import java.util.Set;

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
	
	public static boolean isCollideWithOtherLights(HelperLight current, Set<HelperLight> lights){
		
		for (HelperLight l : lights) {
			if(!current.getBase().equals(l.getBase())){
				double distance = Math.sqrt(Math.pow(current.getX()-l.getX(), 2)+(Math.pow(current.getY()-l.getY(), 2)));
				if(distance<=40){
					if(!current.getBase().equals(l)){
						return true;
					}
				}
			}
		}
		return false;
		
	}
	
}
