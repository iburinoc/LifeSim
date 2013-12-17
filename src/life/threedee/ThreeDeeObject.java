package life.threedee;

import java.awt.Color;

public interface ThreeDeeObject{
	public double calculateT(Vector v, Point p);
	
	public TColorTransfer getRData(Vector v, Point p);
	
	public Point intersection(Vector v, Point p);
	
	public Point intersection(Vector v, Point p, double t);
	
	public void translate(Vector v);
	
	public Color c();
	
	public boolean sameSide(Point a, Point b);
}
