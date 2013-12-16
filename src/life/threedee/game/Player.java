package life.threedee.game;

import static life.threedee.game.GameUtilities.G;
import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;

public class Player extends Camera implements Tickable{
    private Game g;
    
    private Vector v;
    
    protected boolean w,d,s,a,up;
    
    public Player(Game g) {
        super();
        this.v = new Vector(0, 0, 0);
        this.g = g;
    }

    public Player(Point loc, Vector dir, Vector v, Game g) {
        super(loc, dir);
        this.v = v;
        this.g = g;
    }

    public void jump(){
        //if (v.y == 0 && canJump) {
            v = new Vector(v.x, 9.81, v.z);
        //}
    }

    @Override
    public void calcBuffer() {
    	if(g == null)
    		return;
    	this.objects = g.objects();
    	super.calcBuffer();
    }
    
    @Override
    public void move(int d) {
        super.move(d);
    }

    public void tick(int delta){
        v = v.add(G);
        Point newLoc = new Point(new Vector(loc).add(v));
        for (ThreeDeeObject object : objects) {
            if (!object.sameSide(loc, newLoc) || true) {
                newLoc = loc;
                v = new Vector(0, 0, 0);
            }
        }
        loc = newLoc;
    }
}
