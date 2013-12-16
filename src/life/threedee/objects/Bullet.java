package life.threedee.objects;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.game.Tickable;

public class Bullet extends Triangle implements Tickable{

	private Vector movement;
	
	public Bullet(Point a){
		super(a.add(new Point(0,-1,0)), a.add(new Point(new Vector(-0.5,0.28867513459481,0).setScalar(01))), a.add(new Point(new Vector(0.5,0.28867513459481,0).setScalar(01))), Color.black);
		movement = new Vector(Math.random()*2-1,Math.random()*2-1,Math.random()*2-1).setScalar(0.1);
	}

	public void tick(int delta){
		super.translate(movement);
	}
}
