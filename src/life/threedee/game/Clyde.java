package life.threedee.game;

public class Clyde extends Ghost{
    public Clyde(Game game){
        this(GameUtilities.GHOST_LOCATIONS[3], GameUtilities.GHOST_ORIENTATIONS[3], game);
    }

    public Clyde(Location location, int direction, Game game){
        super(location, direction, game, 3);
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0){
            return GameUtilities.GHOST_CORNERS[3];
        }
        if (location.distanceTo(game.getPlayer().getLoc()) > 64){
            return game.getPlayer().getLoc();
        }
        return GameUtilities.GHOST_CORNERS[3];
    }
}
