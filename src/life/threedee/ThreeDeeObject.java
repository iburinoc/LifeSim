package life.threedee;

import java.awt.Color;

public interface ThreeDeeObject{
	public double calculateT(Vector v, Point3D p);
	
	public Point3D intersection(Vector v, Point3D p);
	
	public Point3D intersection(Vector v, Point3D p, double t);
	
	public void translate(Vector v);
	
	public Color c();
}
