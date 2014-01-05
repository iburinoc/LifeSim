package life.threedee.game;

public abstract class Ghost{
    protected Location location, target;
    protected int direction;

    public Location makeDecision(boolean[] open){
        open[direction] = false;
        double shortest = Double.MAX_VALUE;
        Location next = null;
        for (int i = 3; i >= 0; i--){
            Location current = new Location(location.x + (i == 1 ? -1 : (i == 3 ? 1 : 0)), location.z + (i == 0 ? 1 : (i == 2 ? -1 : 0)));
            if (open[i] && current.distanceTo(target) <= shortest){
                next = current;
                shortest = next.distanceTo(target);
            }
        }
        return next;
    }
}
