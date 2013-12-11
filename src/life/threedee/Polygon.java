package life.threedee;

import java.awt.*;

public class Polygon extends Plane{
    private Point[] vertices;
    private Vector up;

    public Polygon(Point[] vertices, Vector up, Point3D origin, Vector normal) {
        super (origin, normal);
        this.vertices = vertices;
        this.up = up;
    }

    public Polygon(Point[] vertices, Vector up, Point3D origin, Vector normal, Color colour) {
        super (origin, normal, colour);
        this.vertices = vertices;
        this.up = up;
    }

    public boolean inside(Point point) {
        boolean inside = true;
        for (int i = 0; i < vertices.length; i++) {
            try {
                double m = (vertices[i].y - vertices[(i + 1) % vertices.length].y) / (vertices[i].x - vertices[(i + 1) % vertices.length].x);
                double b = vertices[i].y - m * vertices[i].x;
                boolean above = vertices[(i + 2) % vertices.length].y > m * vertices[(i + 2) % vertices.length].x + b;
                if (above != point.y > m * point.x + b) {
                    inside = false;
                }
            } catch (ArithmeticException  e) {
                boolean right = vertices[(i + 2) % vertices.length].x > vertices[i].x;
                if (right != point.x > vertices[i].x) {
                    inside = false;
                }
            }
        }
        return inside;
    }

    @Override
    public Point3D intersection(Vector vector, Point3D point3D) {
        double t = super.calculateT(vector, point3D);
        double nx = point3D.x + vector.x * t;
        double ny = point3D.y + vector.y * t;
        double nz = point3D.z + vector.z * t;
        return inside(new Point(nx, ny)) ? new Point3D(nx, ny, nz) : null;
    }
}
