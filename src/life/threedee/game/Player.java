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
 * A player, extends Camera and has some extra additions to represent the fact that it is part of the game.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 * 
 */
public class Player extends Camera implements Tickable{
    private Game g;
    private GameMap m;

    /**
     * Indicates whether the button to move forward is pressed.
     */
    protected boolean w;

    /**
     * Indicates whether the button to move left is pressed.
     */
    protected boolean d;

    /**
     * Indicates whether the button to move backwards is pressed.
     */
    protected boolean s;

    /**
     * Indicates whether the button to move right is pressed.
     */
    protected boolean a;
    
    protected int stop;
    
    private List<ThreeDeeObject> map;

    /**
     * The "default" player constructor. Calls the camera constructor with the default player location and orientation.
     * @param g The game that the player will read input from.
     * @param m The map that contains information in the game.
     */
    public Player(Game g, GameMap m) {
        super(new Point(0, 1, -8.5), new Vector(-1, 0, 0));
        this.g = g;
        this.m = m;
    }

    /**
     * Resets the player upon death.
     */
    public void reset() {
    	w = false;
    	d = false;
    	s = false;
    	a = false;
    	stop = 0;
    }
    
    @Override
    public void calcBuffer() {
    	if(!g.draw3D()) {
    		return;
    	}
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
    	this.g.drawSpecial(g);
    }

    /**
     * Uses input to determine which way to move. Makes sure that the player cannot go through walls.
     */
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
    	
    	mov = mov.setScalar(g.getMode() == -1 ? ((GameUtilities.GAME_DATA[g.getArraySafeLevel()][0] + 100) / 5000.0) : (GameUtilities.GAME_DATA[g.getArraySafeLevel()][0] / 2500.0));
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
    			if(!(Math.abs((yawl - yaw) % (2*PI)) < PI)) {
    				mloc = loc;
    			}
    		}
    		mloc = loc;
    	}
    	if(crossTunnel(loc, mloc)) {
    		mloc = new Point(mloc.x - 28 * Math.signum(mloc.x), mloc.y, mloc.z);
    	}
    	loc = mloc;
    	
    }

    /**
     * Determines if the player wrapped around the map through the tunnel through checking if two points are on opposite sides of a tunnel plane.
     * @param a The first point.
     * @param b The second point.
     * @return Whether the player has crossed through the tunnel.
     */
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

    /**
     * Determines if the player is on the same side of all the walls in the game by checking if two points are on the same side.
     * @param a The first point.
     * @param b The second point.
     * @return Whether the player is on the same side as all the walls.
     */
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

    /**
     * Determines which way to move based on the keys that are being pressed.
     * @param d Direction pressed.
     * @param yaw Direction the player is facing.
     * @return Returns which way the player is going.
     */
    private Vector getMoveVector(int d, double yaw) {
         yaw -= PI / 2 * d;
         return Vector.fromPolarTransform(yaw, 0, 1);
    }

    @Override
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

    /**
     * Determines which plane is the first thing that the player sees in a given pixel.
     * @param dir The direction of the pixel from the player.
     * @param px The coordinates of the pixel.
     * @param map The list of planes to check.
     * @return Returns the colour and distance to the closest plane.
     */
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

    /**
     * Location getter.
     * @return Returns the location.
     */
    public Point getLoc(){
        return loc;
    }

    /**
     * Stop the player for a given number of ticks.
     * @param len Duration of the time player needs to be stopped.
     */
    public void stop(int len){
        stop = len;
    }

    /**
     * Location setter.
     * @param loc New location.
     */
    public void setLoc(Point loc){
        this.loc = loc;
    }

    /**
     * Direction getter.
     * @return Returns the direction.
     */
    public Vector getDir(){
        return dir;
    }

    /**
     * Direction setter.
     * @param dir New direction.
     */
    public void setDir(Vector dir){
        this.dir = dir;
    }
}
