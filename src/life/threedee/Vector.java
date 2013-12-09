package life.threedee;

public class Vector{
	public final double p,r,y;
	public final double s;
	
	public Vector(double p,double r,double y){
		this(p,r,y,1);
	}
	
	public Vector(double p,double r,double y,double s){
		assert(Math.abs(p * p + r * r + y * y - 1) < 0.0000001);
		this.p = p;
		this.r = r;
		this.y = y;
		this.s = s;
	}
}
