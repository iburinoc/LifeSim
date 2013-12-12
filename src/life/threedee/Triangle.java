package life.threedee;

import java.awt.Color;

/**
 * 
 * @author gud wizard
 * @author eclipse
 * @author a bit of zeus hammer
 *
 */

public class Triangle extends Plane{

	private Point3D a, b, c;
    private double[] yaw = new double[6];

	public Triangle(Point3D a, Point3D b, Point3D c) {
		this(a, b, c, new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256)));
	}

	public Triangle(Point3D a, Point3D b, Point3D c, Color colour){
		super(a, new Vector(a,b).crossProduct(new Vector(a,c)), colour);
		this.a = a;
		this.b = b;
		this.c = c;
        yaw[0] = new Vector(a, b).polarTransform()[0];
        yaw[1] = new Vector(a, c).polarTransform()[0];
        yaw[2] = new Vector(b, a).polarTransform()[0];
        yaw[3] = new Vector(b, c).polarTransform()[0];
        yaw[4] = new Vector(c, a).polarTransform()[0];
        yaw[5] = new Vector(c, b).polarTransform()[0];
	}
	
	@Override
	public double calculateT(Vector vector, Point3D point3D){
		double t = super.calculateT(vector, point3D);
		Point3D inter = super.intersection(vector, point3D, t);
		
		if(inside(inter)){
			return t;
		} else {
			return Double.NaN;
		}
	}
	
    public boolean inside(Point3D point) {
        double yawPoint = new Vector(a, point).polarTransform()[0];
        if (inYaws(yawPoint, yaw[0], yaw[1])) {
            return false;
        }
        yawPoint = new Vector(b, point).polarTransform()[0];
        if (inYaws(yawPoint, yaw[2], yaw[3])) {
            return false;
        }
        yawPoint = new Vector(c, point).polarTransform()[0];
        if (inYaws(yawPoint, yaw[4], yaw[5])) {
            return false;
        }
        return true;
    }
    
    private boolean inYaws(double yawPoint, double yaw1, double yaw2){
    	return (Math.max(yaw1, yaw2) - Math.min(yaw1, yaw2) < Math.PI &&
                (yawPoint > Math.max(yaw1, yaw2) || yawPoint < Math.min(yaw1, yaw2))) ||
                (Math.max(yaw1, yaw2) - Math.min(yaw1, yaw2) > Math.PI &&
                        (yawPoint < Math.max(yaw1, yaw2) && yawPoint > Math.min(yaw1, yaw2)));
    }
}
