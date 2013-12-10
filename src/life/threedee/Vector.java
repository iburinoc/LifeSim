package life.threedee;

public class Vector{
	public final double x, y, z;
	public final double s;

	public Vector(double x, double y, double z){
		this.s = Math.sqrt(x * x + y * y + z * z);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(Point point0, Point point1){
		this.x = (point1.x - point0.x);
		this.y = (point1.y - point0.y);
		this.z = (point1.z - point0.z);
		double mag = Math.sqrt(x * x + y * y + z * z);
		this.s = mag;
	}

	public Vector unitVector(){
		return new Vector(x / s, y / s, z / s);
	}

	public String toString(){
		return "(" + x + ", " + y + ", " + z + ", " + s + ")";
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

		return new Vector(x.determinant(), -y.determinant(), z.determinant());
	}

	public double dotProduct(Vector vector){
		return x * vector.x + y * vector.y + z * vector.z;
	}

    public double[] polarTransform() {
        double yaw = Math.atan(z / x), pitch = Math.atan(y / Math.sqrt(x * x + z * z));
        return new double[] {yaw, pitch};
    }

    public static Vector relativeTransform(double yaw, double pitch, double scalar){
        double x = Math.cos(yaw), z = Math.sin(yaw), y = Math.tan(pitch) * Math.sqrt(x * x + z * z);
        return new Vector(x, y, z).setScalar(scalar);
    }

	public Vector add(Vector v){
		return new Vector(x + v.x, y + v.y, z + v.z);
	}
	
	public Vector scalarProduct(double scalar){
		return new Vector(x * scalar, y * scalar, z * scalar);
	}
	
	public Vector setScalar(double scalar){
		double mod = scalar / s;
		return new Vector(x * mod, y * mod, z * mod);
	}
}
