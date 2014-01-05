package life.threedee.game;

import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;

public class Player extends Camera implements Tickable{
    private Game g;
    
    private Vector v;
    
    protected boolean w,d,s,a;
    
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

    @Override
    public void calcBuffer() {
    	if(g == null)
    		return;
    	this.objects = g.objects();
    	super.calcBuffer();
    }

    public void tick(int delta){
        Point newLoc = new Point(new Vector(loc).add(v));
        for (ThreeDeeObject object : objects) {
            if (!object.sameSide(loc, newLoc) || true) {
                newLoc = loc;
                v = new Vector(0, 0, 0);
            }
        }
        loc = newLoc;
    }

    public Location getLoc(){
        return new Location(loc.x, loc.z);
    }

    public Point getLocPoint(){
        return loc;
    }

    public Vector getDir(){
        return dir;
    }
}
