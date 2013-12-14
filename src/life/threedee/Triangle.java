package life.threedee;

import java.awt.Color;

public class Triangle extends Plane {
	private Point a, b, c;
    
	public Triangle(Point a, Point b, Point c) {
		this(a, b, c, new Color((int) (Math.random() * 256),(int) (Math.random() * 256),(int) (Math.random() * 256)));
	}

	public Triangle(Point a, Point b, Point c, Color colour){
		super(a, new Vector(a,b).crossProduct(new Vector(a,c)), colour);
		this.a = a;
		this.b = b;
		this.c = c;
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
    
    public boolean inside(Point point) {
    	Vector u = new Vector(a, b);
    	Vector v = new Vector(a, c);
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
    
    @Override
    public void translate(Vector v){
    	super.translate(v);
    	a = new Point(a.x + v.x, a.y + v.y, a.z + v.z);
    	b = new Point(b.x + v.x, b.y + v.y, b.z + v.z);
    	c = new Point(c.x + v.x, c.y + v.y, c.z + v.z);
    }

    public void stretch(Point origin, double elasticity) {
        a = a.subtract(origin).stretch(elasticity).add(origin);
        b = b.subtract(origin).stretch(elasticity).add(origin);
        c = c.subtract(origin).stretch(elasticity).add(origin);
    }
}
