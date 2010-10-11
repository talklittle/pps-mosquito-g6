package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Collector;
import mosquito.sim.Light;
import mosquito.sim.Player;

public class Grp6Blank10 extends Player {
	private int numLights;
	
	@Override
	public String getName() {
		return "Group 6 Blank 10 Lights";
	}

	@Override
	public void startNewGame(Set<Line2D> walls, int NumLights) {
		this.numLights = NumLights;
	}

	Point2D firstLight = null;
	Point2D secondLight = null;
	Point2D thirdLight = null;
	Point2D fourthLight = null;
	Point2D fifthLight = null;
	
	Point2D sixthLight = null;
	Point2D seventhLight = null;
	Point2D eighthLight = null;
	Point2D ninthLight = null;
	Point2D tenthLight = null;
	
	@Override
	public Set<Light> getLights() {
		HashSet<Light> ret = new HashSet<Light>();
	
		// middle light
		firstLight = new Point2D.Double(50, 50);
		
		// NSEW lights
		secondLight = new Point2D.Double(30.1, 50);
		thirdLight = new Point2D.Double(69.9, 50);
		fourthLight = new Point2D.Double(50, 69.9);
		fifthLight = new Point2D.Double(50, 30.1);
		
		//outer lights
		sixthLight = new Point2D.Double(30.1, 30.1);
		seventhLight = new Point2D.Double(69.9, 30.1);
		eighthLight = new Point2D.Double(30.1, 69.9);
		ninthLight = new Point2D.Double(69.9, 69.9);
		tenthLight = new Point2D.Double(1,1);
		
		// center light
		Light firstL = new Light(firstLight.getX(),firstLight.getY(), 1,1,1);
		ret.add(firstL);
		
		// NSEW lights
		Light secondL = new Light(secondLight.getX(), secondLight.getY(),60,40,1);
		ret.add(secondL);
		
		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),60,40,1);
		ret.add(thirdL);
		
		Light fourthL = new Light(fourthLight.getX(), fourthLight.getY(),60,40,1);
		ret.add(fourthL);
		
		Light fifthL = new Light(fifthLight.getX(), fifthLight.getY(),60,40,1);
		ret.add(fifthL);
		
		// outer lights
		Light sixthL = new Light(sixthLight.getX(), sixthLight.getY(),60, 20, 1);
		ret.add(sixthL);
		
		Light seventhL = new Light(seventhLight.getX(), seventhLight.getY(),60, 20, 1);
		ret.add(seventhL);
		
		Light eighthL = new Light(eighthLight.getX(), eighthLight.getY(),60, 20, 1);
		ret.add(eighthL);
		
		Light ninthL = new Light(ninthLight.getX(), ninthLight.getY(),60, 20, 1);
		ret.add(ninthL);
		
		Light tenthL = new Light(tenthLight.getX(), tenthLight.getY(),1, 0, 1);
		ret.add(tenthL);
		
		return ret;
	}

	@Override
	public Collector getCollector() {
		Random r = new Random();
		Collector c = new Collector(firstLight.getX()+0.5,firstLight.getY() + 0.5);
		return c;
	}


}
