package life.threedee.game;

import static life.threedee.game.GameUtilities.PX_METER;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TexturedPlane;
import life.threedee.Vector;

/**
 * The graphical element of the side of a ghost.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class GhostPlane extends TexturedPlane {
    private int offset, ghostNum;
    private boolean facePlane;
    
    /**
     * Constructs a GhostPlane with 3 given points, 
     * and the number of the ghost that this plane is a part of.
     * @param origin The origin point. Used in the same manner as in a TexturedPlane.
     * @param right The "right" point.
     * @param up The "up" point.
     * @param ghostNum The number of ghost for which this plane is a side.
     */
    public GhostPlane(Point origin, Point right, Point up, int ghostNum) {
        this(origin,new Vector(origin, up).crossProduct(new Vector(origin, right)), ghostNum);
    }
    
    /**
     * Constructs a GhostPlane with a given point, a normal vector,
     * and the number of the ghost that this plane is a part of.
     * @param point The origin point. Used in the same manner as in a TexturedPlane.
     * @param normal The normal vector.
     * @param ghostNum The number of ghost for which this plane is a side.
     */
    public GhostPlane(Point point, Vector normal, int ghostNum) {
        super(point, normal, GameUtilities.GHOST_SIDE_TEXTURES[ghostNum]);
        this.offset = 1;
        this.ghostNum=ghostNum;
        this.facePlane=false;
    }
    
    public Color c(Point inter) {
        Vector p = new Vector(this.origin, inter);
        double du = p.dotProduct(super.up);
        double dr = p.dotProduct(super.right)+0.5;
        int py = texture.getHeight() - (int) (du * PX_METER);
        double pv = (((double)super.h)+((double)py))/(2*((double)super.h));
        if (GameUtilities.SCARY_FACES) {
            double pxPrime = (dr * PX_METER / pv + super.w/2);
            int px = (int) (dr * PX_METER+ super.w/2);
            if(pxPrime >= 0 && pxPrime < super.w && py >= 0 && py < super.h) {
                try{
                    if (py > super.h-7) {
                        return new Color(super.texture.getRGB((px+offset)%20, py), true);
                    } else {
                        return new Color(super.texture.getRGB(px, py), true);
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                }
            }
        } else {
            int px = (int) (dr * PX_METER / pv + super.w/2);
            if(px >= 0 && px < super.w && py >= 0 && py < super.h) {
                try{
                    if (py > super.h-6) {
                        return new Color(super.texture.getRGB((px+offset)%20, py), true);
                    } else {
                        return new Color(super.texture.getRGB(px, py), true);
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        return null;
    }
    
    /**
     * Sets whether this plane is the face of the ghost or not.
     * @param face Whether or not this plane is the face of the ghost.
     */
    public void setFace(boolean face) {
        this.facePlane=face;
        reloadTexture();
    }
    
    /** 
     * Sets the number of the ghost that this plane is a part of.
     * @param ghostNum The number of the ghost.
     */
    public void setGhostNum(int ghostNum) {
        this.ghostNum = ghostNum;
        reloadTexture();
    }
    
    /** 
     * Reloads the texture. Used for when the ghost changes color or rotates.
     */
    public void reloadTexture() {
        if(facePlane) {
            this.texture = GameUtilities.GHOST_FACE_TEXTURES[ghostNum];
        } else {
            this.texture = GameUtilities.GHOST_SIDE_TEXTURES[ghostNum];
        }
    }
    
    /**
     * Shifts the bottom of the ghost.
     */
    public void shiftTexture() {
        offset++; 
        offset %= 10;
    }
}
