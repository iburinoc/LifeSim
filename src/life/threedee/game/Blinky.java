package life.threedee.game;

public class Blinky extends Ghost{
    private final Location corner;
    private boolean cruiseElroy;

    public Blinky(Game game){
        this(new Location(0, 3.5), 1, game);
    }

    public Blinky(Location location, int direction, Game game){
        super(location, direction, game, 0);
        corner = new Location(11.5, 18.5);
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0){
            return corner;
        }
        return game.getPlayer().getLoc();
    }
}
