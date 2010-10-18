package mosquito.g6;

import java.awt.geom.Point2D;

public class OutOfBounds {
	public static boolean isOutOfBounds(Point2D point) {
		return point.getX() < 0.0 || point.getX() > 100.0 || point.getY() < 0.0 || point.getY() > 0.0;
	}
}
