package life.threedee.game;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.ThreeDeeObject;
import life.threedee.Triangle;
import life.threedee.Vector;

public class Pellet implements ThreeDeeObject, Tickable{

	private static final double A_INC = Math.PI / 60;
	private static final double C_THIRD = 2*Math.PI/3;
	
	private Triangle[] t;
	private Point[] p;
	private Point top;
	
	private Point center;
	
	private double yaw;
	
	private boolean eaten;
	
	public Pellet(Point center) {
//		(0,0.75,0);"
//                + "(0,0.875,0),(0,0.625,0.2),(0.14,0.625,-0.1);"
//                + "(0,0.875,0),(0,0.625,0.2),(-0.14,0.625,-0.1);"
//                + "(0,0.875,0),(0.14,0.625,-0.1),(-0.14,0.625,-0.1)"
		this.center = center;
		top = new Point(0, 0.875, 0);
		t = new Triangle[3];
		p = new Point[3];
		generate();
	}
	
	private void generate() {
		double cyaw = yaw;
		for(int i = 0; i < 3; i++) {
			p[i] = new Point(Vector.fromPolarTransform(cyaw, 0, 0.2));
			cyaw += C_THIRD;
		}
		for(int i = 0;i < 3; i++) {
			t[i] = new Triangle(top, p[i], p[(i+1)%3]);
		}
		translate(new Vector(center.x, 0.625, center.y));
	}

	@Override
	public void tick() {
		yaw += A_INC;
		generate();
	}
	
	public boolean eaten() {
		return eaten;
	}
	
	@Override
	public double calculateT(Vector v, Point p) {
		return Math.min(Math.min(t[0].calculateT(v, p), t[1].calculateT(v, p)), t[0].calculateT(v, p));
	}

	@Override
	public TColorTransfer getRData(Vector v, Point p, double minT) {
		double min = Double.MAX_VALUE;
		for(Triangle t : this.t) {
			double ct = t.calculateT(v, p, Math.min(minT, min));
			if(ct == ct && ct < min) {
				min = ct;
			}
		}
		if(!(min > minT)) {
			return new TColorTransfer(min, c(), this);
		} else {
			return new TColorTransfer(Double.NaN, null, null);
		}
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
	public Color c() {
		return Color.YELLOW;
	}

	@Override
	public boolean sameSide(Point m, Point n) {
		for(Triangle t : this.t) {
			if(!t.sameSide(m, n)) {
				return false;
			}
		}
		return true;
	}

}
