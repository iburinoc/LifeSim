package life.threedee;

public class Test{
	public static void main(String[] args){
		Vector u = new Vector(3,1,-5);
		Vector v = new Vector(4,-2,-3);
		System.out.println(u + "x" + v + "=" + u.crossProduct(v));
	}
}
