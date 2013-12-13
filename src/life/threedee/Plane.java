package life.threedee;

import java.awt.Color;

public class Plane implements ThreeDeeObject{

	/*
	// z = ax + by + c

	double a; // x coefficient
	double b; // y coefficient
	double c; // constant
	*/
	
	private Point origin; // Origin point on plane
	private Vector normal; // Normal

	public Color c;
	
	public Plane(Point a, Point b, Point c){
		this(a,b,c,new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256)));
	}
	
	public Plane(Point a, Point b, Point c, Color colour){
		origin = a;
		normal = new Vector(a, b).crossProduct(new Vector(a, c));
		
		this.c = colour;
	}

	public Plane(Point origin, Vector normal){
		this(origin,normal,new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256)));
	}
	
	public Plane(Point origin, Vector normal, Color c){
		this.origin = origin;
		this.normal = normal;
		
		this.c = c;
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

	@Override
	public double calculateT(Vector vector, Point point){
		return -(normal.x * point.x - normal.x * origin.x + normal.y * point.y - normal.y * origin.y + normal.z * point.z - normal.z * origin.z)
				/ (normal.x * vector.x + normal.y * vector.y + normal.z * vector.z);
	}

    public boolean sameSide(Point point1, Point point2) {
        double solutionX = evaluate(Double.NaN, point1.y, point1.z).x;
        double solutionY = evaluate(point1.x, Double.NaN, point1.z).y;
        double solutionZ = evaluate(point1.x, point1.y, Double.NaN).z;
        return (point1.x > solutionX == point2.x > evaluate(Double.NaN, point2.y, point2.z).x || solutionX != solutionX) && (point1.y > solutionY == point2.y > evaluate(point2.x, Double.NaN, point2.z).y || solutionY != solutionY) && (point1.z > solutionZ == point2.z > evaluate(point2.x, point2.y, Double.NaN).z || solutionZ != solutionZ);
    }

    @Override
	public Point intersection(Vector vector, Point point){
		double t = calculateT(vector, point);
		return intersection(vector, point, t);
	}
	
	@Override
	public Point intersection(Vector vector, Point point, double t){
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
	public Point evaluate(double x, double y, double z) {
		//x=(-(yn(y-y0)+zn(z-z0))+xn*x0)/xn
		if(x != x) {
			x = (-(normal.y * (y - origin.y) + normal.z * (z - origin.z)) + normal.x * origin.x) / normal.x;
			return new Point(x,y,z);
		} else if(y != y) {
			y = (-(normal.x * (x - origin.x) + normal.z * (z - origin.z)) + normal.y * origin.y) / normal.y;
			return new Point(x,y,z);
		} else if(z != z) {
			z = (-(normal.y * (y - origin.y) + normal.x * (x - origin.x)) + normal.z * origin.z) / normal.z;
			return new Point(x,y,z);
		}
		return null;
	}
	
	@Override
	public String toString(){
		return "Plane: " + origin + ";" + normal;
	}
	
	@Override
	public void translate(Vector v){
		origin = new Point(origin.x + v.x, origin.y + v.y, origin.z + v.z);
	}
	
	public Color c(){
		return c;
	}
}
