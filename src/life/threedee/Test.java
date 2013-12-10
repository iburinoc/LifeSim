package life.threedee;

public class Test{
	public static void mainCrossProduct(String[] args){
		Vector u = new Vector(3, 1, -5);
		Vector v = new Vector(4, -2, -3);
		System.out.println(u + " x " + v + " = " + u.crossProduct(v));
	}

	public static void main(String[] args){
		System.out.println(new Matrix(new double[][]{{1,5,3},{2,4,7},{4,6,2}}).determinant());
		Plane plane = new Plane(new Point(0, 0, 1), new Vector(0, 0, 1));
		Point point = new Point(0, 1, 0);
		Vector vector = new Vector(0, 0, 1);
		System.out.println(plane.intersection(vector, point) + "\n\n");
        Vector otherVector = new Vector(1, 1, 1);
        System.out.println(otherVector.polarTransform()[0] + "\t" + otherVector.polarTransform()[1]);
        System.out.println(otherVector.relativeTransform(otherVector.polarTransform()[0], otherVector.polarTransform()[1], otherVector.s));
	}
}
