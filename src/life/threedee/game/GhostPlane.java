package life.threedee.game;

import static life.threedee.game.GameUtilities.PX_METER;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import life.threedee.Point;
import life.threedee.TexturedPlane;
import life.threedee.Vector;
import life.threedee.game.GameUtilities;

public class GhostPlane extends TexturedPlane {
    private int offset, ghostNum;
    private boolean facePlane;
    
    public GhostPlane(Point o, Point r, Point u, int ghostNum) {
        this(o,new Vector(o, u).crossProduct(new Vector(o, r)), ghostNum);
    }
    
    public GhostPlane(Point p, Vector n, int ghostNum) {
        super(p, n, GameUtilities.GHOST_SIDE_TEXTURES[ghostNum]);
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
    
    public void setFace(boolean face) {
        this.facePlane=face;
        reloadTexture();
    }
    
    public void setGhostNum(int ghostNum) {
        this.ghostNum = ghostNum;
        reloadTexture();
    }
    
    public void reloadTexture() {
        if(facePlane) {
            this.texture = GameUtilities.GHOST_FACE_TEXTURES[ghostNum];
        } else {
            this.texture = GameUtilities.GHOST_SIDE_TEXTURES[ghostNum];
        }
    }
    
    public void shiftTexture() {
        offset++; 
        offset %= 10;
    }
}
