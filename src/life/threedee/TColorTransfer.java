package life.threedee;

import java.awt.Color;

/**
 * Contains the data for the rendering of a given ThreeDeeObject, including the
 * T value (see calculateT of ThreeDeeObject), the color at that given point of
 * intersection, and the ThreeDeeObject from which this TColorTransfer
 * originates.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 */
public final class TColorTransfer {
	
	/**
	 * The t value for this object
	 */
	public final double t;
	
	/**
	 * The color that should be rendered if this is the closest one
	 */
	public final Color c;
	
	/**
	 * The ThreeDeeObject of origin
	 */
	public final ThreeDeeObject o;

	/**
	 * Creates a new TColorTransfer from the values given
	 * @param t
	 * @param c
	 * @param o
	 */
	public TColorTransfer(double t, Color c, ThreeDeeObject o) {
		super();
		this.t = t;
		this.c = c;
		this.o = o;
	}
}
