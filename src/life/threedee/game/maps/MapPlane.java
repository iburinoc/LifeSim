package life.threedee.game.maps;

import java.awt.image.BufferedImage;

import life.threedee.Point;
import life.threedee.TexturedPlane;
import life.threedee.Vector;

public class MapPlane extends TexturedPlane {
	private static int idCount = 0;
	
	public final int id;
	
	public MapPlane(Point p, Vector n, BufferedImage texture) {
		super(p, n, texture);
		id = idCount;
		idCount++;
	}

}
