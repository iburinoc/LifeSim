package life.threedee;

public class Polygon extends Plane{
    private Point[] vertices;

    Polygon(Point[] vertices) {
        super (vertices[0], vertices[1], vertices[2]);
        this.vertices = vertices;
    }
}
