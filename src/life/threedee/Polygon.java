package life.threedee;

import java.awt.*;

public class Polygon extends Plane{
    private Point[] vertices;

    Polygon(Point[] vertices, Point3D origin, Vector normal) {
        super (origin, normal);
        this.vertices = vertices;
    }

    Polygon(Point[] vertices, Point3D origin, Vector normal, Color colour) {
        super (origin, normal, colour);
        this.vertices = vertices;
    }
}
