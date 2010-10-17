package mosquito.g6;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;
import mosquito.sim.Light;

public class PutLights {
	
	public static Set<Light> putLights(Set<Line2D> walls, Point2D base, int numLight, double initialRadient){
		if(CollideWithWall.isCollideWithWall(base, walls)){
			return null;
		}else{
			Light l = new Light(base.getX(),base.getY(), 1,1,1);
			Set<Light> result = new HashSet<Light>();
			result.add(l);
			if(numLight==1){
				return result;
			}
			Point2D next;
			int phase = 1;
			int pedalIndex = 0;
			int numLightPerPhase;
			while(numLight>0){
				if(pedalIndex==(phase*8)){
					pedalIndex = 0;
					phase++;
				}
				numLightPerPhase = Math.min(phase*8, numLight);
				next = new Point2D.Double(base.getX()+((19.9*phase)*Math.cos(initialRadient+(pedalIndex*2*Math.PI/numLightPerPhase))),
						base.getY()+((19.9*phase)*Math.sin(initialRadient+(pedalIndex*2*Math.PI/numLightPerPhase))));
				if(CollideWithWall.isCollideWithWall(base, next, walls)){
					pedalIndex++;
				}else{
					l = new Light(next.getX(), next.getY(), 60, 24, 24*(2-(phase-1)%3));
					result.add(l);
					pedalIndex++;
					numLight--;
				}
			}
			return result;
		}
	}
}
