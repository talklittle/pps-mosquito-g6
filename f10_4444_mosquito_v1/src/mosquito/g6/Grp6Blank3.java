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
	
//		// horizontal
//		firstLight = new Point2D.Double(50, 50);
//		secondLight = new Point2D.Double(30.1, 50);
//		thirdLight = new Point2D.Double(69.9, 50);
		// diagonal bottom-left to top-right
		firstLight = new Point2D.Double(50, 50);
		secondLight = new Point2D.Double(50-14.14+0.07, 50+14.14-0.07);
		thirdLight = new Point2D.Double(50+14.14-0.07, 50-14.14+0.07);
		
		/*
		 * VELOCITY
		 * 
		 * ***Lights separated by radius only need to be off for 12 turns.***
		 * Lights separated by 1 radius are 20 meters apart.
		 * After turning off first light, mosquitoes need to move more than half of 20m before light turns on again.
		 * Speed is 1 meter per turn. with 30 degree cone towards light.
		 * Worst case they only move (1*cos(30)) meters or 0.866 meters towards light.
		 * So they need to go 1/0.866 or 1.154734411 turns to guarantee 1 meter toward 2nd light.
		 * So, for 10 meters it's 10*1.1547 = 11.547 rounded up to 12 turns.
		 */
		
		
		Light firstL = new Light(firstLight.getX(),firstLight.getY(), 1,1,1);
//		// 60% of the time gets t=275. 40% of the time requires 2nd iteration so 480.
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),252, 240, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),252, 240, 1);
//		// 60% of the time gets t=260. 40% of the time requires 2nd iteration so 450.
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),240, 228, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),240, 228, 1);
//		// 60% of the time gets t=245. 40% of the time requires 2nd iteration so 450.
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),225, 213, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),225, 213, 1);
//		// gets about t=232 best. often (66% of the time) takes 2 iterations though. so 400+
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),220, 200, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),220, 200, 1);
//		// 70% of the time gets t=310. 20% takes 220. 10% takes 380.
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),100, 88, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),100, 88, 1);
//		// usually gets t=283 = best. occasionally takes another iteration so 350.
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),70, 50, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),70, 50, 1);
//		// Diagonal, gets about t=260 usually and on average. occasionally +- 50, so 210 to 340
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),50, 38, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),50, 38, 1);
//		// gets about t=300 usually and on average. best was 255
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),40, 20, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),40, 20, 1);
		// Diagonal, gets about t=260,290 usually. occasionally +- 30, so 230 to 320
		// Horizontal seems worse!
		Light secondL = new Light(secondLight.getX(), secondLight.getY(),40, 28, 1);
		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),40, 28, 1);
//		// Horizontal, gets about t=300 usually and on average. occasionally +- 40, so 260 to 340
//		Light secondL = new Light(secondLight.getX(), secondLight.getY(),36, 24, 1);
//		Light thirdL = new Light(thirdLight.getX(), thirdLight.getY(),36, 24, 1);
		
		ret.add(firstL);
		ret.add(secondL);
		ret.add(thirdL);

		if (false) {
			// Fill in dark lights up to minimum
			for (int i = 3; i < numLights; i++)
				ret.add(new Light(i / 100, i % 100, 1, 0, 1));
		}
		
		return ret;
	}

	@Override
	public Collector getCollector() {
		Random r = new Random();
		Collector c = new Collector(firstLight.getX()+0.5,firstLight.getY() +0.5);
		return c;
	}


}
