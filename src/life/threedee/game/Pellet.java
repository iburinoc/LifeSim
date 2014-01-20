package life.threedee.game;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;

/**
 * A pellet that can be eaten.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class Pellet extends Consumable{
    // A third of a circle in radians. Used for finding the locations of the lower points.
	private static final double C_THIRD = 2*Math.PI/3;
	
	// The top point of the tetrahedron.
	private Point top;
	
	/**
	 * Constructs a Pellet at a given location.
	 * @param center The center of this pellet.
	 */
	public Pellet(Point center) {
		super(center, 3, 3, Math.PI / 90);
	}
	
	protected void generate() {
		top = new Point(0, 0.250, 0);
		double cyaw = yaw;
		for(int i = 0; i < 3; i++) {
			p[i] = new Point(Vector.fromPolarTransform(cyaw, 0, 0.2));
			cyaw += C_THIRD;
		}
		for(int i = 0; i < 3; i++) {
			t[i] = new Triangle(top, p[i], p[(i+1)%3]);
		}
		translate(new Vector(center.x, 0.625, center.z));
	}

	public void eat(Game g, Player p){
        eaten = true;
        g.pelletEaten();
        p.stop(1);
    }

	@Override
	public Color c() {
		return Color.YELLOW;
	}
}
