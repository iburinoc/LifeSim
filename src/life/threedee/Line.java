package life.threedee;

public class Line{

    private double m, b;

    public Line(double m, double b) {
        this.m = m;
        this.b = b;
    }

    public boolean over (Point point){
        return point.y > m * point.x + b;
    }
}
