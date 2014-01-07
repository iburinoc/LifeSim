package life.threedee.game.maps;

import static life.threedee.game.GameUtilities.idCount;

import java.awt.image.BufferedImage;

import life.threedee.Point;
import life.threedee.TexturedPlane;
import life.threedee.Vector;

public class MapPlane extends TexturedPlane implements MapFeature {
	public final int id;
	
	public MapPlane(Point p, Vector n, BufferedImage texture) {
		super(p, n, texture);
		id = idCount;
		idCount++;
	}

	public int getID() {
		return id;
	}
}
