package life.threedee.game;

public class Clyde extends Ghost{
    private final Location corner;

    public Clyde(Game game){
        this(new Location(2, 0.5), 0, game);
    }

    public Clyde(Location location, int direction, Game game){
        super(location, direction, game, 3);
        corner = new Location(-13.5, -16.5);
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0){
            return corner;
        }
        if (location.distanceTo(game.getPlayer().getLoc()) > 64){
            return game.getPlayer().getLoc();
        }
        return corner;
    }
}
