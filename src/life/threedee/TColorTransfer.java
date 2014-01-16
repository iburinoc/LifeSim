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
public class TColorTransfer {
	
	/**
	 * T
	 */
	public final double t;
	public final Color c;
	public final ThreeDeeObject o;

	public TColorTransfer(double t, Color c, ThreeDeeObject o) {
		super();
		this.t = t;
		this.c = c;
		this.o = o;
	}
}
