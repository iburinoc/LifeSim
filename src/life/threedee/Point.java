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
    
    public Point add(Point a){
    	return new Point(x + a.x, y + a.y, z + a.z);
    }

    public Point subtract(Point a){
        return new Point(x - a.x, y - a.y, z - a.z);
    }

    public Point stretch(double elasticity) {
        return new Point(x * elasticity, y * elasticity, z * elasticity);
    }
}
