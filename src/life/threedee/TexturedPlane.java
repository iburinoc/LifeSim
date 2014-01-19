package life.threedee;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.PX_METER;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A plane that has a texture on it, does not extend past it's texture.  The origin point given is the bottom left corner of the image
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class TexturedPlane extends Plane{
	
	/**
	 * The texture for this textured plane
	 */
    public BufferedImage texture;

    /**
     * Represents right with regards to this plane, must be recalculated if the plane is rotated
     */
    public Vector right;
    
    /**
     * Represents up with regards to this plane, must be recalculated if the plane is rotated
     */
    public Vector up;

    /**
     * The width of the image in pixels
     */
	public int w;
	
	/**
	 * The height of the image in pixels
	 */
	public int h;
	
	/**
	 * Note: p is the bottom left corner of the texture, so keep that in mind.
	 * @param p
	 * @param n
	 * @param texture
	 */
	public TexturedPlane(Point p, Vector n, BufferedImage texture){
		super(p, n);
		this.texture = texture;
		
		double[] dirPolar = this.normal.polarTransform();

		if(dirPolar[0] == dirPolar[0] && dirPolar[1] == dirPolar[1]){
			up = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
			right = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
		} else {
			up = new Vector(0, 0, 1);
			right = new Vector(1, 0, 0);
		}
		
		this.w = texture.getWidth();
		this.h = texture.getHeight();
	}
	
	/**
	 * Calculates the pixel at a given point of intersection, null if it is outside the image
	 * @param inter
	 * @return
	 */
	public Color c(Point inter) {
		Vector p = new Vector(this.origin, inter);
		double du = p.dotProduct(up);
		double dr = p.dotProduct(right);
		int px = (int) (dr * PX_METER);
		int py = texture.getHeight() - (int) (du * PX_METER);
		if(px >= 0 && px < w && py >= 0 && py < h) {
			try{
				return new Color(texture.getRGB(px, py), true);
			}
			catch(ArrayIndexOutOfBoundsException e) {
			}
		}
		return null;
	}
	
	@Override
	public TColorTransfer getRData(Vector vector, Point point, double minT) {
		double t = calculateT(vector, point, minT);
		if(t == t && t > 0 && !(t > minT)){
			Color c = this.c(this.intersection(vector, point, t));
			return new TColorTransfer(t, c, this);
		} else {
			return new TColorTransfer(Double.NaN, null, this);
		}
	}

    @Override
    public boolean sameSide(Point point1, Point point2){
        Color color = getRData(new Vector(point1, point2), point1, Double.NaN).c;
        return color == null || color.getAlpha() == 0 || super.sameSide(point1, point2);
    }
}
