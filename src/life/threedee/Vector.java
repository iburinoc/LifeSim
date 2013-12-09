package life.threedee;

public class Vector{
	public final double x,y,z;
	public final double s;
	
	public Vector(double x,double y,double z){
		this(x,y,z,1);
	}
	
	public Vector(double x,double y,double z,double s){
		double mag = Math.sqrt(x * x + y * y + z * z);
		
		this.x = x / mag;
		this.y = y / mag;
		this.z = z / mag;
		this.s = s;
	}
	
	public Vector(Point p0, Point p1){
		double x = (p1.x - p0.x);
		double y = (p1.y - p0.y);
		double z = (p1.z - p0.z);
		double mag = Math.sqrt(x * x + y * y + z * z);
		
		this.x = x / mag;
		this.y = y / mag;
		this.z = z / mag;
		this.s = mag;
	}
	
	public String toString(){
		return "(" + x + "," + y + "," + y + "," + s + ")";
	}
	
	public Vector crossProduct(Vector v){
		Vector u = this;
		double u1 = u.x;
		double u2 = u.y;
		double u3 = u.z;
		
		double v1 = v.x;
		double v2 = v.y;
		double v3 = v.z;
		
		Matrix x = new Matrix(new double[][]{{u2,u3},{v2,v3}});
		Matrix y = new Matrix(new double[][]{{u1,u3},{v1,v3}});
		Matrix z = new Matrix(new double[][]{{u1,u2},{v1,v2}});
		
		return new Vector(x.determinant(),y.determinant(),z.determinant());
	}
	
	public double dotProduct(Vector v){
		
	}
}
