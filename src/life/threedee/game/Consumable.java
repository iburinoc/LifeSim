package life.threedee.game;

import static life.threedee.game.GameUtilities.idCount;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.ThreeDeeObject;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.game.maps.MapFeature;

public abstract class Consumable implements ThreeDeeObject, Tickable, MapFeature{

	protected Triangle[] t;
	protected Point[] p;
	
	protected Point top;
	
	protected Point center;
	
	protected double yaw;
	
	protected boolean eaten;
	
	private final int id;
	
	private final double a_inc;
	
	public Consumable(final Point center, final int t, final int p, final double a_inc) {
		this.center = center;
		this.t = new Triangle[t];
		this.p = new Point[p];
		generate();
		id = idCount++;
		this.a_inc = a_inc;
	}
	
	protected abstract void generate();

	@Override
	public void tick() {
		yaw += a_inc;
		generate();
	}

    public abstract void eat(Game g, Player p);
    
    public void spawn() {
        eaten = false;
    }
	
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

    public Point getCenter(){
        return center;
    }
}
