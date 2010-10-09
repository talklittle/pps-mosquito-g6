package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class Grp6Blank3 extends Player {
	private int numLights;
	
	@Override
	public String getName() {
		return "Group 6 Blank 3 Lights";
	}

	@Override
	public void startNewGame(Set<Line2D> walls, int NumLights) {
		this.numLights = NumLights;
	}

	Point2D firstLight = null;
	Point2D secondLight = null;
	Point2D thirdLight = null;
	
	@Override
	public Set<Light> getLights() {
		HashSet<Light> ret = new HashSet<Light>();
	
		firstLight = new Point2D.Double(50, 50);
		secondLight = new Point2D.Double(30.1, 50);
		thirdLight = new Point2D.Double(69.9, 50);
		
		Light firstL = new Light(firstLight.getX(),firstLight.getY(), 1,1,1);
		ret.add(firstL);
		
		Light secondL = new Light(secondLight.getX(), secondLight.getY(),40, 20, 1);
		ret.add(secondL);
		
		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),40, 20, 1);
		ret.add(thirdL);
		
		return ret;
	}

	@Override
	public Collector getCollector() {
		Random r = new Random();
		Collector c = new Collector(firstLight.getX()+0.5,firstLight.getY() +0.5);
		return c;
	}


}
