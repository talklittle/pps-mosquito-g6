package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
	
	private static final int NUM_REPETITIONS = 10;

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

		for (double i = 0; i < 101; i+=0.5) {
			for (double j = 0; j < 101; j+=0.5) {
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
		int average = sum / simRounds.size();

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
		Point2D collectorLocation = new Point2D.Double(initialLightLocation.getX()+0.1, initialLightLocation.getY()+0.1);
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
