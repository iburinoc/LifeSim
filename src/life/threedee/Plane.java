package life.threedee;

public class Plane{
	
	// 0 = ax + by + cz + d
	
	double a; // x coeff
	double b; // y coeff
	double c; // z coeff
	double d; // constant
	
	public Plane(Point a,Point b,Point c){
		if(collinear(a,b,c)){
			throw new IllegalArgumentException("Points collinear");
		}
		createPlane(a,b,c);
	}
	
	private void createPlane(Point a,Point b,Point c){
		
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
	
	private double eval(double x,double z){
		return a * x + b * z + c;
	}
}
