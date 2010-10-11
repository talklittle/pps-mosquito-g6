package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

import org.apache.log4j.Logger;

public class MosquitoBuster extends Player {
	private int numLights;
	private Set<Line2D> walls;
	private HashSet<Light> lights;
	
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

	Point2D lastLight = null;
	@Override
	public Set<Light> getLights() {
		lights = new HashSet<Light>();
		Random r = new Random();
		Light l = new Light(50,50, 1,1,1);
		while(isNearWall(l.getLocation(), 20) && l.getX() < 98 && l.getY() < 98){
			l = new Light(l.getX()+1,l.getY()+1, 1,1,1);
		}
		// make sure light is not on top of a wall
		while (isNearWall(l.getLocation(), 1)) {
			l = new Light(random.nextDouble() * 99.0, random.nextDouble() * 99.0, 1,1,1);
		}
		
		lights.add(l);
		for(int i = 1; i<numLights;i++)
		{
			// select location until we are not within 20 meters of any wall. or stop after 10 tries.
			for (int numTries = 0; numTries < 10; numTries++) {
				lastLight = new Point2D.Double(l.getX()-(14*Math.cos((i-1)*2*Math.PI/(numLights-1))),(l.getY()-(14*Math.sin((i-1)*2*Math.PI/(numLights-1)))));
				if (!isNearWall(lastLight, 20)) {
					// if we found a spot that is not near a wall, then keep it
					logger.debug("point ("+lastLight.getX()+","+lastLight.getY()+") not near wall");
					break;
				}
				logger.debug("point ("+lastLight.getX()+","+lastLight.getY()+") NEAR WALL. numTries="+numTries);
			}
			l = new Light(lastLight.getX(),lastLight.getY(), 10,1,1);
			lights.add(l);
		}
		return lights;
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
		Point2D collectorLocation = new Point2D.Double(lastLight.getX()+0.1, lastLight.getY()+0.1);
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
