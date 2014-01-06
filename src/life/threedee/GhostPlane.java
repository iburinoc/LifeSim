package life.threedee;

import static life.threedee.game.GameUtilities.PX_METER;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import life.threedee.game.GameUtilities;

public class GhostPlane extends TexturedPlane {
    private int textureNum, ghostNum;
    
    public GhostPlane(Point p, Vector n, int ghostNum) {
        super(p, n, GameUtilities.loadImage("resources/Ghost"+ghostNum+"Side1.png"));
        this.textureNum = 1;
        this.ghostNum=ghostNum;
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
                if (py > super.h-7) {
                    return new Color(super.texture.getRGB((px+textureNum)%20, py), true);
                } else {
                    return new Color(super.texture.getRGB(px, py), true);
                }
            }
            catch(ArrayIndexOutOfBoundsException e) {
            }
        }
        return null;
    }
    
    public void shiftTexture() {
        textureNum++; 
        textureNum %= 10;
        //this.setTexture(GameUtilities.loadImage("./resources/Ghost"+ghostNum+"Side"+textureNum+".png"));
    }
}
