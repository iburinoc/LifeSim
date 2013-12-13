package life.threedee.test;

import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.Vector;


public class PlaneTest {

	public void testEvaluate() {
		Plane p = new Plane(new Point(0,0,1),new Vector(0,1,1));
		assert(p.evaluate(0, Double.NaN, 1).y == 0);
	}

	public void testIntersection() {
		
	}
}
