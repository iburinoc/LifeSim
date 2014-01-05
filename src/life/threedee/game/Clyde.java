package life.threedee.game;

public class Clyde extends Ghost{
    private final Location corner;

    public Clyde(Game game){
        corner = new Location(-13.5, -16.5);
        super.location = new Location(2, 0.5);
        super.direction = 0;
        super.game = game;
    }

    public Clyde(Location location, int direction, Game game){
        corner = new Location(-13.5, -16.5);
        super.location = location;
        super.direction = direction;
        super.game = game;
    }

    public Location findTarget(){
        if (location.distanceTo(game.getPlayer().getLoc()) <= 64){
            return game.getPlayer().getLoc();
        }
        return corner;
    }
}
