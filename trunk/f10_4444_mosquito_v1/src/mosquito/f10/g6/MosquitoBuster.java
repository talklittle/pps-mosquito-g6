package mosquito.f10.g6;

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
		HashSet<Light> ret = new HashSet<Light>();
		Random r = new Random();
		for(int i = 0; i<numLights;i++)
		{
			// select location until we are not within 20 meters of any wall. or stop after 10 tries.
			for (int numTries = 0; numTries < 10; numTries++) {
				lastLight = new Point2D.Double(r.nextInt(100), r.nextInt(100));
				if (!isNearWall(lastLight, 20)) {
					// if we found a spot that is not near a wall, then keep it
					logger.debug("point ("+lastLight.getX()+","+lastLight.getY()+") not near wall");
					break;
				}
				logger.debug("point ("+lastLight.getX()+","+lastLight.getY()+") NEAR WALL. numTries="+numTries);
			}
			Light l = new Light(lastLight.getX(),lastLight.getY(), 10,1,1);
			if(i==(numLights-1)){
				l = new Light(lastLight.getX(),lastLight.getY(), 1,1,1);
			}
			ret.add(l);
		}
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

	@Override
	public Collector getCollector() {
		// for 1 light, just place it near the light
		if (numLights == 1) {
			Random r = new Random();
			Collector c = new Collector(lastLight.getX()+0.1,lastLight.getY() +0.1);
			return c;
		}
		// for more than 1 light, place collector between the 2 closest lights
		else {
			// FIXME replace this
			Random r = new Random();
			Collector c = new Collector(lastLight.getX()+0.1,lastLight.getY() +0.1);
			return c;
		}
	}


}
