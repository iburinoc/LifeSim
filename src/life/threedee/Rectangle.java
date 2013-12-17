package life.threedee;

import java.awt.Color;

public class Rectangle implements ThreeDeeObject {

	private Triangle a, b;
	
	public Rectangle(Triangle a, Triangle b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public TColorTransfer getRData(Vector vector, Point point) {
		TColorTransfer at = a.getRData(vector, point);
		TColorTransfer bt = b.getRData(vector, point);
		if(at.t <= bt.t) {
			return at;
		} else {
			return bt;
		}
	}
	
	@Override
	public double calculateT(Vector v, Point p) {
		return Math.min(a.calculateT(v, p), b.calculateT(v, p));
	}

	@Override
	public Point intersection(Vector v, Point p) {
		return a.intersection(v, p);
	}

	@Override
	public Point intersection(Vector v, Point p, double t) {
		return a.intersection(v, p, t);
	}

	@Override
	public void translate(Vector v) {
		a.translate(v);
		b.translate(v);
	}

	@Override
	public Color c() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sameSide(Point a, Point b) {
		return this.a.sameSide(a, b);
	}

}
