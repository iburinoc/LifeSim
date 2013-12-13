package life.threedee;

public class Point {
    public final double x, y, z;

    public Point(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

	public Point(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
	public String toString(){
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
