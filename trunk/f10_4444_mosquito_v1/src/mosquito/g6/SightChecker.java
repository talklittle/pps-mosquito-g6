package mosquito.g6;

import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Set;

import mosquito.sim.Light;

public class SightChecker {
	/**
	 * Get the set of lights that are within range and not obstructed from originLight.
	 * @param originLight
	 * @param otherLights
	 * @param walls
	 * @return
	 */
	public Set<Light> getInSight(Light originLight, Set<Light> otherLights, Set<Line2D> walls) {
		HashSet<Light> sightLights = new HashSet<Light>();
		// Get lights within 20 meters
		for (Light light : otherLights) {
			double distance = originLight.getLocation().distance(light.getLocation());
			if (distance < 20.0) {
				sightLights.add(light);
			}
		}
		
		// Draw lines for each pair of lights (originLight, sightLights[i]) and check for walls
		HashSet<Light> goodLights = new HashSet<Light>();
		for (Light light : sightLights) {
			if (!CollideWithWall.isCollideWithWall(originLight.getLocation(), light.getLocation(), walls)) {
				goodLights.add(light);
			}
		}
		
		return goodLights;
	}
}
