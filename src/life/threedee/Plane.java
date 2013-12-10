package life.threedee;

public class Plane {
	
	// z = ax + by + c
	
	double a; // x coeff
	double b; // y coeff
	double c; // constant
	
	Point origin; //Origin point on plane
	Vector normal; //Normal
	
	public Plane (Point a, Point b, Point c) {
		if(collinear(a, b, c)) {
			throw new IllegalArgumentException("Points collinear");
		}
		origin = a;
		normal = new Vector(a, b).crossProduct(new Vector(a, c));
	}
	
	public Plane (Point origin, Vector normal) {
		this.origin = origin;
		this.normal = normal;
	}
	
	/*
	 * double d = a.x;
		double e = a.y;
		double f = a.z;
		
		double g = b.x;
		double h = b.y;
		double i = b.z;
		
		double j = c.x;
		double k = c.y;
		double l = c.z;
		
		this.a = (l * h - l * e - k * i + k * f - f * h + f * e + e * i - e * f) / (j * h - j * e - k * g + k * d - d * h + d * e + e * g - e * d);
		this.b = (i - this.a * (g - d) - f) / (h - e);
		this.c = f - this.a * d - this.b * e;
		Here for reference.  no longer needed.
	 */
	
	private boolean collinear (Point a,Point b,Point c) {
		boolean condition1 = false, condition2 = false;
		try {
			double dxy1 = (a.x - b.x) / (a.y - b.y);
			double dxy2 = (b.x - c.x) / (b.y - c.y);
			condition1 = Math.abs(dxy1 - dxy2) < 0.0000001;
		} catch(ArithmeticException e) {
			condition1 = a.y == b.y && b.y == c.y;
		}
		try {
			double dxz1 = (a.x - b.x) / (a.z - b.z);
			double dxz2 = (b.x - c.x) / (b.z - b.z);
			condition2 = Math.abs(dxz1 - dxz2) < 0.0000001;
		} catch(ArithmeticException e) {
			condition2 = a.z == b.z && b.z == c.z;
		}
		return condition1 && condition2;
	}
	
	public double calculateT (Vector vector, Point point){
		return -(normal.x * point.x - normal.x * origin.x +
				normal.y * point.y - normal.y * origin.y +
				normal.z * point.z - normal.z * origin.z) /
				(normal.x * vector.x + normal.y * vector.y + normal.z * vector.z);
	}
	
	public Point intersection (Vector vector, Point point){
		double t = calculateT(vector, point);
		double nx = point.x + vector.x * t;
		double ny = point.y + vector.y * t;
		double nz = point.z + vector.z * t;
		return new Point(nx, ny, nz);
	}
	
	private double evaluate (double x, double z){
		return a * x + b * z + c;
	}
}
