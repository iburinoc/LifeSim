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
		
	}
	
	public double dotProduct(Vector v){
		
	}
}
