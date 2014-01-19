package life.threedee.game.maps;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.idCount;

import java.awt.Color;

import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.Vector;
import life.threedee.game.GameUtilities;

/**
 * A plane in the map
 * @author Sean
 *
 */
public class MapPlane extends Plane implements MapFeature {
	
	/**
	 * The id value for this map feature
	 */
	public final int id;
	
	// the width of the edge
	private static final double EDGE = GameUtilities.PX_WALL_BORDER / (double) GameUtilities.PX_METER;
	
	// up and right for this plane
	private final Vector up, right;
	
	// the width and height of this plane
	private final double width, height;
	
	/**
	 * Constructs a new MapPlane with the given parameters
	 * @param p
	 * @param n
	 * @param width
	 * @param height
	 */
	public MapPlane(Point p, Vector n, double width, double height) {
		super(p, n);
		id = idCount;
		idCount++;
		
		this.width = width;
		this.height = height;
		
		double[] dirPolar = this.normal.polarTransform();

		if(dirPolar[0] == dirPolar[0] && dirPolar[1] == dirPolar[1]){
			up = Vector.fromPolarTransform(dirPolar[0], PI/2 + dirPolar[1], 1);
			right = Vector.fromPolarTransform(dirPolar[0] - PI/2, 0, 1);
		} else {
			up = new Vector(0, 0, 1);
			right = new Vector(1, 0, 0);
		}
	}

	@Override
	public int getID() {
		return id;
	}
	
	/**
	 * Calculates the colour at a given point, returning null if it is outside the plane
	 * @param inter
	 * @return
	 */
	public Color c(Point inter) {
		Vector p = new Vector(this.origin, inter);
		double du = p.dotProduct(up);
		double dr = p.dotProduct(right);
		
		// do the inside check before the edge check so the common case is faster
		if(du >= 0 && du < height && dr >= 0 && dr < width) {
			if(du < EDGE || du > height - EDGE || dr < EDGE || dr > width - EDGE) {
				return GameUtilities.W_BLUE;
			} else {
				return Color.BLACK;
			}
		} else {
			return null;
		}
	}
	
	@Override
	public TColorTransfer getRData(Vector v, Point p, double minT) {
		double t = this.calculateT(v, p);
		if(t == t && !(t > minT)) {
			return new TColorTransfer(t, c(this.intersection(v, p, t)), this);
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
