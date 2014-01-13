package life.threedee;

import java.awt.Color;

public interface ThreeDeeObject {

	/**
	 * Calculates the scalar value the vector v must be multiplied by for the
	 * vector to span from the point to called on given object
	 * 
	 * @param v
	 * @param p
	 * @return
	 */
	public double calculateT(Vector v, Point p);

	/**
	 * Returns the t value (see {@code calculateT}), color, and {@code this} for
	 * the given direction, point, and minimum t value. The minT is passed so as
	 * to avoid calculating color if it will not be used anyways.
	 * 
	 * @param v
	 * @param p
	 * @param minT
	 * @return
	 */
	public TColorTransfer getRData(Vector v, Point p, double minT);

	/**
	 * Calculate the intersection point of the line represented by the point and
	 * the vector and {@code this}
	 * 
	 * @param v
	 * @param p
	 * @return
	 */
	public Point intersection(Vector v, Point p);

	/**
	 * Calculates the intersection point given the t value. Note: all this does
	 * is multiply the vector by t and add it to the point
	 * 
	 * @param v
	 * @param p
	 * @param t
	 * @return
	 */
	public Point intersection(Vector v, Point p, double t);

	/**
	 * Translates {@code this} by the given {@code Vector}
	 * 
	 * @param v
	 */
	public void translate(Vector v);

	/**
	 * Returns the color of the object
	 * 
	 * @return
	 */
	public Color c();

	/**
	 * Returns whether or not the two points are on the same side of the object
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean sameSide(Point a, Point b);
}
