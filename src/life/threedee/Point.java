package life.threedee;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import life.threedee.game.GameUtilities;

/**
 * A single point. Not much to say about it.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 * 
 */
public class Point implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2430041703358196041L;

	/**
	 * The x coordinate of this Point
	 */
	public final double x;

	/**
	 * The y coordinate of this Point
	 */
	public final double y;

	/**
	 * The z coordinate of this Point
	 */
	public final double z;

	/**
	 * Creates a point with the same components as the vector given
	 * 
	 * @param vector
	 */
	public Point(Vector vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	/**
	 * Creates a point out of the three components passed
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Outputs the string in (x, y, z) format
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	/**
	 * Returns a new point consisting of the sum of this Point and Point a
	 * 
	 * @param a
	 * @return
	 */
	public Point add(Point a) {
		return new Point(x + a.x, y + a.y, z + a.z);
	}

	/**
	 * Returns a new point consisting of the difference between this Point and
	 * Point a
	 * 
	 * @param a
	 * @return
	 */
	public Point subtract(Point a) {
		return new Point(x - a.x, y - a.y, z - a.z);
	}

	/**
	 * Returns a new point where each component has been multiplied by the
	 * elasticity
	 * 
	 * @param elasticity
	 * @return
	 */
	public Point stretch(double elasticity) {
		return new Point(x * elasticity, y * elasticity, z * elasticity);
	}
	
	public void serialize(OutputStream o) throws IOException {
		GameUtilities.writeDouble(o, x);
		GameUtilities.writeDouble(o, y);
		GameUtilities.writeDouble(o, z);
	}
	
	public static Point deserialize(InputStream i) throws IOException {
		double x = GameUtilities.readDouble(i);
		double y = GameUtilities.readDouble(i);
		double z = GameUtilities.readDouble(i);
		return new Point(x, y, z);
	}
}
