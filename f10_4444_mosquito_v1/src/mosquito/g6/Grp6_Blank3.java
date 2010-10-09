package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class Grp6_Blank3 extends Player {
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
	//Point2D secondLight = null;
	//Point2D thirdLight = null;
	
	@Override
	public Set<Light> getLights() {
		HashSet<Light> ret = new HashSet<Light>();
	
		firstLight = new Point2D.Double(50, 50);
		Light l = new Light(firstLight.getX(),firstLight.getY(), 1,10,1);
			
		ret.add(l);
		return ret;
	}

	@Override
	public Collector getCollector() {
		Random r = new Random();
		Collector c = new Collector(firstLight.getX()+1,firstLight.getY() +1);
		return c;
	}


}
