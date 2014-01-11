package life.threedee.game;

import static life.threedee.game.GameUtilities.PX_METER;
import static life.threedee.game.GameUtilities.WALL_HEIGHT;

import java.awt.Color;
import java.awt.image.BufferedImage;

import life.threedee.Point;
import life.threedee.Vector;
import life.threedee.game.maps.MapPlane;

public class GhostHouseDoor extends MapPlane {

	public GhostHouseDoor(Point p, Vector n) {
		super(p, n, new BufferedImage(2 * PX_METER, WALL_HEIGHT * PX_METER, BufferedImage.TYPE_INT_ARGB));
	}

	public boolean sameSide(Point point1, Point point2) {
		Color color = getRData(new Vector(point1, point2), point1, Double.NaN).c;
        return super.sameSide(point1, point2) || color == null;
	}
}
