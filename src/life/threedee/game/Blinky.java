package life.threedee.game;

public class Blinky extends Ghost{
    private final Location corner;

    public Blinky(Game game){
        corner = new Location(11.5, 18.5);
        super.location = new Location(0, 3.5);
        super.direction = 1;
        super.game = game;
    }

    public Blinky(Location location, int direction, Game game){
        corner = new Location(11.5, 18.5);
        super.location = location;
        super.direction = direction;
        super.game = game;
    }

    public Location findTarget(){
        return game.getPlayer().getLoc();
    }
}
