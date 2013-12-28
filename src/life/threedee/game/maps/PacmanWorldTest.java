package life.threedee.game.maps;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.TexturedPlane;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;

public class PacmanWorldTest{
	public static void genTunnel(List<ThreeDeeObject> o) {
		Plane f = new Plane(new Point(0,0,0), new Vector(0,-1,0), Color.black);
		Plane c = new Plane(new Point(0,2,0), new Vector(0,-1,0), Color.black);
		Plane l = new Plane(new Point(-1,0,0), new Vector(1,0,0), Color.blue);
		Plane r = new Plane(new Point(1,0,0), new Vector(1,0,0), Color.blue);
		o.add(f);
		o.add(c);
		o.add(l);
		o.add(r);
	}
	
	public static void genTexturedTunnel(List<ThreeDeeObject> o) {
		BufferedImage wall = WallTexFactory.createWallTex(4);
		Plane l = new TexturedPlane(new Point(-1,0,0), new Vector(1,0,0), wall);
		Plane r = new TexturedPlane(new Point(1,0,0), new Vector(1,0,0), wall);
		o.add(l);
		o.add(r);
	}
}
