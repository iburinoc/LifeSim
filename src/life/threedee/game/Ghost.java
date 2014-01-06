package life.threedee.game;

public abstract class Ghost implements Tickable{
    protected Location location, target;
    protected final Location eyesTarget = new Location(-0.5, 3.5);
    protected int direction;
    protected boolean uTurn, eaten;
    protected Game game;

    public int makeDecision(boolean[] open){
        if (uTurn){
            return (direction + 2) % 4;
        }
        target = findTarget();
        open[(direction + 2) % 4] = false;
        double shortest = Double.MAX_VALUE;
        int toReturn = 0;
        for (int i = 3; i >= 0; i--){
            Location choice = new Location(location.x + (i == 1 ? -1 : (i == 3 ? 1 : 0)), location.z + (i == 0 ? 1 : (i == 2 ? -1 : 0)));
            if (open[i] && choice.distanceTo(target) <= shortest){
                shortest = choice.distanceTo(target);
                toReturn = i;
            }
        }
        return toReturn;
    }

    public abstract Location findTarget();

    public void tick(int delta){
    }

    public Location getLocation(){
        return location;
    }
}
