package life.threedee.game;

import static life.threedee.game.GameUtilities.G;
import life.lib.Tickable;
import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;

public class Player extends Camera implements Tickable{
    private Vector v;

    public Player(Point loc, Vector dir, Vector v) {
        super(loc, dir);
        this.v = v;
    }

    public void tick(){
        v = v.add(G);
        Point newLoc = new Point(new Vector(loc).add(v));
        for (ThreeDeeObject object : objects) {
            /*if (object.sameSide(loc, newLoc)) {
                newLoc = loc;
            }*/
        }
        loc = newLoc;
    }
}
