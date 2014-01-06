package life.threedee;

import static life.threedee.game.GameUtilities.PX_METER;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class HalvedTrapezoidalTexturedPlane extends TexturedPlane {
    public HalvedTrapezoidalTexturedPlane(Point p, Vector n, BufferedImage texture){
        super(p, n, texture);
    }
    
    public Color c(Point inter) {
        Vector p = new Vector(this.origin, inter);
        double du = p.dotProduct(super.up);
        double dr = p.dotProduct(right);
        int py = texture.getHeight() - (int) (du * PX_METER);
        double pv = (2*h-py)/2*h;
        int px = (int) (dr * PX_METER * pv);
        if(px >= 0 && px < w && py >= 0 && py < h) {
            try{
                return new Color(texture.getRGB(px, py));
            }
            catch(ArrayIndexOutOfBoundsException e) {
            }
        }
        return null;
    }
}
