package life.threedee.game;

import java.awt.Color;

import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.Vector;

/**
 * This is a special kind of plane that there are two instances of, on either side of the map. If you see a tunnel plane, the game knows to draw what is on the other side.
 */
public class TunnelPlane extends Plane{
    /**
     * Represent whether the plane is on the right side of the map of the left side.
     */
	public final double id;

    /**
     * "Default" constructor. Works like the regular plane constructor.
     * @param origin A point on the plane. Doesn't matter where on the plane. It is used to determine what side of the map the plane is on.
     * @param normal The normal to the plane.
     */
    public TunnelPlane(Point origin, Vector normal){
        super(origin, normal, Color.BLACK);
        id = Math.signum(origin.x);
    }
}
