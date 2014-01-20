package life.threedee.game;

import static life.threedee.game.GameUtilities.idCount;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.ThreeDeeObject;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.game.maps.MapFeature;

/**
 * A consumable in the game. A consumable is anything that is stationary, 
 * can be consumed during the play of the game, 
 * and is usually constructed out of several, similarly generated triangles.
 * Also, they usually rotate.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public abstract class Consumable implements ThreeDeeObject, Tickable, MapFeature{

    /**
     * The triangles which make up this consumable. 
     */
	protected Triangle[] t;
	
	/**
	 * The points which can be constructed in a generic manner. 
	 * Point which are different from most of the points are not in this array.
	 */
	protected Point[] p;
	
	/**
	 * The center and location of this consumable.
	 */
	protected Point center;
	
	/**
	 * The current yaw of the forwards vector of this consumable. This is used for rotation.
	 */
	protected double yaw;
	
	/**
	 * Whether this consumable has been eaten, and must therefore be displayed.
	 */
	protected boolean eaten;
	
	/**
	 * This consumable's id for deterministic reconstruction.
	 */
	private final int id;
	
	/**
	 * The angle incrementer, which is the amount that this consumable rotates by each time.
	 */
	private final double a_inc;
	
	/**
	 * Construct a consumable with a given center, angle incrementer, 
	 * and a given amount of standard triangles and points.
	 * @param center The center of this consumable.
	 * @param t The amount of standard triangles in this consumable.
	 * @param p The amount of standard points in this consumable.
	 * @param a_inc The angle incrementer.
	 */
	public Consumable(final Point center, final int t, final int p, final double a_inc) {
		this.center = center;
		this.t = new Triangle[t];
		this.p = new Point[p];
		generate();
		id = idCount++;
		this.a_inc = a_inc;
	}
	
	/**
	 * Generates all relevant triangles and points for this consumable in the correct location and orientation.
	 */
	protected abstract void generate();

	@Override
	public void tick() {
		yaw += a_inc;
		generate();
	}

	/**
	 * Called when this consumable is to be eaten.
	 * @param g The {@link Game} objects that called this method.
	 * @param p The {@link Player} that ate this consumable.
	 */
    public abstract void eat(Game g, Player p);
    
    /**
     * Spawns this consumable, making it eatable and visible.
     */
    public void spawn() {
        eaten = false;
    }
	
    /**
     * Getter for being eaten.
     * @return Whether this consumable has been eaten or not.
     */
	public boolean getEaten() {
		return eaten;
	}
	
	@Override
	public int getID() {
		return id;
	}
	
	@Override
	public double calculateT(Vector v, Point p) {
		double min = Double.NaN;
		for(Triangle tri : t) { 
			double t = tri.calculateT(v, p);
			if(!(t > min)) {
				min = t;
			}
		}
		return min;
	}

	@Override
	public TColorTransfer getRData(Vector v, Point p, double minT) {
		if(eaten) {
			return new TColorTransfer(Double.NaN, null, null);
		}
		for(Triangle t : this.t) {
			double ct = t.calculateT(v, p, minT);
			if(ct == ct && !(ct > minT)) {
				return new TColorTransfer(ct, c(), this);
			}
		}
		return new TColorTransfer(Double.NaN, null, null);
	}

	@Override
	public Point intersection(Vector v, Point p) {
		return null;
	}

	@Override
	public Point intersection(Vector v, Point p, double t) {
		return null;
	}

	@Override
	public void translate(Vector v) {
		for(Triangle t : this.t) {
			t.translate(v);
		}
	}

	@Override
	public boolean sameSide(Point m, Point n) {
		return true; //non-solid
	}

	/**
	 * Getter for the center.
	 * @return The center of this consumable.
	 */
    public Point getCenter(){
        return center;
    }
}
