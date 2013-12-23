package life.threedee;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.BLANK;
import static life.threedee.game.GameUtilities.PX_METER;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class TexturedPlane extends Plane{
	private BufferedImage texture;
	
	private Vector right, up;

	/**
	 * Note: p is the bottom right corner of the texture, so keep that in mind.
	 * @param p
	 * @param n
	 * @param texture
	 */
	public TexturedPlane(Point p, Vector n, BufferedImage texture){
		super(p, n);
		this.texture = texture;
		
		double[] dirPolar = this.normal.polarTransform();
		
		up = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
		right = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
	}
	
	public Color c(Point inter) {
		Vector p = new Vector(this.origin, inter);
		double du = p.dotProduct(up);
		double dr = p.dotProduct(right);
		int px = (int) (dr * PX_METER);
		int py = texture.getHeight() - (int) (du * PX_METER);
		try{
			return new Color(texture.getRGB(px, py));
		}
		catch(ArrayIndexOutOfBoundsException e) {
			return BLANK;
		}
		
	}
	
	@Override
	public TColorTransfer getRData(Vector vector, Point point, double minT) {
		double t = calculateT(vector, point, minT);
		if(t == t && t > 0 && (minT != minT || t < minT)) {
			Color c = this.c(this.intersection(vector, point, t));
			return new TColorTransfer(t, c);
		} else {
			return new TColorTransfer(t, BLANK);
		}
	}
}
