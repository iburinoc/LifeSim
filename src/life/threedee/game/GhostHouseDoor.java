package life.threedee.game;

import static life.threedee.game.GameUtilities.WALL_HEIGHT;
import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.Vector;
import life.threedee.game.maps.MapFeature;

/**
 * The door to the ghost house, which is transparent, but prevents user from entering it. 
 *
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class GhostHouseDoor extends Plane implements MapFeature {

    //It's id, once again, for deterministic generation.
	private final int id;
	
	/**
	 * Constructs a new GhostHouseDoor with the given origin and normal.
	 * @param p The origin point.
	 * @param n The normal vector.
	 */
	public GhostHouseDoor(Point p, Vector n) {
		super(p, n, GameUtilities.BLANK);
		id = GameUtilities.idCount++;
	}
	
	public int getID() {
		return id;
	}

	// Whether or not a given Point p is contained within the area defined by the door.
	private boolean contained(Point p) {
		Vector d = new Vector(this.origin, p);
		return d.x >= 0 && d.x < 2 && d.y >= 0 && d.y <= WALL_HEIGHT && d.z == 0;
	}
	
	public boolean sameSide(Point point1, Point point2) {
		return super.sameSide(point1, point2) || !contained(this.intersection(new Vector(point1, point2), point1));
	}
}
