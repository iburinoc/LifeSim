package life.threedee;

public class Triangle extends Plane{

	private Point3D a, b, c;
    private Vector ab, ac;
    private double[] yaw = new double[2];

	public Triangle(Point3D a, Point3D b, Point3D c) {
		super(a, new Vector(a,b).crossProduct(new Vector(a,c)));
		this.a = a;
		this.b = b;
		this.c = c;
        ab = new Vector(a, b);
        ac = new Vector(a, c);
        yaw[0] = ab.polarTransform()[0];
        yaw[1] = ac.polarTransform()[0];
	}

    public boolean inside(Point3D point) {
        boolean inside = true;
        double yawPoint = new Vector(a, point).polarTransform()[0];
        if ((Math.max(yaw[0], yaw[1]) - Math.min(yaw[0], yaw[1]) < Math.PI &&
                (yawPoint > Math.max(yaw[0], yaw[1]) || yawPoint < Math.min(yaw[0], yaw[1]))) ||
                (Math.max(yaw[0], yaw[1]) - Math.min(yaw[0], yaw[1]) > Math.PI &&
                        (yawPoint < Math.max(yaw[0], yaw[1]) && yawPoint > Math.min(yaw[0], yaw[1])))) {
            inside = false;
        }
        return inside;
    }
}
