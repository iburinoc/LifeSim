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
    
    protected boolean w,d,s,a,up;
    
    private List<ThreeDeeObject> map;
    
    public Player(Game g, GameMap m) {
        super();
        this.v = new Vector(0, 1, 0);
        this.g = g;
        this.m = m;
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
    
    public synchronized void move(int d) {
        if (d < 4) {
            double[] pt = dir.polarTransform();
            pt[0] += PI / 2 * d;
            Vector mov = Vector.fromPolarTransform(pt[0], d % 2 == 1 ? 0 : (d == 0 ? pt[1] : -pt[1]), 1);
            loc = new Point(loc.x+mov.x/10,loc.y/*+mov.y*/,loc.z+mov.z/10);
//            loc = new Point(loc.x+mov.x,loc.y+mov.y,loc.z+mov.z);
        }
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
