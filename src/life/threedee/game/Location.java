package life.threedee.game;

public class Location {
    public final double x, z;

    Location(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public double distanceTo(Location target){
        return (target.x - this.x) * (target.x - this.x) + (target.z - this.z) * (target.z - this.z);
    }
}
