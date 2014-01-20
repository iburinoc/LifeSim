package life.threedee.game;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.Triangle;
import life.threedee.Vector;

/**
 * A Special Points Consumable that can be eaten in the game.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class SpecialPointsConsumable extends Consumable {
    // A quarter of a circle in radians. Used for finding the locations of the forward and back points.
    private static final double C_QUARTER = Math.PI/2;
    // The colors of the various triangles.
    private static final Color[] colors = {new Color(255, 92, 205), Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY, Color.BLACK};
    
    // The point at the front of the SPC.
    private Point front;
    // The point at the back of the SPC.
    private Point back;
    
    // The amount of triangles that this SPC has.
    private int amount;

    /**
     * Constructs a SpecialPointsConsumable at a given location when it's a given level.
     * The level decides the amount of sides.
     * @param center The center of this SpecialPointsConsumable.
     * @param level The level during which this SpecialPointsConsumable is created.
     */
    public SpecialPointsConsumable(Point center, int level) {
        super(center, (level+3)*2, level+3, Math.PI / 90);
        amount = level+3;
    }

    /**
     * Despawns this SpecialPointsConsumable.
     */
    public void despawn() {
        eaten = true;
    }
    
    /**
     * Updates the level for this SpecialPointsConsumable 
     * and thereby updates the amount of sides.
     * @param level The current level.
     */
    public void updateLevel(int level) {
        amount = level+3;
        t = new Triangle[2*amount];
        p = new Point[amount];
        generate();
    }
    
    // Generates a part of a circle. Used to find the amount that the angle for each of the points is changed by.
    private double c_part() {
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
            double cpitch = (cyaw%(c_part())-c_part()) + C_QUARTER + (amount/2-0.5)*c_part();
            for(int i = 0; i < amount; i++) {
                p[i] = new Point(Vector.fromPolarTransform(cyaw, cpitch, 0.5));
                cpitch -= c_part();
            }
            front = new Point(Vector.fromPolarTransform(cyaw+C_QUARTER, 0, 0.2));
            back = new Point(Vector.fromPolarTransform(cyaw-C_QUARTER, 0, 0.2));
            for (int i = 0; i < amount; i++) {
                t[2*i] = new Triangle(front, p[i], p[(i+((amount+1)/3))%amount], colors[(((-i-((int) (cyaw/c_part())))%amount+amount)%amount)%colors.length]);
                t[2*i+1] = new Triangle(back, p[i], p[(i+((amount+1)/3))%amount], colors[(((-i-((int) (cyaw/c_part()))+1)%amount+amount)%amount)%colors.length]);
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
