package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PutLights {
	
	private static Random random = new Random();
	private static double stepSmall = Math.toRadians(15);
	private static double stepLarge = Math.toRadians(45);
	private static int maximumLights = 20;
	
	
	private static Set<HelperLight> putLightRandom(Set<Line2D> walls, Set<HelperLight> lights, int numLightLeft){
		Point2D next;
		for (int i = 0; i < numLightLeft; i++) {
			do {
				next = new Point2D.Double(random.nextDouble() * 100.0, random.nextDouble() * 100.0);
			} while (CollideWithWall.isCollideWithWall(next, walls));
			HelperLight l = new HelperLight(next.getX(), next.getY(), 1, 0, 0);
			lights.add(l);
		}
		return lights;
	}
	
	private static Set<HelperLight> putLightsRecursive(Set<Line2D> walls, HelperLight base, int numLight, double radient, Set<HelperLight> lights, 
			double radius, int phase, int numLightPlaced, Set<HelperLight> lightsInPhase){
		//terminate constraints
		if(numLight<=numLightPlaced){
			return lights;
		}
//		if(numLightPlaced>=maximumLights){
//			return putLightRandom(walls, lights, numLight-numLightPlaced);
//		}
		if(radius<=0){
			return lights;
		}
		if((radient>= 2*Math.PI) && lightsInPhase.isEmpty()){
			return putLightsRecursive(walls, base, numLight, 0, lights, radius-5, phase, numLightPlaced, new HashSet<HelperLight>());
		}
		if((radient>= 2*Math.PI) && !lightsInPhase.isEmpty()){
			//move to another phase
			for(HelperLight l: lightsInPhase){
				int sizeNow = lights.size();
				lights = putLightsRecursive(walls, l, numLight, 0, lights, 19.9, phase+1, numLightPlaced, new HashSet<HelperLight>());
				numLightPlaced += lights.size() - sizeNow;
			}
			return lights;
		}
		Point2D pedalPoint = new Point2D.Double(base.getX()+(radius*Math.cos(radient)), base.getY()+(radius*Math.sin(radient)));
		HelperLight pedal = new HelperLight(pedalPoint.getX(), pedalPoint.getY(), -1, -1, -1);
		pedal.setBase(base);
		pedal.setPhase(phase);
		if(CollideWithWall.isCollideWithWall(pedalPoint, walls)||CollideWithWall.isCollideWithWall(base.getPoint(), pedalPoint, walls)
				||CollideWithWall.isCollideWithOtherLights(pedal, lights, walls)
				|| OutOfBounds.isOutOfBounds(pedal.getPoint())){
			return putLightsRecursive(walls, base, numLight, radient+stepSmall, lights, radius, phase, numLightPlaced, lightsInPhase);
		}
		pedal.setD(60);
		pedal.setT(24);
		pedal.setS(24*(2-(phase-1)%3));
		lights.add(pedal);
		lightsInPhase.add(pedal);
		return putLightsRecursive(walls, base, numLight, radient+stepLarge, lights, radius, phase, numLightPlaced+1, lightsInPhase);
		// try to span flowery...  do each phase.
		// if step to 360 and light left, reduce radius and start everything again
		// For each light in the same phase,
		// hit wall, step small
		// if collide with other lights, step small
		// if not any of them, put lights and step large

	}
	
	public static Set<HelperLight> putLights(Set<Line2D> walls, HelperLight base, int numLight){
		
		if(CollideWithWall.isCollideWithWall(base.getPoint(), walls) || OutOfBounds.isOutOfBounds(base.getPoint())){
			return new HashSet<HelperLight>();
		}
		
		// Increase stepLarge if we have fewer than 8 lights
		if (numLight < 8) {
			stepLarge = Math.toRadians(360.0 / (double) numLight);
		}
		
		HelperLight l = new HelperLight(base.getX(),base.getY(), 1,1,1);
		l.setBase(null);
		l.setPhase(0);
		Set<HelperLight> lights = new HashSet<HelperLight>();
		lights.add(base);
		Set<HelperLight> lightsInPhase = new HashSet<HelperLight>();
		if(numLight==1){
			return lights;
		}else{
			lights = putLightsRecursive(walls, base, numLight,0,lights,19.9, 1, 1, lightsInPhase);
			if (lights.size() < numLight) {
				return putLightRandom(walls, lights, numLight-lights.size());
			}
			return lights;
		}
		
	}
	
//	public static Point2D.Double collectorPlace()
//	{
//		int length = list.size();
//		
//		int midLength = length/2; /* rounds down naturally */
//		
//		/* start at 20 and for each light we go out,
//		 * we add 20 seconds to the maximum. This will be each
//		 * light's d-value 
//		 */
//		int maxTime = 20 + midLength*20; 
//		
//		/* To get the location of middle light, and put the collector there */
//		Point2D.Double collectorPlacement = (Double) list.get(midLength).getLocation();
//		
//
//		int right = 0;
//		/* To add the time to the lights to the right of the center light */
//		for(int midRight = midLength + 1; midRight < list.size(); midRight++)
//		{
//			right = right + 20;
//			list.get(midRight).setD(maxTime);
//			list.get(midRight).setT(right);
//		}
//		
//		int left = 0;
//		/* To add the time to the lights to the left of the center light */
//		for(int midLeft = midLength -1; midLeft >=0; midLeft--)
//		{
//			left = left + 20;
//			list.get(midLeft).setD(maxTime);
//			list.get(midLeft).setT(left);
//			
//		}
//		
//		return collectorPlacement;
//		
//	}
	
}
