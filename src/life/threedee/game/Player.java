package life.threedee.game;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.G;
import life.lib.Tickable;
import life.threedee.*;

public class Player extends Camera implements Tickable{
    private Vector v;
    private ThreeDeeObject floor = null;

    public Player() {
        super();
        this.v = new Vector(0, 0, 0);
    }

    public Player(Point loc, Vector dir, Vector v) {
        super(loc, dir);
        this.v = v;
    }

    public void move(int d){
        if (floor != null) {
            Vector mov = Vector.fromPolarTransform(dir.polarTransform()[0] + PI / 2 * d, 0, 0.1);
            v = v.add(mov);
        }
    }

    public void jump(){
        //if (canJump())  {
            v = new Vector(v.x, 9.81, v.z);
        //}
    }

    public void stop(){
        if (floor != null) {
            v = new Vector(0, v.y, 0);
        }
    }

    public void tick(){
        v = v.add(G);
        if (floor != null) {
            v = new Vector(v.x, 0, v.z);
        }
        Point newLoc = new Point(new Vector(loc).add(v));
        for (ThreeDeeObject object : objects) {
            if (!object.sameSide(new Point(loc.x, loc.y - 1.65, loc.z), new Point(newLoc.x, newLoc.y - 1.65, newLoc.z))) {
                newLoc = loc;
                v = new Vector(0, 0, 0);
            }
            if (!object.sameSide(loc, new Point(loc.x, loc.y - 2.65, loc.z))) {
                floor = object;
            }
        }
        loc = newLoc;
        System.out.println(floor);
    }
}
