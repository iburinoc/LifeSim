package life.threedee.game;

import life.lib.Tickable;
import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;

public class Player extends Camera implements Tickable{
    private Vector v;

    public Player() {
        super();
        this.v = new Vector(0, 0, 0);
    }

    public Player(Point loc, Vector dir, Vector v) {
        super(loc, dir);
        this.v = v;
    }

    @Override
    public void move(int d) {
    }

    public void tick() {
        Point newLoc = new Point(new Vector(loc).add(v));
        for (ThreeDeeObject object : objects) {
            if (!object.sameSide(loc, newLoc)) {
                newLoc = loc;
                v = new Vector(0, 0, 0);
            }
        }
        loc = newLoc;
    }
}
