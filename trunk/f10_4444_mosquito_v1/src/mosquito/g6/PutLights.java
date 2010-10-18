package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import mosquito.sim.Light;

public class PutLights {
	
	private static Random random = new Random();
	
	public static Set<Light> putLights(Set<Line2D> walls, Point2D base, int numLight, double initialRadient){
		if(CollideWithWall.isCollideWithWall(base, walls) || OutOfBounds.isOutOfBounds(base)){
			return null;
		}else{
			int numPlaced = 0;
			Light l = new Light(base.getX(),base.getY(), 1,1,1);
			Set<Light> result = new HashSet<Light>();
			result.add(l);
			if(numLight==1){
				return result;
			}
			numPlaced++;
			
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
						for ( ; numPlaced < numLight; numPlaced++) {
							do {
								next = new Point2D.Double(random.nextDouble() * 100.0, random.nextDouble() * 100.0);
							} while (CollideWithWall.isCollideWithWall(next, walls));
							l = new Light(next.getX(), next.getY(), 1, 0, 0);
							result.add(l);
						}
						return result;
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
					l = new Light(next.getX(), next.getY(), 60, 24, 24*(2-(phase-1)%3));
					result.add(l);
					pedalIndex++;
					numPlaced++;
					numPlacedThisPhase++;
				}
			}
			return result;
		}
	}
}
