package life.threedee;

import java.awt.Color;

/**
 * A Triangle represented by 3 points
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 * 
 */
public class Triangle extends Plane {

	// The three points representing this triangle
	private Point a, b, c;

	// used for barymetric inside calculations
	private Vector u, v;

	private double uu, uv, vv, D;

	/**
	 * Constructs a new triangle from the given points and with a random colour
	 * 
	 * @param a
	 * @param b
	 * @param c
	 */
	public Triangle(Point a, Point b, Point c) {
		this(a, b, c, new Color((int) (Math.random() * 256),
				(int) (Math.random() * 256), (int) (Math.random() * 256)));
	}

	/**
	 * Constructs a new triangle from the given points with the given colour
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param colour
	 */
	public Triangle(Point a, Point b, Point c, Color colour) {
		super(a, new Vector(a, b).crossProduct(new Vector(a, c)), colour);
		this.a = a;
		this.b = b;
		this.c = c;
		calcBary();
	}

	// calculates the reused values for the barymetric calculations
	private void calcBary() {
		u = new Vector(a, b);
		v = new Vector(a, c);
		uu = u.dotProduct(u);
		uv = u.dotProduct(v);
		vv = v.dotProduct(v);
		D = uv * uv - uu * vv;
	}

	@Override
	public double calculateT(Vector vector, Point point, double minT) {
		double t = super.calculateT(vector, point);
		Point inter = super.intersection(vector, point, t);
		// min.t > o.t && o.t >= 0 && o.t == o.t && o.c.getAlpha() != 0
		boolean check = (minT == minT) ? (t == t && minT > t && t >= 0) : true;
		if (check && inside(inter)) {
			return t;
		} else {
			return Double.NaN;
		}
	}

	@Override
	public double calculateT(Vector vector, Point point) {
		double t = super.calculateT(vector, point);
		Point inter = super.intersection(vector, point, t);
		if (inside(inter)) {
			return t;
		} else {
			return Double.NaN;
		}
	}

	/**
	 * Calculates whether the given point is inside the triangle by converting
	 * to barymetric coordinates
	 * 
	 * @param point
	 * @return
	 */
	public boolean inside(Point point) {
		Vector w = new Vector(a, point);

		double wv = w.dotProduct(v), wu = w.dotProduct(u);

		double s = (uv * wv - vv * wu) / D;
		if (s < 0 || s > 1) {
			return false;
		}
		double t = (uv * wu - uu * wv) / D;
		if (t < 0 || t + s > 1) {
			return false;
		}

		return true;
	}

	/**
	 * Translates the a point of this triangle by the given vector
	 * 
	 * @param v
	 */
	public void translateA(Vector v) {
		a = new Point(a.x + v.x, a.y + v.y, a.z + v.z);
		calcBary();
	}

	/**
	 * Translates the b point of this triangle by the given vector
	 * 
	 * @param v
	 */
	public void translateB(Vector v) {
		b = new Point(b.x + v.x, b.y + v.y, b.z + v.z);
		calcBary();
	}

	/**
	 * Translates the c point of this triangle by the given vector
	 * 
	 * @param v
	 */
	public void translateC(Vector v) {
		c = new Point(c.x + v.x, c.y + v.y, c.z + v.z);
		calcBary();
	}

	@Override
	public void translate(Vector v) {
		super.translate(v);
		translateA(v);
		translateB(v);
		translateC(v);
		calcBary();
	}

	/**
	 * Stretches this triangle by the given value
	 * 
	 * @param origin
	 * @param elasticity
	 */
	public void stretch(Point origin, double elasticity) {
		a = a.subtract(origin).stretch(elasticity).add(origin);
		b = b.subtract(origin).stretch(elasticity).add(origin);
		c = c.subtract(origin).stretch(elasticity).add(origin);
		calcBary();
	}

	@Override
	public boolean sameSide(Point point1, Point point2) {
		return super.sameSide(point1, point2)
				|| !this.inside(this.intersection(new Vector(point1, point2),
						point1));
	}
}
