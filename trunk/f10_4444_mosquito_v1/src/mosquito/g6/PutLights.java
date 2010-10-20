package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PutLights {
	
	private static Random random = new Random();
	private static double stepSmall = 15;
	private static double stepLarge = 45;
	private static int maximumLights = 20;
	
	
	private static Set<HelperLight> putLightRandom(Set<Line2D> walls, Set<HelperLight> lights, int numLightLeft){
		Point2D next;
		ArrayList<Point2D> list = new ArrayList<Point2D>();
		for (int i = 0; i < numLightLeft; i++) {
			do {
				next = new Point2D.Double(random.nextDouble() * 100.0, random.nextDouble() * 100.0);
				list.add(next);
			} while (CollideWithWall.isCollideWithWall(next, walls));
			HelperLight l = new HelperLight(next.getX(), next.getY(), 1, 0, 0);
			lights.add(l);
		}
		return lights;
	}
	
	private static Set<HelperLight> putLightsRecursive(Set<Line2D> walls, HelperLight base, int numLight, double radient, Set<HelperLight> lights, 
			double radius, int phase, int numLightPlaced, boolean thereIsOneLightInPhase, Set<HelperLight> lightsInPhase){
		//terminate constraints
		if(numLight<=numLightPlaced){
			return lights;
		}
		if(numLightPlaced>=maximumLights){
			lights = putLightRandom(walls, lights, numLight-numLightPlaced);
			return lights;
		}
		if(radius<=0){
			return lights;
		}
		if((radient>=360)&&!thereIsOneLightInPhase){
			return putLightsRecursive(walls, base, numLight, 0, lights, radius-5, phase, numLightPlaced, false, new HashSet<HelperLight>());
		}
		if((radient>=360&&thereIsOneLightInPhase)){
			//move to another phase
			for(HelperLight l: lightsInPhase){
				lights = putLightsRecursive(walls, l, numLight, 0, lights, 19.9, phase+1, numLightPlaced, false, new HashSet<HelperLight>());
			}
			return lights;
		}
		Point2D pedalPoint = new Point2D.Double(base.getX()+(radius*Math.cos(radient)), base.getY()+(radius*Math.sin(radient)));
		HelperLight pedal = new HelperLight(pedalPoint.getX(), pedalPoint.getY(), -1, -1, -1);
		pedal.setBase(base);
		pedal.setPhase(phase);
		if(CollideWithWall.isCollideWithWall(pedalPoint, walls)||CollideWithWall.isCollideWithWall(base.getPoint(), pedalPoint, walls)||CollideWithWall.isCollideWithOtherLights(pedal, lights)){
			return putLightsRecursive(walls, base, numLight, radient+stepSmall, lights, radius, phase, numLightPlaced, thereIsOneLightInPhase, lightsInPhase);
		}
		pedal.setD(60);
		pedal.setT(24);
		pedal.setS(24*(2-(phase-1)%3));
		lights.add(pedal);
		lightsInPhase.add(pedal);
		return putLightsRecursive(walls, base, numLight, radient+stepLarge, lights, radius, phase, numLightPlaced+1, true, lightsInPhase);
		// try to span flowery...  do each phase.
		// if step to 360 and light left, reduce radius and start everything again
		// For each light in the same phase,
		// hit wall, step small
		// if collide with other lights, step small
		// if not any of them, put lights and step large

	}
	
	public static Set<HelperLight> putLights(Set<Line2D> walls, HelperLight base, int numLight){
		
		if(CollideWithWall.isCollideWithWall(base.getPoint(), walls) || OutOfBounds.isOutOfBounds(base.getPoint())){
			return null;
		}else{
			HelperLight l = new HelperLight(base.getX(),base.getY(), 1,1,1);
			l.setBase(null);
			l.setPhase(0);
			Set<HelperLight> lights = new HashSet<HelperLight>();
			lights.add(base);
			Set<HelperLight> lightsInPhase = new HashSet<HelperLight>();
			if(numLight==1){
				return lights;
			}else{
				return putLightsRecursive(walls, base, numLight,0,lights,19.9, 1, 1, false, lightsInPhase);
			}
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
