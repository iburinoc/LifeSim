package life.threedee.objects;

import java.awt.Color;

import life.lib.Tickable;
import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;

public class Bullet extends Triangle implements Tickable{

	private Vector movement;
	
	public Bullet(Point a, Point b, Point c){
		super(a, b, c, Color.black);
		movement = new Vector(Math.random(),0,Math.random()).setScalar(0.1);
	}

	public void tick(){
		super.translate(movement);
	}
}
