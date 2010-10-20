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
	
	private static Set<HelperLight> putLightsRecursive(Set<Line2D> walls, HelperLight base, int numLightLeft, double radient, Set<HelperLight> lights, 
			boolean reach360, double radius, int phase, int numLightPlaced){
		
		if(numLightLeft==0){
			return lights;
		}
		if(numLightPlaced==20){
			lights = putLightRandom(walls, lights, numLightLeft);
			return lights;
		}
		// try to span flowery...  do each phase.
		//hit wall, step small
		//if collide with other lights, step small
		if()
		
		
		
		
		Point2D next;
		int phase = 1;
		int pedalIndex = 0;
		int numPlacedThisPhase = 0;
		int numLightPerPhase;
		while(numPlaced < numLight){
			if(pedalIndex==(phase*8)){
				// If we couldn't place any during this phase, quit
				if (numPlacedThisPhase == 0) {
					// TODO a good way to place the remaining lights
					
				}
				pedalIndex = 0;
				numPlacedThisPhase = 0;
				phase++;
			}
			numLightPerPhase = Math.min(phase*8, numLight);
			next = new Point2D.Double(base.getX()+((19.9*phase)*Math.cos(initialRadient+(pedalIndex*2*Math.PI/numLightPerPhase))),
					base.getY()+((19.9*phase)*Math.sin(initialRadient+(pedalIndex*2*Math.PI/numLightPerPhase))));
			if(CollideWithWall.isCollideWithWall(base, next, walls) || OutOfBounds.isOutOfBounds(next)){
				pedalIndex++;
			}else{
				list.add(next);
				l = new Light(next.getX(), next.getY(), 60, 24, 24*(2-(phase-1)%3));
				result.add(l);
				pedalIndex++;
				numPlaced++;
				numPlacedThisPhase++;
			}
		}
		return result;
	}
	
	public static Set<HelperLight> putLights(Set<Line2D> walls, HelperLight base, int numLight){
		
		if(CollideWithWall.isCollideWithWall(base, walls) || OutOfBounds.isOutOfBounds(base.getPoint())){
			return null;
		}else{
			HelperLight l = new HelperLight(base.getX(),base.getY(), 1,1,1);
			l.setBase(null);
			l.setPhase(0);
			Set<HelperLight> lights = new HashSet<HelperLight>();
			lights.add(base);
			if(numLight==1){
				return lights;
			}else{
				return putLightsRecursive(walls, base, numLight-1,0,lights,false,19.9, 1, 1);
			}
		}
	}
	public static Point2D.Double collectorPlace()
	{
		int length = list.size();
		
		int midLength = length/2; /* rounds down naturally */
		
		/* start at 20 and for each light we go out,
		 * we add 20 seconds to the maximum. This will be each
		 * light's d-value 
		 */
		int maxTime = 20 + midLength*20; 
		
		/* To get the location of middle light, and put the collector there */
		Point2D.Double collectorPlacement = (Double) list.get(midLength).getLocation();
		

		int right = 0;
		/* To add the time to the lights to the right of the center light */
		for(int midRight = midLength + 1; midRight < list.size(); midRight++)
		{
			right = right + 20;
			list.get(midRight).setD(maxTime);
			list.get(midRight).setT(right);
		}
		
		int left = 0;
		/* To add the time to the lights to the left of the center light */
		for(int midLeft = midLength -1; midLeft >=0; midLeft--)
		{
			left = left + 20;
			list.get(midLeft).setD(maxTime);
			list.get(midLeft).setT(left);
			
		}
		
		return collectorPlacement;
		
	}
	
}
