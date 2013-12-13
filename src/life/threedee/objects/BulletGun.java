package life.threedee.objects;

import life.lib.Tickable;
import life.threedee.Camera;
import life.threedee.Point;

public class BulletGun implements Tickable{
	
	private int tick;
	private Camera c;
	
	public BulletGun(Camera c){
		this.c = c;
	}
	
	public void tick(){
		tick++;
		if(tick == 50){
			Bullet b = new Bullet(new Point(0,1.5,0));
			c.add(b);
			c.addTickable(b);
			tick = 0;
		}
	}
}
