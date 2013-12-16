package life.threedee.objects;

import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.game.Tickable;

public class MovementTester implements Tickable {
    private Triangle t;
    private int tick;
    
    public MovementTester(Triangle t) {
        tick = 0;
        this.t = t;
    }
    
    @Override
    public void tick(int delta) {
        tick++;
        t.translateC(new Vector(0, 0, Math.signum(50 - tick)/10));
        if (tick == 100) {
            tick = 0;
        }
    }
}
