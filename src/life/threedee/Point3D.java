package life.threedee;

public class Point3D {
    public final double x, y, z;

	public Point3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
	public String toString(){
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
