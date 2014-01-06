package life.threedee.game;

public class Blinky extends Ghost{
    private boolean cruiseElroy;

    public Blinky(Game game){
        this(GameUtilities.GHOST_LOCATIONS[0], GameUtilities.GHOST_ORIENTATIONS[0], game);
    }

    public Blinky(Location location, int direction, Game game){
        super(location, direction, game, 0);
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0 && !cruiseElroy){
            return GameUtilities.GHOST_CORNERS[0];
        }
        return game.getPlayer().getLoc();
    }
}
