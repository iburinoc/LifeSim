package life.threedee;

import java.awt.Color;

public class Polygon extends Plane{
    private Point[] vertices;
    private Vector right, up;

    public Polygon(Point[] vertices, Vector right, Vector up, Point3D origin, Vector normal) {
        super (origin, normal);
        this.vertices = vertices;
        this.right = right;
        this.up = up;
    }

    public Polygon(Point[] vertices, Vector right, Vector up, Point3D origin, Vector normal, Color colour) {
        super (origin, normal, colour);
        this.vertices = vertices;
        this.right = right;
        this.up = up;
    }

    public boolean inside(Point point) {
        boolean inside = true;
        for (int i = 0; i < vertices.length; i++) {
            try {
                Line side = new Line((vertices[i].y - vertices[(i + 1) % vertices.length].y) /
                                     (vertices[i].x - vertices[(i + 1) % vertices.length].x),
                                      vertices[i].y - (vertices[i].y - vertices[(i + 1) % vertices.length].y) /
                                     (vertices[i].x - vertices[(i + 1) % vertices.length].x) * vertices[i].x);
                if (side.over(vertices[(i + 2) % vertices.length]) != side.over(point)) {
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
        return inside(new Point(new Plane (super.intersection(vector, point3D), right).calculateT(right, super.intersection(vector, point3D)), new Plane (super.intersection(vector, point3D), up).calculateT(up, super.intersection(vector, point3D)))) ? super.intersection(vector, point3D) : null;
    }
}