package life.threedee;

public class Vector {
	public final double x, y, z;
	public final double s;
	
	public Vector (double x, double y, double z){
		double mag = Math.sqrt(x * x + y * y + z * z);
		this.x = x;
		this.y = y;
		this.z = z;
		this.s = mag;
	}
	
	public Vector (Point point0, Point point1) {
		this.x = (point1.x - point0.x);
		this.y = (point1.y - point0.y);
		this.z = (point1.z - point0.z);
		double mag = Math.sqrt(x * x + y * y + z * z);
		this.s = mag;
	}
	
	public Vector unitVector () {
		double mag = Math.sqrt(x * x + y * y + z * z);
		return new Vector(x / mag, y / mag, z / mag);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + s + ")";
	}
	
	public Vector crossProduct (Vector v){
		Vector u = this;
		double u1 = u.x;
		double u2 = u.y;
		double u3 = u.z;

		double v1 = v.x;
		double v2 = v.y;
		double v3 = v.z;

        Matrix x = new Matrix(new double[][]{{u2, u3}, {v2, v3}});
        Matrix y = new Matrix(new double[][]{{u1, u3}, {v1, v3}});
        Matrix z = new Matrix(new double[][]{{u1, u2}, {v1, v2}});

        return new Vector(x.determinant(), -y.determinant(), z.determinant());
	}
	
	public double dotProduct(Vector vector){
		return x * vector.x + y * vector.y + z * vector.z;
	}
}
