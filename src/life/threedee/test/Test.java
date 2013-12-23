package life.threedee.test;

import life.threedee.Matrix;
import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.Vector;

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
        System.out.println(Vector.fromPolarTransform(otherVector.polarTransform()[0], otherVector.polarTransform()[1], otherVector.s()));
        System.out.println(Vector.fromPolarTransform(otherVector.polarTransform()[0] * 2, otherVector.polarTransform()[1], otherVector.s()));
        System.out.println(Vector.fromPolarTransform(otherVector.polarTransform()[0] * 4, otherVector.polarTransform()[1], otherVector.s()));
        System.out.println(Vector.UNIT_X.polarTransform()[0] + ":" + Vector.UNIT_X.polarTransform()[1]);
        System.out.println(Vector.UNIT_Y.polarTransform()[0] + ":" + Vector.UNIT_Y.polarTransform()[1]);
        System.out.println(Vector.UNIT_Z.polarTransform()[0] + ":" + Vector.UNIT_Z.polarTransform()[1]);
        System.out.println(Vector.fromPolarTransform(Vector.UNIT_X.polarTransform()[0], Vector.UNIT_X.polarTransform()[1], 1));
        System.out.println(Vector.fromPolarTransform(Vector.UNIT_Y.polarTransform()[0], Vector.UNIT_Y.polarTransform()[1], 1));
        System.out.println(Vector.fromPolarTransform(Vector.UNIT_Z.polarTransform()[0], Vector.UNIT_Z.polarTransform()[1], 1));
        System.out.println(Double.NaN == Double.NaN);
        System.out.println(-6%5);
        System.out.println(-3%5);
        
        System.out.println(0115);
        
        System.out.println(Double.NaN <= Double.NaN);
        System.out.println(5 > Double.NaN);
        System.out.println(-5 < Double.NaN);
        System.out.println(-5 > Double.NaN);
        
        Vector a = new Vector(1,1,0);
        Vector b = new Vector(1,-0.5,0);
        System.out.println(a.dotProduct(b));
        
        Vector u = new Vector(0, 1, 0);
        Vector r = new Vector(1, 0, 0);
        Vector p = new Vector(3, 5, 0);
        System.out.println(p.dotProduct(u));
        System.out.println(p.dotProduct(r));
	}
}
