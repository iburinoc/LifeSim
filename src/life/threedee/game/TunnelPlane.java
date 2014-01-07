package life.threedee.game;

import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.Vector;

import java.awt.*;

public class TunnelPlane extends Plane{
    public TunnelPlane(Point a, Point b, Point c){
        super(a,b,c, a.x > 0 ? Color.MAGENTA : Color.GREEN);
    }

    public TunnelPlane(Point origin, Vector normal){
        super(origin, normal, origin.x > 0 ? Color.MAGENTA : Color.GREEN);
    }
}
