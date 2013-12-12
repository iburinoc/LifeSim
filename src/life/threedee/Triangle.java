package life.threedee;

public class Triangle extends Plane{

	private Point3D a, b, c;
	
	public Triangle(Point3D origin, Point3D a, Point3D b, Point3D c) {
		super(origin, new Vector(a,b).crossProduct(new Vector(a,c)));
		this.a = a;
		this.b = b;
		this.c = c;
	}

}
