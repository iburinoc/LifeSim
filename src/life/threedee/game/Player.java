package life.threedee.game;

import static java.lang.Math.PI;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;

/**
 * A player, extends Camera and has some extra additions to represent the fact that it is part of the game
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 * 
 */
public class Player extends Camera implements Tickable{
    private Game g;
    private GameMap m;
    
    protected boolean w, d, s, a;
    
    protected int stop;
    
    private List<ThreeDeeObject> map;
    
    public Player(Game g, GameMap m) {
        super(new Point(0, 1, -8.5), new Vector(-1, 0, 0));
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

    @Override
    public void paintBuffer(Graphics g) {
    	super.paintBuffer(g);
    	this.g.drawScore(g);
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
    	
    	mov = mov.setScalar(g.getMode() == -1 ? ((GameUtilities.GAME_DATA[g.getLevel()][0] + 100) / 5000.0) : (GameUtilities.GAME_DATA[g.getLevel()][0] / 2500.0));
    	if(mov.s() != mov.s()) {
    		return;
    	}
    	Point mloc = loc.add(new Point(mov));

    	double yaw = mov.yaw();
    	slide:
    	if(!sameSideAll(loc, mloc)){
    		double yawl = yaw, yawr = yaw;
    		while(Math.abs((yawl - yaw) % (2*PI)) < PI) {
    			yawl += PI/90;
    			yawr -= PI/90;
    			Vector l = Vector.fromPolarTransform(yawl, 0, mov.s());
    			if(sameSideAll(loc, loc.add(new Point(l.setScalar(l.dotProduct(mov.setScalar(1))))))) {
    				mov = l.setScalar(l.dotProduct(mov.setScalar(1)));
    				mloc = loc.add(new Point(mov));
    				break slide;
    			}
    			Vector r = Vector.fromPolarTransform(yawr, 0, mov.s());
    			if(sameSideAll(loc, loc.add(new Point(r.setScalar(r.dotProduct(mov.setScalar(1))))))) {
    				mov = r.setScalar(r.dotProduct(mov.setScalar(1)));
    				mloc = loc.add(new Point(mov));
    				break slide;
    			}
    		}
    		mloc = loc;
    	}
    	if(crossTunnel(loc, mloc)) {
    		mloc = new Point(mloc.x - 28 * Math.signum(mloc.x), mloc.y, mloc.z);
    	}
    	loc = mloc;
    	
    }
    
    private boolean crossTunnel(Point a, Point b) {
    	if(map != null) {
    		for(ThreeDeeObject wall : map) {
    			if(wall instanceof TunnelPlane && !wall.sameSide(a, b)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    private boolean sameSideAll(Point a, Point b) {
    	if(map != null) {
    		for(ThreeDeeObject wall : map) {
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

    public void tick(){
        if (stop == 0) {
    	    move();
        } else {
        	stop--;
        }
        if ((Math.abs(loc.x) > 13.5 && Math.abs(loc.z - 0.5) > 1) || (Math.abs(loc.x) > 13.5 && Math.abs(loc.z - 0.5) > 1)) {
            loc = new Point(loc.x, loc.y, loc.z > 0 ? 1.499999 : -0.499999);
        }
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
        if (min.o instanceof TunnelPlane && Math.abs(px.x) <= 14 && ((TunnelPlane) min.o).id == Math.signum(px.x)){
            List<ThreeDeeObject> mapTmp = m.getObjects(new Point(-px.x, px.y, px.z));
            min = closestInFront(dir, px.subtract(new Point(28 * Math.signum(px.x), 0, 0)), mapTmp);
        }
		return min;
	}

    public Point getLoc(){
        return loc;
    }

    public void stop(int len){
        stop = len;
    }

    public void setLoc(Point loc){
        this.loc = loc;
    }

    public void setDir(Vector dir){
        this.dir = dir;
    }

    public Vector getDir(){
        return dir;
    }
}
