package life.threedee.game;

import life.lib.Tickable;
import life.threedee.Camera;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;

import static java.lang.Math.PI;

public class Player extends Camera implements Tickable{
    private Vector v;
    private boolean w, a, s, d;

    public Player() {
        super();
        this.v = new Vector(0, 0, 0);
    }

    public Player(Point loc, Vector dir, Vector v) {
        super(loc, dir);
        this.v = v;
    }

    @Override
    public void move(int key) {
        v = Vector.fromPolarTransform(dir.polarTransform()[0] += PI / 2 * key, 0, 1);
        System.out.println(v);
        //v = Vector.fromPolarTransform(dir.yaw() + new Vector(w ? 1 : (s ? -1 : 0), 0, d ? 1 : (a ? -1 : 0)).yaw(), 0, 1);
    }

    public void tick(){
        Point newLoc = new Point(new Vector(loc).add(v));
        for (ThreeDeeObject object : objects) {
            if (!object.sameSide(loc, newLoc)) {
                newLoc = loc;
                v = new Vector(0, 0, 0);
                System.out.println("YOU FAIL!");
            }
        }
        System.out.println("YOU FAIL! PART 2");
        loc = newLoc;
    }
}
