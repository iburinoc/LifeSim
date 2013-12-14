package life.threedee.objects;

import life.lib.Tickable;
import life.threedee.Triangle;
import life.threedee.Vector;

public class MovementTester implements Tickable {
    private Triangle t;
    private int tick;
    
    public MovementTester(Triangle t) {
        tick = 0;
        this.t = t;
    }
    
    @Override
    public void tick() {
        tick++;
        t.translateA(new Vector(0, 0, Math.signum(50 - tick)));
        if (tick == 100) {
            tick = 0;
        }
    }
}
