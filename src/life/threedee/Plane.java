package life.threedee;

import java.awt.Color;

/**
 * A Plane, represented with an origin point and normal vector. Also has a color
 * to render if it is the closest object.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 */
public class Plane implements ThreeDeeObject {

	/**
	 * The origin Point of this Plane. This can be any point on the plane, it
	 * makes no difference
	 */
	protected Point origin;

	/**
	 * The normal vector for this plane
	 */
	protected Vector normal;

	/**
	 * The color of the plane
	 */
	public Color c;

	/**
	 * Constructs a new plane from the three points with a random color
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	public Plane(Point a, Point b, Point c) {
		this(a, b, c, new Color((int) (Math.random() * 256),
				(int) (Math.random() * 256), (int) (Math.random() * 256)));
	}

	/**
	 * Constructs a new plane from the three points with the given color
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param colour
	 */
	public Plane(Point a, Point b, Point c, Color colour) {
		origin = a;
		normal = new Vector(a, b).crossProduct(new Vector(a, c));

		this.c = colour;
	}

	/**
	 * Constructs a new plane with the given origin and normal with a random
	 * color
	 * 
	 * @param origin
	 * @param normal
	 */
	public Plane(Point origin, Vector normal) {
		this(origin, normal, new Color((int) (Math.random() * 256),
				(int) (Math.random() * 256), (int) (Math.random() * 256)));
	}

	/**
	 * Constructs a new plane with the given origin and normal with the given
	 * color
	 * 
	 * @param origin
	 * @param normal
	 * @param c
	 */
	public Plane(Point origin, Vector normal, Color c) {
		this.origin = origin;
		this.normal = normal;

		this.c = c;
	}

	@Override
	public TColorTransfer getRData(Vector vector, Point point, double minT) {
		double t = calculateT(vector, point, minT);
		return new TColorTransfer(t, this.c, this);
	}

	@Override
	public double calculateT(Vector vector, Point point) {
		return -(normal.x * point.x - normal.x * origin.x + normal.y * point.y
				- normal.y * origin.y + normal.z * point.z - normal.z
				* origin.z)
				/ (normal.x * vector.x + normal.y * vector.y + normal.z
						* vector.z);
	}

	public double calculateT(Vector vector, Point point, double minT) {
		return calculateT(vector, point);
	}

	public boolean sameSide(Point point1, Point point2) {
		double t = calculateT(new Vector(point2.subtract(point1)), point1);
		return t >= 1 || t <= 0;
	}

	@Override
	public Point intersection(Vector vector, Point point) {
		double t = calculateT(vector, point);
		return intersection(vector, point, t);
	}

	@Override
	public Point intersection(Vector vector, Point point, double t) {
		double nx = point.x + vector.x * t;
		double ny = point.y + vector.y * t;
		double nz = point.z + vector.z * t;
		return new Point(nx, ny, nz);
	}

	/**
	 * Call this with three values, the two you wish to plug in and a NaN that
	 * you wish to solve for. remember, if you give two values that are not part
	 * of the plane, you will get NaN back
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public Point evaluate(double x, double y, double z) {
		// x=(-(yn(y-y0)+zn(z-z0))+xn*x0)/xn
		if (x != x) {
			x = (-(normal.y * (y - origin.y) + normal.z * (z - origin.z)) + normal.x
					* origin.x)
					/ normal.x;
			return new Point(x, y, z);
		} else if (y != y) {
			y = (-(normal.x * (x - origin.x) + normal.z * (z - origin.z)) + normal.y
					* origin.y)
					/ normal.y;
			return new Point(x, y, z);
		} else if (z != z) {
			z = (-(normal.y * (y - origin.y) + normal.x * (x - origin.x)) + normal.z
					* origin.z)
					/ normal.z;
			return new Point(x, y, z);
		}
		return null;
	}

	@Override
	public String toString() {
		return "Plane: " + origin + ";" + normal;
	}

	@Override
	public void translate(Vector v) {
		origin = new Point(origin.x + v.x, origin.y + v.y, origin.z + v.z);
	}

	/**
	 * Sets the color of this plane
	 * 
	 * @param c
	 */
	public void setC(Color c) {
		this.c = c;
	}

	public Color c() {
		return c;
	}
}
