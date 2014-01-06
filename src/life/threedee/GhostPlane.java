package life.threedee;

import static life.threedee.game.GameUtilities.PX_METER;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import life.threedee.game.GameUtilities;

public class GhostPlane extends TexturedPlane {
    private int textureNum;
    
    public GhostPlane(Point p, Vector n) {
        super(p, n, GameUtilities.loadImage("resources/GhostSide1.png"));
        this.textureNum = 1;
    }
    
    public Color c(Point inter) {
        Vector p = new Vector(this.origin, inter);
        double du = p.dotProduct(super.up);
        double dr = p.dotProduct(super.right)+0.5;
        int py = texture.getHeight() - (int) (du * PX_METER);
        double pv = (((double)super.h)+((double)py))/(2*((double)super.h));
        int px = (int) (dr * PX_METER / pv + super.w/2);
        /*System.out.print(":D");
        System.out.println(py);
        System.out.println(h);
        System.out.println(pv);*/
        if(px >= 0 && px < super.w && py >= 0 && py < super.h) {
            try{
                return new Color(super.texture.getRGB(px, py), true);
            }
            catch(ArrayIndexOutOfBoundsException e) {
            }
        }
        return null;
    }
    
    public void shiftTexture() {
        textureNum++; 
        if (textureNum == 21) {
            textureNum = 1;
        }
        this.setTexture(GameUtilities.loadImage("./resources/GhostSide"+textureNum+".png"));
    }
}
