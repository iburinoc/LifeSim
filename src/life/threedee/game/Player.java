package life.threedee.game;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.R_INC;

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

    private void move() {
    	double y = dir.yaw();
    	if(y != y) {
    		return;
    	}
    	Vector mov = new Vector(0, 0, 0);
    	if(w) mov = mov.add(getMoveVector(0, y));
    	if(d) mov = mov.add(getMoveVector(1, y));
    	if(s) mov = mov.add(getMoveVector(2, y));
    	if(a) mov = mov.add(getMoveVector(3, y));
    	
    	mov = mov.setScalar(0.25);
    	if(mov.s() != mov.s()) {
    		return;
    	}
    	Point mloc = loc.add(new Point(mov));
    	
    	double yaw = mov.yaw();
    	
    	if(map != null) {
        	for (ThreeDeeObject wall : map) {
        		if ((wall instanceof TunnelPlane)){
                    mloc = mloc.subtract(new Point(28 * Math.signum(mloc.x), 0, 0));
                } else if (!wall.sameSide(loc, mloc)){
        			double yawl = yaw, yawr = yaw;
        			while(Math.abs((yawl - yaw) % (2*PI)) < PI) {
        				yawl += PI/45;
        				yawr -= PI/45;
        				Vector l = Vector.fromPolarTransform(yawl, 0, mov.s());
        				if(wall.sameSide(loc, loc.add(new Point(l)))) {
        					mov = l.setScalar(l.dotProduct(mov.setScalar(1)));
        					mloc = loc.add(new Point(mov));
        					break;
        				}
        				Vector r = Vector.fromPolarTransform(yawr, 0, mov.s());
        				if(wall.sameSide(loc, loc.add(new Point(r)))) {
        					mov = r.setScalar(r.dotProduct(mov.setScalar(1)));
        					mloc = loc.add(new Point(mov));
        					break;
        				}
        			}
        		}
        	}
        }
    	loc = mloc;
    }
    
    private boolean sameSide(Point a, Point b) {
    	if(map != null) {
    		for (ThreeDeeObject wall : map) {
    			if(!(wall instanceof TunnelPlane)) {
    				if(!wall.sameSide(a, b)) {
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }
    
    private Vector getMoveVector(int d, double yaw) {
         yaw -= PI / 2 * d;
         return Vector.fromPolarTransform(yaw, 0, 1);
    }
    
    public synchronized void move(int d) {
        double[] pt = dir.polarTransform();
        pt[0] -= PI / 2 * d;
        Vector mov = Vector.fromPolarTransform(pt[0], 0, 0.25);//d % 2 == 1 ? 0 : (d == 0 ? pt[1] : -pt[1])
        //loc = new Point(loc.x+mov.x/10,loc.y/*+mov.y*/,loc.z+mov.z/10);
        Point newLoc = new Point(loc.x+mov.x,loc.y,loc.z+mov.z);
        if(map != null) {
        	for (ThreeDeeObject wall : map) {
                if ((wall instanceof TunnelPlane)){
                    newLoc = newLoc.subtract(new Point(28 * Math.signum(newLoc.x), 0, 0));
                } else if (!wall.sameSide(loc, newLoc)){
        			newLoc = loc;
        		}
        	}
        }
        loc = newLoc;
    }

    public void tick(int delta){
//    	if(w) move(0);
//    	if(d) move(1);
//    	if(s) move(2);
//    	if(a) move(3);
    	move();
    }
    
    @Override
    protected TColorTransfer closestInFront(Vector dir, Point px) {
    	return closestInFront(dir, px, this.map);
    }
    
    private TColorTransfer closestInFront(Vector dir, Point px, List<ThreeDeeObject> map){
		TColorTransfer min = new TColorTransfer(Double.MAX_VALUE, Color.white, null);
		if(map != null) {
			for(ThreeDeeObject p : map){
				TColorTransfer o = p.getRData(dir, px, min.t);
				if(min.t > o.t && o.t >= 0 && o.t == o.t && o.c != null && o.c.getAlpha() != 0 && (!(o.o instanceof TunnelPlane) || Math.abs(px.x) <= 14)){
					min = o;
				}
			}
		}
		if(objects != null) {
			for(ThreeDeeObject p : objects){
				TColorTransfer o = p.getRData(dir, px, min.t);
				if(min.t > o.t && o.t >= 0 && o.t == o.t && o.c != null && o.c.getAlpha() != 0 && (!(o.o instanceof TunnelPlane) || Math.abs(px.x) <= 14)){
					min = o;
				}
			}
		}
        if (min.o instanceof TunnelPlane && Math.abs(px.x) <= 14){
            List<ThreeDeeObject> mapTmp = m.getObjects(new Point(-px.x, px.y, px.z));
            min = closestInFront(dir, px.subtract(new Point(28 * Math.signum(px.x), 0, 0)), mapTmp);
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
