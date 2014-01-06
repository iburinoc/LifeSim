package life.threedee.game;

public class Blinky extends Ghost{
    private boolean cruiseElroy;

    public Blinky(Game game){
        this(GameUtilities.GHOST_LOCATIONS[0], 1, game);
    }

    public Blinky(Location location, int direction, Game game){
        super(location, direction, game, 0);
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0){
            return GameUtilities.GHOST_CORNERS[0];
        }
        return game.getPlayer().getLoc();
    }
}
