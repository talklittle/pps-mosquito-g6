package mosquito.g6;
import java.awt.geom.*;
import java.util.Set;

public class CollideWithWall {

	public static boolean isCollideWithWall(Point2D first, Point2D next, Set<Line2D> walls){
		Line2D line = new Line2D.Double(first, next);
		for (Line2D l : walls) {
			if(l.intersectsLine(line)){
				return false;
			}
		}
		return true;
	}
	//Jon's function in Board
	public static boolean isCollideWithWall(Point2D point, Set<Line2D> walls){
		double MOSQUITO_EPSILON = 0.5;
		Rectangle2D pointRectangle = new Rectangle2D.Double(
				point.getX() - MOSQUITO_EPSILON, point.getY() - MOSQUITO_EPSILON,
				MOSQUITO_EPSILON * 2, MOSQUITO_EPSILON * 2);
		for (Line2D l : walls) {
			if(l.intersects(pointRectangle)){
				return false;
			}
		}
		return true;
	}
	
}
