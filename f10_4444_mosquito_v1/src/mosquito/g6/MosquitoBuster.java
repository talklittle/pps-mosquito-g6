package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Board;
import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

import org.apache.log4j.Logger;

public class MosquitoBuster extends Player {
	private int numLights;
	private Set<Line2D> walls;
	private Set<Light> lights;
	private Board board;

	
	private static final Random random = new Random();
	
	private static final Logger logger = Logger.getLogger(MosquitoBuster.class);
	
	@Override
	public String getName() {
		return "Mosquito Buster";
	}
	
	@Override
	public void startNewGame(Set<Line2D> walls, int NumLights) {
		this.walls = walls;
		this.numLights = NumLights;
	}

	/* Brute force to go through each spot on the board and test from there */
	public double[] getStartSpot()
	{
		double[][] spots = new double[101][101];
		double[] fastestCoordinate = {0,0};
		
		for (double i = 0; i < 101; i+=0.5) {
			for (double j = 0; j < 101; j+=0.5) {
					spots[i][j] = testAtSpot(i, j); // test at spot does simulation in that coordinate
					if(j-0.5 >= 0)
					{
						if((spots[i][j] <= spots[i][j-0.5]) && (spots[i][j] != -1.0));
						fastestCoordinate[0] = i;
						fastestCoordinate[1] = j;
					}
			}
		}
		
		return fastestCoordinate;
		
	}
	
	@Override
	public Set<Light> getLights() {
		
		HashSet<Light> ret = new HashSet<Light>();
		
		ret = getLightPositions(); // after start spot has been chosen, we decide light positions and this returns them
		
		return ret;
	}
	
	/**
	 * 
	 * @param point The point
	 * @param distance Distance in meters considered "close". 
	 * @return
	 */
	private boolean isNearWall(Point2D point, double distance) {
		for (Line2D wall : walls) {
			if (Geometry.getPointLineDistance(point, wall) <= distance) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isIntersectingLight(Point2D point) {
		for (Light light : lights) {
			if (point.distance(light.getLocation()) < 0.5)
				return true;
		}
		return false;
	}

	@Override
	public Collector getCollector() {
		Point2D collectorLocation = new Point2D.Double(firstLight.getX()+0.1, firstLight.getY()+0.1);
		// move collector slightly if it's on a wall
		while (isNearWall(collectorLocation, 0.5) && collectorLocation.getX() < 99 && collectorLocation.getY() < 99)
			collectorLocation = new Point2D.Double(collectorLocation.getX()+0.1, collectorLocation.getY()+0.1);
		// XXX if all else fails, use random location
		while (isNearWall(collectorLocation, 0.5))
			collectorLocation = new Point2D.Double(random.nextDouble() * 99.0, random.nextDouble() * 99.0);
		
		// move collector if it's on a light
		while (isIntersectingLight(collectorLocation) && collectorLocation.getX() < 99 && collectorLocation.getY() < 99)
			collectorLocation = new Point2D.Double(collectorLocation.getX()+0.1, collectorLocation.getY()+0.1);
		// XXX if all else fails, use random location
		while (isIntersectingLight(collectorLocation))
			collectorLocation = new Point2D.Double(random.nextDouble() * 99.0, random.nextDouble() * 99.0);
		
		// for 1 light, just place it near the light
		if (numLights == 1) {
			Random r = new Random();
			//Collector c = new Collector(lastLight.getX()+0.1,lastLight.getY() +0.1);
			Collector c = new Collector(collectorLocation.getX(), collectorLocation.getY());
			return c;
		}
		// for more than 1 light, place collector between the 2 closest lights
		else {
			// FIXME replace this
			Random r = new Random();
			//Collector c = new Collector(lastLight.getX()+0.1,lastLight.getY() +0.1);
			Collector c = new Collector(collectorLocation.getX(), collectorLocation.getY());
			return c;
		}
	}


}
