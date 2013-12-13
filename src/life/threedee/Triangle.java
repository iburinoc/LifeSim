package life.threedee;

import java.awt.Color;

/**
 * 
 * @author gud wizard
 * @author eclipse
 * @author a bit of zeus hammer
 *
 */

public class Triangle extends Plane {

	private Point a, b, c;
    private double[] yaw = new double[6];

    private Vector ab, ac;
    
	public Triangle(Point a, Point b, Point c) {
		this(a, b, c, new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256)));
	}

	public Triangle(Point a, Point b, Point c, Color colour){
		super(a, new Vector(a,b).crossProduct(new Vector(a,c)), colour);
		this.a = a;
		this.b = b;
		this.c = c;
        yaw[0] = new Vector(a, b).yaw();
        yaw[1] = new Vector(a, c).yaw();
        yaw[2] = new Vector(b, a).yaw();
        yaw[3] = new Vector(b, c).yaw();
        yaw[4] = new Vector(c, a).yaw();
        yaw[5] = new Vector(c, b).yaw();
        
        ab = new Vector(a,b);
        ac = new Vector(a,c);
	}
	
	@Override
	public double calculateT(Vector vector, Point point){
		double t = super.calculateT(vector, point);
		Point inter = super.intersection(vector, point, t);
		if(inside(inter)){
			return t;
		} else {
			return Double.NaN;
		}
	}
	
    public boolean insideYaws(Point point) {
        double yawPoint = new Vector(a, point).yaw();
        if (inYaws(yawPoint, yaw[0], yaw[1])) {
            return false;
        }
        yawPoint = new Vector(b, point).yaw();
        if (inYaws(yawPoint, yaw[2], yaw[3])) {
            return false;
        }
        yawPoint = new Vector(c, point).yaw();
        if (inYaws(yawPoint, yaw[4], yaw[5])) {
            return false;
        }
        return true;
    }
    
    public boolean inside(Point point) {
    	Vector u = ab;
    	Vector v = ac;
    	Vector w = new Vector(a,point);
    	
    	double
    		uv = u.dotProduct(v),
    		wv = w.dotProduct(v),
    		wu = w.dotProduct(u),
    		uu = u.dotProduct(u),
    		vv = v.dotProduct(v),
    		D = uv * uv - uu * vv;
    	
    	double s = (uv * wv - vv * wu) / D;
    	if(s < 0 || s > 1){
    		return false;
    	}
    	double t = (uv * wu - uu * wv) / D;
    	if(t < 0 || t + s > 1){
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
    
    @Override
    public void translate(Vector v){
    	super.translate(v);
    	a = new Point(a.x + v.x, a.y + v.y, a.z + v.z);
    	b = new Point(b.x + v.x, b.y + v.y, b.z + v.z);
    	c = new Point(c.x + v.x, c.y + v.y, c.z + v.z);
    }
}
