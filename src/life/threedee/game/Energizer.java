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
    
    private Point[] points;
    private Point top, bottom;
    private Triangle[] triangles;
    private boolean eaten;
    
    private Point center;
    
    public Energizer(Point center) {
        this.center = center;
        this.eaten = false;
        points = new Point[4];
        triangles = new Triangle[8];
        generate();
        //id = idCount++;
    }
    
    private void generate() {
        double cyaw = yaw;
        for(int i = 0; i < 4; i++) {
            points[i] = new Point(Vector.fromPolarTransform(cyaw, 0, 0.5/Math.sqrt(2.0)));
            cyaw += C_QUARTER;
        }
        top = new Point(Vector.fromPolarTransform(0, C_QUARTER, 0.5/Math.sqrt(2.0)));
        bottom = new Point(Vector.fromPolarTransform(0, -C_QUARTER, 0.5/Math.sqrt(2.0)));
        for (int i = 0; i < 4; i++) {
            triangles[2*i] = new Triangle(top, points[i], points[(i+1)%4]);
            triangles[2*i+1] = new Triangle(bottom, points[i], points[(i+1)%4]);
        }
        translate(new Vector(center.x, 0.625, center.y));
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
        return Math.min(Math.min(Math.min(triangles[0].calculateT(v, p), triangles[1].calculateT(v, p)), Math.min(triangles[2].calculateT(v, p), triangles[3].calculateT(v, p))), Math.min(Math.min(triangles[4].calculateT(v, p), triangles[5].calculateT(v, p)), Math.min(triangles[6].calculateT(v, p), triangles[7].calculateT(v, p))));
    }

    @Override
    public TColorTransfer getRData(Vector v, Point p, double minT) {
        if(eaten) {
            return new TColorTransfer(Double.NaN, null, null);
        }
        for(Triangle t : this.triangles) {
            double ct = t.calculateT(v, p, minT);
            if(ct == ct && !(ct > minT)) {
                return new TColorTransfer(ct, c(), this);
            }
        }
        return new TColorTransfer(Double.NaN, null, null);
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
        for (Triangle tri: triangles) {
            tri.translate(v);
        }
    }

    @Override
    public Color c() {
        return Color.WHITE;
    }

    @Override
    public boolean sameSide(Point a, Point b) {
        // TODO Auto-generated method stub
        return true;

    }
}
