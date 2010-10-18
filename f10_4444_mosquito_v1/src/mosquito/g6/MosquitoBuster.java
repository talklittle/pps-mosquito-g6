package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Board;
import mosquito.sim.Collector;
import mosquito.sim.GameListener;
import mosquito.sim.Light;
import mosquito.sim.Player;

import org.apache.log4j.Logger;

public class MosquitoBuster extends Player {
	private int numLights;
	private Set<Line2D> walls;
	private Board board;

	// static members that are used in simulations
	private static Set<Light> lights;
	private static Point2D initialLightLocation;
	
	private static final int NUM_REPETITIONS = 1;

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
		this.lights = getLightPositions();
	}

	@Override
	public void startSimulatedGame(Set<Line2D> walls, int NumLights) {
		this.walls = walls;
		this.numLights = NumLights;
	}

	public Set<Light> getLightPositions() {
		double[] startSpot = getStartSpot();
		Set<Light> lightPositions = testLightPositions(startSpot[0], startSpot[1]);
		return lightPositions;
	}

	/* Brute force to go through each spot on the board and test from there */
	public double[] getStartSpot()
	{
		double[][] spots = new double[2*101][2*101];
		double[] fastestCoordinate = {0,0};

		for (double i = 0; i < 100; i+=0.5) {
			for (double j = 0; j < 100; j+=0.5) {
				spots[(int) (2*i)][(int) (2*j)] = testAtSpot(i, j); // test at spot does simulation in that coordinate
				if(j-0.5 >= 0)
				{
					if((spots[(int) (2*i)][(int) (2*j)] <= spots[(int) (2*i)][(int) (2*(j-0.5))])
							&& (spots[(int) (2*i)][(int) (2*j)] != -1.0));
					fastestCoordinate[0] = i;
					fastestCoordinate[1] = j;
				}
			}
		}

		return fastestCoordinate;

	}

	/**
	 * Run the tests repeatedly and get the average number of rounds
	 * @param x
	 * @param y
	 * @return
	 */
	public int testAtSpot(double x, double y) {

		final ArrayList<Integer> simRounds = new ArrayList<Integer>();

		initialLightLocation = new Point2D.Double(x, y);

		for (int i = 0; i < NUM_REPETITIONS; i++) {
			double radians = i * (2.0 * Math.PI / (double) NUM_REPETITIONS);
			
			logger.trace("testAtSpot: Before PutLights");
			lights = PutLights.putLights(walls, initialLightLocation, numLights, radians);
			logger.trace("testAtSpot: After PutLights");

			// run the test
			runSimulation(3000, new GameListener() {
				@Override
				public void gameUpdated(GameUpdateType type) {
					if(type.equals(GameUpdateType.MOVEPROCESSED))
					{
						logger.trace("We had a move happen, " + getSimulationRounds() +", caught: " + getSimulationNumCaught());
					}
					else if(type.equals(GameUpdateType.GAMEOVER))
					{
						logger.debug("Game ended at ticks: " + getSimulationRounds());
						simRounds.add(getSimulationRounds());
					}
				}
			});
			logger.debug("Game ended. x="+x+" y="+y+" trial(of "+NUM_REPETITIONS+")="+i+" radians="+radians
					+" simRounds.add("+simRounds.get(simRounds.size()-1)+")");
		}

		// Get the sum, avg
		int sum = 0;
		for (Integer rounds : simRounds) {
			sum += rounds;
		}
		int average = (simRounds.size() > 0) ? (sum / simRounds.size()) : 3000;

		return average;
	}

	/**
	 * Same as testAtSpot but returns light config.
	 * This method requires more memory so try to call it less often.
	 * @param x
	 * @param y
	 * @return
	 */
	public Set<Light> testLightPositions(double x, double y) {
		// map of testId => numRounds
		final HashMap<Integer, Integer> mapRounds = new HashMap<Integer, Integer>();
		// map of testId => Set<Light>
		final HashMap<Integer, Set<Light> > mapLights = new HashMap<Integer, Set<Light> >();

		initialLightLocation = new Point2D.Double(x, y);

		for (int i = 0; i < NUM_REPETITIONS; i++) {
			final double radians = i * (2.0 * Math.PI / (double) NUM_REPETITIONS);
			final int testId = i;
			lights = PutLights.putLights(walls, initialLightLocation, numLights, radians);
			mapLights.put(testId, lights);

			// run the test
			runSimulation(3000, new GameListener() {
				@Override
				public void gameUpdated(GameUpdateType type) {
					if(type.equals(GameUpdateType.MOVEPROCESSED))
					{
						logger.info("We had a move happen, " + getSimulationRounds() +", caught: " + getSimulationNumCaught());
					}
					else if(type.equals(GameUpdateType.GAMEOVER))
					{
						logger.debug("Game ended at ticks: " + getSimulationRounds());
						mapRounds.put(testId, getSimulationRounds());
					}
				}
			});
		}

		// get the Set<Light> associated with the min
		int minKey = 0;
		int minRounds = 3000;
		for (Integer key : mapRounds.keySet()) {
			if (mapRounds.get(key) < minRounds) {
				minRounds = mapRounds.get(key);
				minKey = key;
			}
		}
		return mapLights.get(minKey);
	}

	@Override
	public Set<Light> getLights() {
		return lights;
	}





	private boolean isIntersectingLight(Point2D point) {
		Rectangle2D pointRect = new Rectangle2D.Double(point.getX()-0.5, point.getY()-0.5, 1.0, 1.0);
		for (Light light : lights) {
			Rectangle2D lightRect = new Rectangle2D.Double(light.getX()-0.5, light.getY()-0.5, 1.0, 1.0);
//			if (point.distance(light.getLocation()) < 0.5)
			if (pointRect.intersects(lightRect))
				return true;
		}
		return false;
	}

	@Override
	public Collector getCollector() {
		
		logger.debug("enter getCollector");
		
		// try the 4 diagonals
		Point2D collectorLocation = new Point2D.Double(initialLightLocation.getX()-0.5, initialLightLocation.getY()-0.5);
		if (CollideWithWall.isCollideWithWall(collectorLocation, walls) || OutOfBounds.isOutOfBounds(collectorLocation))
			collectorLocation = new Point2D.Double(initialLightLocation.getX()+0.5, initialLightLocation.getY()-0.5);
		if (CollideWithWall.isCollideWithWall(collectorLocation, walls) || OutOfBounds.isOutOfBounds(collectorLocation))
			collectorLocation = new Point2D.Double(initialLightLocation.getX()-0.5, initialLightLocation.getY()+0.5);
		if (CollideWithWall.isCollideWithWall(collectorLocation, walls) || OutOfBounds.isOutOfBounds(collectorLocation))
			collectorLocation = new Point2D.Double(initialLightLocation.getX()+0.5, initialLightLocation.getY()+0.5);
		
		logger.debug("tested 4 diagonals");
		
		// move collector slightly if it's on a wall
		while (CollideWithWall.isCollideWithWall(collectorLocation, walls) && collectorLocation.getX() < 99.9 && collectorLocation.getY() < 99.9)
			collectorLocation = new Point2D.Double(collectorLocation.getX()+0.5, collectorLocation.getY()+0.5);
		
		logger.debug("after wall sanity check 1");
		
		// XXX if all else fails, use random location
		while (CollideWithWall.isCollideWithWall(collectorLocation, walls) || OutOfBounds.isOutOfBounds(collectorLocation))
			collectorLocation = new Point2D.Double(random.nextDouble() * 99.0, random.nextDouble() * 99.0);
		
		logger.debug("after wall sanity check 2");

		// move collector if it's on a light
		while (isIntersectingLight(collectorLocation) && collectorLocation.getX() < 99.9 && collectorLocation.getY() < 99.9)
			collectorLocation = new Point2D.Double(collectorLocation.getX()+0.5, collectorLocation.getY()+0.5);
		
		logger.debug("after light sanity check 1");
		
		// XXX if all else fails, use random location
		while (isIntersectingLight(collectorLocation) || OutOfBounds.isOutOfBounds(collectorLocation))
			collectorLocation = new Point2D.Double(random.nextDouble() * 99.5, random.nextDouble() * 99.5);

		logger.debug("found collectorLocation: ("+collectorLocation.getX()+", "+collectorLocation.getY()+")");
		
		// for 1 light, just place it near the light
		if (numLights == 1) {
//			Random r = new Random();
			//Collector c = new Collector(lastLight.getX()+0.1,lastLight.getY() +0.1);
			Collector c = new Collector(collectorLocation.getX(), collectorLocation.getY());
			return c;
		}
		// for more than 1 light, place collector between the 2 closest lights
		else {
			// FIXME replace this
//			Random r = new Random();
			//Collector c = new Collector(lastLight.getX()+0.1,lastLight.getY() +0.1);
			Collector c = new Collector(collectorLocation.getX(), collectorLocation.getY());
			return c;
		}
	}


}
