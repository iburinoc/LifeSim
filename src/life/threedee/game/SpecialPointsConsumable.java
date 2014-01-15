package life.threedee.game;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.Triangle;
import life.threedee.Vector;

public class SpecialPointsConsumable extends Consumable {
    private static final double A_INC = Math.PI / 90;
    private static final double C_QUARTER = Math.PI/2;
    private static final double C_FIFTH = 2*Math.PI/5;
    private static final Color[] colors = {new Color(255, 92, 205), Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};
    
    private Point front;
    private Point back;

    public SpecialPointsConsumable(Point center) {
        super(center, 10, 5, A_INC);
    }

    // Should not be called. Use .getRData.c() instead.
    @Override
    public Color c() {
        return Color.PINK;
    }

    @Override
    protected void generate() {
        double cyaw = yaw;
        double cpitch = C_QUARTER - 2*C_FIFTH;
        for(int i = 0; i < 5; i++) {
            p[i] = new Point(Vector.fromPolarTransform(cyaw, cpitch, 0.5));
            cpitch += C_FIFTH;
        }
        front = new Point(Vector.fromPolarTransform(cyaw+C_QUARTER, 0, 0.2));
        back = new Point(Vector.fromPolarTransform(cyaw-C_QUARTER, 0, 0.2));
        for (int i = 0; i < 5; i++) {
            t[2*i] = new Triangle(front, p[i], p[(i+2)%5], colors[i]);
            t[2*i+1] = new Triangle(back, p[i], p[(i+2)%5], colors[(i+1)%5]);
        }
        translate(new Vector(center.x, 0.625, center.z));
    }

    @Override
    public TColorTransfer getRData(Vector v, Point p, double minT) {
        if(eaten) {
            return new TColorTransfer(Double.NaN, null, null);
        }
        double min = Double.NaN;
        Triangle closest = null;
        for(Triangle t : this.t) {
            double ct = t.calculateT(v, p, minT);
            if(ct == ct && !(ct > min) && !(ct > min)) {
                closest = t;
                min = ct;
            }
        }
        if (closest != null) {
            return closest.getRData(v, p, minT);
        }
        return new TColorTransfer(Double.NaN, null, null);
    }
    
    @Override
    public void eat(Game g, Player p) {
        eaten = true;
        g.pointsBonus();
        p.stop(1);
    }
}
