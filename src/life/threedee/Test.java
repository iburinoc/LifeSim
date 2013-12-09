package life.threedee;

public class Test{
	public static void mainCrossProduct(String[] args){
		Vector u = new Vector(3,1,-5);
		Vector v = new Vector(4,-2,-3);
		System.out.println(u + "x" + v + "=" + u.crossProduct(v));
	}
	
	public static void main(String[] args){
		Plane p = new Plane(new Point(0,0,1),new Vector(0,0,1));
		Point a = new Point(0,1,0);
		Vector v = new Vector(0,0,1);
		System.out.println(p.intersection(v, a));
	}
}
