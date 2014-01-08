package life.threedee.game;

import static life.threedee.game.GameUtilities.idCount;

import java.awt.Color;

import life.threedee.Point;
import life.threedee.TColorTransfer;
import life.threedee.TexturedPlane;
import life.threedee.ThreeDeeObject;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.game.maps.MapFeature;

public class Energizer implements ThreeDeeObject, Tickable, MapFeature{
    private static final double A_INC = Math.PI / 360;
    private static final double C_QUARTER = Math.PI/2;
    
    double yaw;
    
    private Point[] lowerPoints, upperPoints;
    private TexturedPlane[] walls;
    private TexturedPlane ceiling;
    
    private Point center;
    
    public Energizer(Point center) {
        this.center = center;
        lowerPoints = new Point[4];
        upperPoints = new Point[4];
        walls = new TexturedPlane[4];
        generate();
        //id = idCount++;
    }
    
    private void generate() {
        double cyaw = yaw;
        for(int i = 0; i < 4; i++) {
            lowerPoints[i] = new Point(Vector.fromPolarTransform(cyaw, 0, 1/Math.sqrt(2.0)));
            lowerPoints[i].stretch(1/Math.sqrt(2.0));
            upperPoints[i] = lowerPoints[i].add(new Point(0, 1, 0));
            cyaw += C_QUARTER;
        }
        for(int i = 0;i < 4; i++) {
            Vector n = new Vector(lowerPoints[i], lowerPoints[(i+1)%4]).crossProduct(new Vector(lowerPoints[i], upperPoints[i]));
            walls[i] = new TexturedPlane(lowerPoints[i], n, GameUtilities.ENERGIZER_SIDE_TEXTURE);
        }
        Vector n = new Vector(upperPoints[2], upperPoints[3]).crossProduct(new Vector(upperPoints[2], upperPoints[1]));
        ceiling = new TexturedPlane(upperPoints[2], n, GameUtilities.ENERGIZER_TOP_TEXTURE);
//        translate(new Vector(center.x, 0, center.z));
        translate(new Vector(0, 1, 0));
    }

    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void tick() {
        yaw += A_INC;
        generate();
    }

    @Override
    public double calculateT(Vector v, Point p) {
        return Math.min(Math.min(walls[0].calculateT(v, p), walls[1].calculateT(v, p)), Math.min(Math.min(walls[2].calculateT(v, p), walls[3].calculateT(v, p)), ceiling.calculateT(v, p)));
    }

    @Override
    public TColorTransfer getRData(Vector v, Point p, double minT) {
//        if(eaten) {
//            return new TColorTransfer(Double.NaN, null, null);
//        }
        TexturedPlane closest = null;
        double min = Double.MAX_VALUE;
        for(TexturedPlane wall : this.walls) {
            double ct = wall.calculateT(v, p, Math.min(minT, min));
            if(ct == ct && ct < min) {
                closest = wall;
                min = ct;
            }
        }
        double ct = ceiling.calculateT(v, p, Math.min(minT, min));
        if(ct == ct && ct < min) {
            closest = ceiling;
            min = ct;
        }
        if(!(min > minT)) {
            return closest.getRData(v, p, minT);
        } else {
            return new TColorTransfer(Double.NaN, null, null);
        }
    }

    @Override
    public Point intersection(Vector v, Point p) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point intersection(Vector v, Point p, double t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void translate(Vector v) {
        for (TexturedPlane tp: walls) {
            tp.translate(v);
        }
        ceiling.translate(v);
    }

    @Override
    public Color c() {
        // TODO Auto-generated method stub
        return Color.WHITE;
    }

    @Override
    public boolean sameSide(Point a, Point b) {
        // TODO Auto-generated method stub
        return false;
    }
}
