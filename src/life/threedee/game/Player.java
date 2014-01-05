package life.threedee.game;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.G;

import java.util.List;

import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;

public class Player extends Camera implements Tickable{
    private Game g;
    private GameMap m;
    
    private Vector v;
    
    protected boolean w,d,s,a;
    
    private List<ThreeDeeObject> map;
    
    public Player(Game g, GameMap m) {
        super();
        this.v = new Vector(0, 1, 0);
        this.g = g;
        this.m = m;
    }

    public Player(Point loc, Vector dir, Vector v, Game g) {
        super(loc, dir);
        this.v = v;
        this.g = g;
    }

    @Override
    public void calcBuffer() {
    	if(g != null || m != null){
    		if(g == null)
    			this.objects = null;
    		else
    			this.objects = g.objects();
    		if(m == null)
    			this.map = null;
    		else
    			this.map = m.getObjects(loc);
    	}
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
