package life.threedee.game;

import life.threedee.Point;
import life.threedee.Vector;

public class Inky extends Ghost{
    private final Location corner;

    public Inky(Game game){
        this(new Location(-2, 0.5), 0, game);
    }

    public Inky(Location location, int direction, Game game){
        super(location, direction, game, 1);
        corner = new Location(13.5, -16.5);
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0){
            return corner;
        }
        Vector dir = game.getPlayer().getDir();
        double yaw = dir.polarTransform()[0];
        Point tar = new Point(new Vector(game.getPlayer().getLocPoint()).add(dir.scalarProduct(2)));
        if (yaw > Math.PI / 4 && yaw < 3 * Math.PI / 4){
            tar = tar.add(new Point(Vector.fromPolarTransform(yaw + Math.PI / 2, 0, 2)));
        }
        Location blinkyPosition = game.getGhosts().get(0).getLocation();
        return new Location(2 * tar.x - blinkyPosition.x, 2 * tar.z - blinkyPosition.z);
    }
}
