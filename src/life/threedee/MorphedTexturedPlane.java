package life.threedee;

import java.awt.image.BufferedImage;

public class MorphedTexturedPlane extends TexturedPlane {
    /**
     * Note: p is the bottom left corner of the texture, so keep that in mind.
     * @param p
     * @param n
     * @param texture
     */
    public MorphedTexturedPlane(Point p, Vector n, BufferedImage texture, double scalingFactor){
        super(p, n, texture);
        
    }
}
