package life.threedee.game;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;

public class Energizer extends Consumable {
    private static final double C_QUARTER = Math.PI/2;
    
    private Point top;
    private Point bottom;
    
    public Energizer(Point center) {
    	super(center, 8, 4, Math.PI / 360);
    }
    
    protected void generate() {
        double cyaw = yaw;
        for(int i = 0; i < 4; i++) {
            p[i] = new Point(Vector.fromPolarTransform(cyaw, 0, 0.5/Math.sqrt(2.0)));
            cyaw += C_QUARTER;
        }
        top = new Point(Vector.fromPolarTransform(0, C_QUARTER, 0.5/Math.sqrt(2.0)));
        bottom = new Point(Vector.fromPolarTransform(0, -C_QUARTER, 0.5/Math.sqrt(2.0)));
        for (int i = 0; i < 4; i++) {
            t[2*i] = new Triangle(top, p[i], p[(i+1)%4]);
            t[2*i+1] = new Triangle(bottom, p[i], p[(i+1)%4]);
        }
        translate(new Vector(center.x, 0.625, center.z));
    }

    public void eat(Game g, Player p){
        eaten = true;
        g.startFrightened();
        p.stop(3);
    }     

    @Override
    public Color c() {
        return Color.WHITE;
    }
}
