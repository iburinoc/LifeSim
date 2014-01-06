package life.threedee.game;

import static java.lang.Math.PI;
import java.awt.Color;
import java.util.List;
import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;

public class Player extends Camera implements Tickable{
    private Game g;
    private GameMap m;
    
    protected boolean w,d,s,a;
    
    private List<ThreeDeeObject> map;
    
    public Player(Game g, GameMap m) {
        super();
        this.g = g;
        this.m = m;
    }

    public Player(Point loc, Vector dir, Game g) {
        super(loc, dir);
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

    public synchronized void move(int d) {
        double[] pt = dir.polarTransform();
        pt[0] -= PI / 2 * d;
        Vector mov = Vector.fromPolarTransform(pt[0], 0, 0.1);//d % 2 == 1 ? 0 : (d == 0 ? pt[1] : -pt[1])
        //loc = new Point(loc.x+mov.x/10,loc.y/*+mov.y*/,loc.z+mov.z/10);
        Point newLoc = new Point(loc.x+mov.x,loc.y,loc.z+mov.z);
        if(map != null) {
        	for (ThreeDeeObject wall : map) {
        		if (!wall.sameSide(loc, newLoc) && false){
        			newLoc = loc;
        		}
        	}
        }
        loc = newLoc;
    }

    public void tick(int delta){
    	if(w) move(0);
    	if(d) move(1);
    	if(s) move(2);
    	if(a) move(3);
    }
    
    @Override
    protected TColorTransfer closestInFront(Vector dir, Point px){
		TColorTransfer min = new TColorTransfer(Double.MAX_VALUE, Color.white, null);
		if(map != null) {
			for(ThreeDeeObject p : map){
				TColorTransfer o = p.getRData(dir, px, min.t);
				if(min.t > o.t && o.t >= 0 && o.t == o.t && o.c != null && o.c.getAlpha() != 0){
					min = o;
				}
			}
		}
		if(objects != null) {
			for(ThreeDeeObject p : objects){
				TColorTransfer o = p.getRData(dir, px, min.t);
				if(min.t > o.t && o.t >= 0 && o.t == o.t && o.c != null && o.c.getAlpha() != 0){
					min = o;
				}
			}
		}
		return min;
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
