package life.threedee;

public class Plane{
	
	// z = ax + by + c
	
	double a; // x coeff
	double b; // y coeff
	double c; // constant
	
	Point o; //Origin point on plane
	Vector n; //Normal
	
	public Plane(Point a,Point b,Point c){
		if(collinear(a,b,c)){
			throw new IllegalArgumentException("Points collinear");
		}
		createPlane(a,b,c);
	}
	
	public Plane(Point o,Vector n){
		this.o = o;
		this.n = n;
	}
	
	private void createPlane(Point a,Point b,Point c){
		double d = a.x;
		double e = a.y;
		double f = a.z;
		
		double g = b.x;
		double h = b.y;
		double i = b.z;
		
		double j = c.x;
		double k = c.y;
		double l = c.z;
		
		this.a = (l*h - l*e - k*i + k*f - f*h + f*e + e*i - e*f)/(j*h - j*e - k*g + k*d - d*h + d*e + e*g - e*d);
		this.b = (i - this.a * (g-d) - f)/(h - e);
		this.c = f - this.a * d - this.b * e;
		
		o = a;
		n = new Vector(a,b).crossProduct(new Vector(a,c));
	}
	
	private boolean collinear(Point a,Point b,Point c){
		boolean cond1 = false,cond2 = false;
		try{
			double dxy1 = (a.x-b.x)/(a.y-b.y);
			double dxy2 = (b.x-c.x)/(b.y-c.y);
			cond1 = Math.abs(dxy1 - dxy2) < 0.0000001;
		}
		catch(ArithmeticException e){
			cond1 = a.y == b.y && b.y == c.y; 
		}
		
		try{
			double dxz1 = (a.x-b.x)/(a.z-b.z);
			double dxz2 = (b.x-c.x)/(b.z-b.z);
			cond2 = Math.abs(dxz1 - dxz2) < 0.0000001;
		}
		catch(ArithmeticException e){
			cond2 = a.z == b.z && b.z == c.z;
		}
		return cond1 && cond2;
	}
	
	public double calculateT(Vector v,Point p){
		return (n.x * p.x - n.x * o.x + 
				n.y * p.y - n.y * o.y + 
				n.z * p.z - n.z * o.z) / 
				(n.x * v.x + n.y * v.y + n.z * v.z);
	}
	
	public Point intersection(Vector v, Point p){	
		double t = calculateT(v,p);
		double nx = p.x + v.x * t;
		double ny = p.y + v.y * t;
		double nz = p.z + v.z * t;
		return new Point(nx,ny,nz);
	}
	
	private double eval(double x,double z){
		return a * x + b * z + c;
	}
}
