package life.threedee.game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import life.threedee.GhostPlane;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.WorldObject;

public class GhostModelFactory {
    public static WorldObject generateGhostModel (int color) {
        Point top = new Point(0.0, 2.0, 0.0);
        Point zP = new Point(0.5, 1.5, 0.5);
        Point xP = new Point(0.5, 1.5, -0.5);
        Point zM = new Point(-0.5, 1.5, -0.5);
        Point xM = new Point(-0.5, 1.5, 0.5);
        Point lZP = new Point(1, 0.0, 1);
        Point lXP = new Point(1, 0.0, -1);
        Point lZM = new Point(-1, 0.0, -1);
        Point lXM = new Point(-1, 0.0, 1);
        Triangle t1 = new Triangle(top, zP, xP, Color.RED);
        Triangle t2 = new Triangle(top, xP, zM, Color.RED);
        Triangle t3 = new Triangle(top, zM, xM, Color.RED);
        Triangle t4 = new Triangle(top, xM, zP, Color.RED);
        BufferedImage texture = null;
        try {
            texture = ImageIO.read(new File("./resources/GhostSide1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        GhostPlane http1 = new GhostPlane(lZP, new Vector(lZP, lXM).crossProduct(new Vector(lZP, zP)));
        GhostPlane http2 = new GhostPlane(lXP, new Vector(lXP, lZP).crossProduct(new Vector(lXP, xP)));
        GhostPlane http3 = new GhostPlane(lZM, new Vector(lZM, lXP).crossProduct(new Vector(lZM, zM)));
        GhostPlane http4 = new GhostPlane(lXM, new Vector(lXM, lZM).crossProduct(new Vector(lXM, xM)));
        WorldObject wo = new WorldObject(new ThreeDeeObject[] {t1, t2, t3, t4, http1, http2, http3, http4}, new Point(0.0, 1.0, 0.0));
        wo.translate(new Vector(0.0, 0.0, 3.0));
        return wo;
    }
}
