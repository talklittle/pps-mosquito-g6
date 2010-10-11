package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class Grp6Y3 extends Player {
	private int numLights;
	
	@Override
	public String getName() {
		return "Group 6 Y-Map 3 Lights";
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
	
		firstLight = new Point2D.Double(45, 80);
		secondLight = new Point2D.Double(62.5, 74);
		thirdLight = new Point2D.Double(79.5, 65.5);
		
		Light firstL = new Light(firstLight.getX(),firstLight.getY(), 40,20,1);
		ret.add(firstL);
		
		Light secondL = new Light(secondLight.getX(), secondLight.getY(),1, 1, 1);
		ret.add(secondL);
		
		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),40, 20, 1);
		ret.add(thirdL);
		
		return ret;
	}

	@Override
	public Collector getCollector() {
		Random r = new Random();
		Collector c = new Collector(secondLight.getX()+0.5,secondLight.getY() +0.5);
		return c;
	}


}
