package life.threedee;

public class Plane{

	/*
	// z = ax + by + c

	double a; // x coeff
	double b; // y coeff
	double c; // constant
	*/
	
	Point origin; // Origin point on plane
	Vector normal; // Normal

	public Plane(Point a, Point b, Point c){
		origin = a;
		normal = new Vector(a, b).crossProduct(new Vector(a, c));
	}

	public Plane(Point origin, Vector normal){
		this.origin = origin;
		this.normal = normal;
	}

	/*
	 * double d = a.x; double e = a.y; double f = a.z;
	 * 
	 * double g = b.x; double h = b.y; double i = b.z;
	 * 
	 * double j = c.x; double k = c.y; double l = c.z;
	 * 
	 * this.a = (l * h - l * e - k * i + k * f - f * h + f * e + e * i - e * f)
	 * / (j * h - j * e - k * g + k * d - d * h + d * e + e * g - e * d); this.b
	 * = (i - this.a * (g - d) - f) / (h - e); this.c = f - this.a * d - this.b
	 * * e; Here for reference. no longer needed.
	 */
	/*
	private boolean collinear(Point a, Point b, Point c){
		boolean condition1 = false, condition2 = false;
		try{
			double dxy1 = (a.x - b.x) / (a.y - b.y);
			double dxy2 = (b.x - c.x) / (b.y - c.y);
			condition1 = Math.abs(dxy1 - dxy2) < 0.0000001;
		}
		catch (ArithmeticException e){
			condition1 = a.y == b.y && b.y == c.y;
		}
		try{
			double dxz1 = (a.x - b.x) / (a.z - b.z);
			double dxz2 = (b.x - c.x) / (b.z - b.z);
			condition2 = Math.abs(dxz1 - dxz2) < 0.0000001;
		}
		catch (ArithmeticException e){
			condition2 = a.z == b.z && b.z == c.z;
		}
		return condition1 && condition2;
	}
	*/

	public double calculateT(Vector vector, Point point){
		return -(normal.x * point.x - normal.x * origin.x + normal.y * point.y - normal.y * origin.y + normal.z * point.z - normal.z * origin.z)
				/ (normal.x * vector.x + normal.y * vector.y + normal.z * vector.z);
	}

	public Point intersection(Vector vector, Point point){
		double t = calculateT(vector, point);
		double nx = point.x + vector.x * t;
		double ny = point.y + vector.y * t;
		double nz = point.z + vector.z * t;
		return new Point(nx, ny, nz);
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
	public Point evaluate(double x, double y,double z) {
		//x=(-(yn(y-y0)+zn(z-z0))+xn*x0)/xn
		if(x != x){
			x = (-(normal.y * (y - origin.y) + normal.z * (z - origin.z)) + normal.x * origin.x) / normal.x;
			return new Point(x,y,z);
		}
		if(y != y){
			y = (-(normal.x * (x - origin.x) + normal.z * (z - origin.z)) + normal.y * origin.y) / normal.y;
			return new Point(x,y,z);
		}
		if(z != z){
			z = (-(normal.y * (y - origin.y) + normal.x * (x - origin.x)) + normal.z * origin.z) / normal.z;
			return new Point(x,y,z);
		}
		return null;
	}
}
