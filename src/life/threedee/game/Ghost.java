package life.threedee.game;

import life.threedee.GhostPlane;
import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;

public class Ghost implements Tickable{
    protected Location location, target;
    protected final Location eyesTarget = new Location(-0.5, 3.5);
    protected int direction;
    protected boolean uTurn, eaten;
    protected Game game;
    protected GhostPlane zPlusPlane, xPlusPlane, zMinusPlane, xMinusPlane;
    protected Triangle zPlusTriangle, xPlusTriangle, zMinusTriangle, xMinusTriangle;

    public Ghost(Game g, int ghostNum) {
        Point top = new Point(0.0, 2.0, 0.0);
        Point zPlusXPlus = new Point(0.25, 1.5, 0.25);
        Point zMinusXPlus = new Point(0.25, 1.5, -0.25);
        Point zMinusXMinus = new Point(-0.25, 1.5, -0.25);
        Point zPlusXMinus = new Point(-0.25, 1.5, 0.25);
        Point lowerZPlusXPlus = new Point(0.5, 0.0, 0.5);
        Point lowerZMinusXPlus = new Point(0.5, 0.0, -0.5);
        Point lowerZMinusXMinus = new Point(-0.5, 0.0, -0.5);
        Point lowerZPlusXMinus = new Point(-0.5, 0.0, 0.5);
        zPlusTriangle = new Triangle(top, zPlusXPlus, zPlusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        xPlusTriangle = new Triangle(top, zPlusXPlus, zMinusXPlus, GameUtilities.GHOST_COLORS[ghostNum]);
        zMinusTriangle = new Triangle(top, zMinusXPlus, zMinusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        xMinusTriangle = new Triangle(top, zMinusXMinus, zPlusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        zPlusPlane = new GhostPlane(lowerZPlusXPlus, new Vector(lowerZPlusXPlus, zPlusXPlus).crossProduct(new Vector(lowerZPlusXPlus, lowerZMinusXPlus)), ghostNum);
        xPlusPlane = new GhostPlane(lowerZMinusXPlus, new Vector(lowerZMinusXPlus, zMinusXPlus).crossProduct(new Vector(lowerZMinusXPlus, lowerZMinusXMinus)), ghostNum);
        zMinusPlane = new GhostPlane(lowerZMinusXMinus, new Vector(lowerZMinusXMinus, zMinusXMinus).crossProduct(new Vector(lowerZMinusXMinus, lowerZPlusXMinus)), ghostNum);
        xMinusPlane = new GhostPlane(lowerZPlusXMinus, new Vector(lowerZPlusXMinus, zPlusXMinus).crossProduct(new Vector(lowerZPlusXMinus, lowerZPlusXPlus)), ghostNum);
        g.addObject(zPlusPlane);
        g.addObject(xPlusPlane);
        g.addObject(zMinusPlane);
        g.addObject(xMinusPlane);
        g.addObject(zPlusTriangle);
        g.addObject(xPlusTriangle);
        g.addObject(zMinusTriangle);
        g.addObject(xMinusTriangle);
        
        g.addTickable(this);
    }
    
    public Ghost(Location location, int direction, Game game, int ghostNum) {
        this(game, ghostNum);
        this.location = location;
        this.direction = direction;
        this.game = game;
    }
    
    public int makeDecision(boolean[] open){
        if (uTurn){
            return (direction + 2) % 4;
        }
        target = findTarget();
        open[(direction + 2) % 4] = false;
        double shortest = Double.MAX_VALUE;
        int toReturn = 0;
        for (int i = 3; i >= 0; i--){
            Location choice = new Location(location.x + (i == 1 ? -1 : (i == 3 ? 1 : 0)), location.z + (i == 0 ? 1 : (i == 2 ? -1 : 0)));
            if (open[i] && choice.distanceTo(target) <= shortest){
                shortest = choice.distanceTo(target);
                toReturn = i;
            }
        }
        return toReturn;
    }

    public Location findTarget() {
        return null;
    }

    public void tick(int delta){
        zPlusPlane.shiftTexture();
        xPlusPlane.shiftTexture();
        zMinusPlane.shiftTexture();
        xMinusPlane.shiftTexture();
    }
    
    public void move(Vector v) {
        //ANDREY, PUT MOVEMENT CODE HERE!
        zPlusPlane.translate(v);
        xPlusPlane.translate(v);
        zMinusPlane.translate(v);
        xMinusPlane.translate(v);
        zPlusTriangle.translate(v);
        xPlusTriangle.translate(v);
        zMinusTriangle.translate(v);
        xMinusTriangle.translate(v);
    }

    public Location getLocation(){
        return location;
    }
}
