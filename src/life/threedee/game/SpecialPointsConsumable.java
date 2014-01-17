package life.threedee.game;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.Triangle;
import life.threedee.Vector;

public class SpecialPointsConsumable extends Consumable {
    private static final double A_INC = Math.PI / 90;
    private static final double C_QUARTER = Math.PI/2;
    private static final Color[] colors = {new Color(255, 92, 205), Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY, Color.BLACK};
    
    private Point front;
    private Point back;
    
    private int amount;

    public SpecialPointsConsumable(Point center, int level) {
        super(center, (level+2)*2, level+2, A_INC);
        amount = level+2;
    }

    public void despawn() {
        eaten = true;
    }
    
    public void updateLevel(int level) {
        amount = level+2;
        t = new Triangle[2*amount];
        p = new Point[amount];
    }
    
    public double c_part() {
        return 2*Math.PI/amount;
    }
    
    // Should not be called. Use .getRData.c() instead.
    @Override
    public Color c() {
        return Color.PINK;
    }

    @Override
    protected void generate() {
        if (amount > 2) {
            double cyaw = yaw;
            double cpitch = (cyaw%(c_part())-c_part()) + C_QUARTER + (amount/2-0.5)*c_part();/*C_QUARTER - (amount/2)*c_part();*/
            for(int i = 0; i < amount; i++) {
                p[i] = new Point(Vector.fromPolarTransform(cyaw, cpitch, 0.5));
                cpitch -= c_part();
            }
            front = new Point(Vector.fromPolarTransform(cyaw+C_QUARTER, 0, 0.2));
            back = new Point(Vector.fromPolarTransform(cyaw-C_QUARTER, 0, 0.2));
            for (int i = 0; i < amount; i++) {
                t[2*i] = new Triangle(front, p[i], p[(i+((amount+1)/3))%amount], colors[(((-i-((int) (cyaw/c_part())))%amount+amount)%amount)%7/*i%7*/]);
                t[2*i+1] = new Triangle(back, p[i], p[(i+((amount+1)/3))%amount], colors[(((-i-((int) (cyaw/c_part()))+1)%amount+amount)%amount)%7/*(i+1)%7*/]);
            }
            translate(new Vector(center.x, 0.625, center.z));
        } else {
            double cyaw = yaw;
            double cpitch = C_QUARTER - (p.length/2)*2*Math.PI/(p.length);
            for(int i = 0; i < p.length; i++) {
                p[i] = new Point(Vector.fromPolarTransform(cyaw, cpitch, 0.5));
                cpitch += c_part();
            }
            front = new Point(Vector.fromPolarTransform(cyaw+C_QUARTER, 0, 0.2));
            back = new Point(Vector.fromPolarTransform(cyaw-C_QUARTER, 0, 0.2));
            for (int i = 0; i < p.length; i++) {
                t[2*i] = new Triangle(front, p[i], p[(i+((p.length+1)/3))%p.length], colors[i%7]);
                t[2*i+1] = new Triangle(back, p[i], p[(i+((p.length+1)/3))%p.length], colors[(i+1)%7]);
            }
            translate(new Vector(center.x, 0.625, center.z));
        }
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
    }
}
