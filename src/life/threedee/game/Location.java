package life.threedee.game;

public class Location {
    public final int x, z;

    Location(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public double distanceTo(Location target){
        return (target.x - this.x) * (target.x - this.x) + (target.z - this.z) * (target.z - this.z);
    }
}
