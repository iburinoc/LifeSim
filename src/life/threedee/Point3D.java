package life.threedee;

public class Point3D extends Point {
	public final double z;

	public Point3D(double x, double y, double z){
		super(x, y);
		this.z = z;
	}

	public String toString(){
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
