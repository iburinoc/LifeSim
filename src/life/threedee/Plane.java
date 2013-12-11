package life.threedee;

import java.awt.Color;

public class Plane{

	/*
	// z = ax + by + colour

	double a; // x coefficient
	double b; // y coefficient
	double colour; // constant
	*/
	
	private Point3D origin; // Origin point on plane
	private Vector normal; // Normal

	public Color colour;
	
	public Plane(Point3D a, Point3D b, Point3D colour){
		origin = a;
		normal = new Vector(a, b).crossProduct(new Vector(a, colour));
		
		this.colour = new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256));
	}

	public Plane(Point3D origin, Vector normal){
		this(origin,normal,new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256)));
	}
	
	public Plane(Point3D origin, Vector normal, Color colour){
		this.origin = origin;
		this.normal = normal;
		
		this.colour = colour;
	}

	/*
	 * double d = a.x; double e = a.y; double f = a.z;
	 * 
	 * double g = b.x; double h = b.y; double i = b.z;
	 * 
	 * double j = colour.x; double k = colour.y; double l = colour.z;
	 * 
	 * this.a = (l * h - l * e - k * i + k * f - f * h + f * e + e * i - e * f)
	 * / (j * h - j * e - k * g + k * d - d * h + d * e + e * g - e * d); this.b
	 * = (i - this.a * (g - d) - f) / (h - e); this.colour = f - this.a * d - this.b
	 * * e; Here for reference. no longer needed.
	 */
	/*
	private boolean collinear(Point3D a, Point3D b, Point3D colour){
		boolean condition1 = false, condition2 = false;
		try{
			double dxy1 = (a.x - b.x) / (a.y - b.y);
			double dxy2 = (b.x - colour.x) / (b.y - colour.y);
			condition1 = Math.abs(dxy1 - dxy2) < 0.0000001;
		}
		catch (ArithmeticException e){
			condition1 = a.y == b.y && b.y == colour.y;
		}
		try{
			double dxz1 = (a.x - b.x) / (a.z - b.z);
			double dxz2 = (b.x - colour.x) / (b.z - b.z);
			condition2 = Math.abs(dxz1 - dxz2) < 0.0000001;
		}
		catch (ArithmeticException e){
			condition2 = a.z == b.z && b.z == colour.z;
		}
		return condition1 && condition2;
	}
	*/

	public double calculateT(Vector vector, Point3D point3D){
		return -(normal.x * point3D.x - normal.x * origin.x + normal.y * point3D.y - normal.y * origin.y + normal.z * point3D.z - normal.z * origin.z)
				/ (normal.x * vector.x + normal.y * vector.y + normal.z * vector.z);
	}

	public Point3D intersection(Vector vector, Point3D point3D){
		double t = calculateT(vector, point3D);
		double nx = point3D.x + vector.x * t;
		double ny = point3D.y + vector.y * t;
		double nz = point3D.z + vector.z * t;
		return new Point3D(nx, ny, nz);
	}

	/**
	 * Call this with three values, the two you wish to plug in and a NaN that
	 * you wish to solve for. remember, if you give two values that are not part
	 * of the plane, you will get NaN back
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public Point3D evaluate(double x, double y, double z) {
		//x=(-(yn(y-y0)+zn(z-z0))+xn*x0)/xn
		if(x != x) {
			x = (-(normal.y * (y - origin.y) + normal.z * (z - origin.z)) + normal.x * origin.x) / normal.x;
			return new Point3D(x,y,z);
		} else if(y != y) {
			y = (-(normal.x * (x - origin.x) + normal.z * (z - origin.z)) + normal.y * origin.y) / normal.y;
			return new Point3D(x,y,z);
		} else if(z != z) {
			z = (-(normal.y * (y - origin.y) + normal.x * (x - origin.x)) + normal.z * origin.z) / normal.z;
			return new Point3D(x,y,z);
		}
		return null;
	}
	
	public String toString(){
		return "Plane: " + origin + ";" + normal;
	}
}
