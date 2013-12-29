package life.threedee.game.maps;

import java.awt.image.BufferedImage;
import java.util.List;

import life.threedee.ThreeDeeObject;
import life.threedee.game.GameUtilities;

/**
 * Parses 2D pacman map image and builds the object list for it.
 * @author iburinoc
 *
 */
public class MapBuilder {
	private static final BufferedImage map = GameUtilities.loadImage("resources/map.png");
	
	private static final int PX_TILE = 8;
	
	public static List<ThreeDeeObject> createMap() {
		
		throw new RuntimeException("Not implemented");
	}
}
