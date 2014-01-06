package life.threedee.game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import life.threedee.HalvedTrapezoidalTexturedPlane;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.WorldObject;

public class GhostModelFactory {
    public static WorldObject generateGhostModel (int color) {
        Point top = new Point(0.0, 2.0, 0.0);
        Point zP = new Point(0.0, 1.5, 0.5);
        Point xP = new Point(0.5, 1.5, 0.0);
        Point zM = new Point(0.0, 1.5, -0.5);
        Point xM = new Point(-0.5, 1.5, 0.0);
        Point lZP = new Point(0.0, 0.0, 0.5);
        Point lXP = new Point(0.5, 0.0, 0.0);
        Point lZM = new Point(0.0, 0.0, -0.5);
        Point lXM = new Point(-0.5, 0.0, 0.0);
        Triangle t1 = new Triangle(top, zP, xP, Color.RED);
        Triangle t2 = new Triangle(top, xP, zM, Color.RED);
        Triangle t3 = new Triangle(top, zM, xM, Color.RED);
        Triangle t4 = new Triangle(top, xM, zP, Color.RED);
        BufferedImage texture = null;
        try {
            texture = ImageIO.read(new File("./resources/kitten.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HalvedTrapezoidalTexturedPlane http1 = new HalvedTrapezoidalTexturedPlane(lZP, new Vector(lZP, lXM).crossProduct(new Vector(lZP, zP)), texture);
        HalvedTrapezoidalTexturedPlane http2 = new HalvedTrapezoidalTexturedPlane(lXP, new Vector(lXP, lZP).crossProduct(new Vector(lXP, xP)), texture);
        HalvedTrapezoidalTexturedPlane http3 = new HalvedTrapezoidalTexturedPlane(lZM, new Vector(lZM, lXP).crossProduct(new Vector(lZM, zM)), texture);
        HalvedTrapezoidalTexturedPlane http4 = new HalvedTrapezoidalTexturedPlane(lXM, new Vector(lXM, lZM).crossProduct(new Vector(lXM, xM)), texture);
        return new WorldObject(new ThreeDeeObject[] {t1, t2, t3, t4, http1, http2, http3, http4}, new Point(0.0, 1.0, 0.0));
    }
}
