package life.threedee;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import life.threedee.game.GameUtilities;

/**
 * Represents a three dimensional vector in component form and provides methods
 * for common calculations such as dot product and cross product as well as
 * methods for converting to and from polar coordinates
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 */
public class Vector implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7819196802404488035L;

	/**
	 * X unit vector
	 */
	public static final Vector UNIT_X = new Vector(1, 0, 0);

	/**
	 * Y unit vector
	 */
	public static final Vector UNIT_Y = new Vector(0, 1, 0);

	/**
	 * Z unit vector
	 */
	public static final Vector UNIT_Z = new Vector(0, 0, 1);

	/**
	 * The x component of this vector
	 */
	public final double x;

	/**
	 * The y component of this vector
	 */
	public final double y;

	/**
	 * The z component of this vector
	 */
	public final double z;

	/**
	 * The length of this vector, lazily calculated
	 */
	private double s;

	/**
	 * Constructs a new vector with the same components as the point
	 * 
	 * @param point
	 */
	public Vector(Point point) {
		this.s = Double.NaN;
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}

	/**
	 * Constructs a new vector with the given components
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector(double x, double y, double z) {
		this.s = Double.NaN;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructs a new vector representing the vector from point1 to point2
	 * 
	 * @param point1
	 * @param point2
	 */
	public Vector(Point point1, Point point2) {
		this.x = (point2.x - point1.x);
		this.y = (point2.y - point1.y);
		this.z = (point2.z - point1.z);
		this.s = Double.NaN;
	}

	/**
	 * Returns the value of the scalar, and calculates it if it hasn't been
	 * accessed yet
	 * 
	 * @return
	 */
	public double s() {
		if (s != s) {
			s = Math.sqrt(x * x + y * y + z * z);
		}

		return s;
	}

	/**
	 * Converts the vector to a string representation
	 */
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + s() + ")";
	}

	/**
	 * Calculates the cross product of this vector and the vector v using
	 * matrices
	 * 
	 * @param v
	 * @return
	 */
	public Vector crossProduct(Vector v) {
		Vector u = this;
		double u1 = u.x;
		double u2 = u.y;
		double u3 = u.z;

		double v1 = v.x;
		double v2 = v.y;
		double v3 = v.z;

		Matrix x = new Matrix(new double[][] { { u2, u3 }, { v2, v3 } });
		Matrix y = new Matrix(new double[][] { { u1, u3 }, { v1, v3 } });
		Matrix z = new Matrix(new double[][] { { u1, u2 }, { v1, v2 } });

		return new Vector(x.determinant(), -y.determinant(), z.determinant());
	}

	/**
	 * Calculates the dot product of this and vector
	 * 
	 * @param vector
	 * @return
	 */
	public double dotProduct(Vector vector) {
		return x * vector.x + y * vector.y + z * vector.z;
	}

	/**
	 * Returns a double[] representing this vector in polar form
	 * 
	 * @return
	 */
	public double[] polarTransform() {
		return new double[] { yaw(), pitch() };
	}

	/**
	 * Returns the yaw of this vector
	 * 
	 * @return
	 */
	public double yaw() {
		double d = Math.atan(z / x);
		if (x < 0) {
			d += Math.PI;
		}
		return d;
	}

	/**
	 * Calculates the pitch of this vector
	 * 
	 * @return
	 */
	public double pitch() {
		return Math.atan(y / Math.sqrt(x * x + z * z));
	}

	/**
	 * Constructs a new vector from the given yaw, pitch, and length
	 * 
	 * @param yaw
	 * @param pitch
	 * @param scalar
	 * @return
	 */
	public static Vector fromPolarTransform(double yaw, double pitch,
			double scalar) {
		if (Math.abs(pitch) > Math.PI / 2) {
			pitch = Math.PI - pitch;
			yaw += Math.PI;
			yaw %= 2 * Math.PI;
		}
		double x = Math.cos(yaw), z = Math.sin(yaw), y = Math.tan(pitch)
				* Math.sqrt(x * x + z * z);
		if (yaw != yaw) {
			x = 0;
			y = 2 * pitch / Math.PI;
			z = 0;
		}
		return new Vector(x, y, z).setScalar(scalar);
	}

	/**
	 * Constructs a new vector representing the sum of this and v
	 * 
	 * @param v
	 * @return
	 */
	public Vector add(Vector v) {
		return new Vector(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Multiplies this vector by the scalar
	 * 
	 * @param scalar
	 * @return
	 */
	public Vector scalarProduct(double scalar) {
		return new Vector(x * scalar, y * scalar, z * scalar);
	}

	/**
	 * Returns a new vector with the scalar given
	 * 
	 * @param scalar
	 * @return
	 */
	public Vector setScalar(double scalar) {
		double mod = scalar / s();
		return new Vector(x * mod, y * mod, z * mod);
	}

	@Override
	public boolean equals(Object o) {
		try {
			Vector v = (Vector) o;
			return GameUtilities.equals(v.x, this.x)
					&& GameUtilities.equals(v.y, this.y)
					&& GameUtilities.equals(v.z, this.z);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public void serialize(OutputStream o) throws IOException {
		GameUtilities.writeDouble(o, x);
		GameUtilities.writeDouble(o, y);
		GameUtilities.writeDouble(o, z);
	}
	
	public static Vector deserialize(InputStream i) throws IOException {
		double x = GameUtilities.readDouble(i);
		double y = GameUtilities.readDouble(i);
		double z = GameUtilities.readDouble(i);
		return new Vector(x, y, z);
	}
}
