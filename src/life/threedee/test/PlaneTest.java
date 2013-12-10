package life.threedee.test;

import static org.junit.Assert.*;

import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.Test;
import life.threedee.Vector;
import org.junit.Before;
import org.junit.Test;

public class PlaneTest {

    @Test
	public void testEvaluate() {
		Plane p = new Plane(new Point(0,0,1),new Vector(0,1,1));
		assert(p.evaluate(0, Double.NaN, 1).y == 0);
	}
	
	@Test
	public void testIntersection() {
		
	}
}