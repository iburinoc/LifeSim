package life.threedee.game;

import life.threedee.Point;
import life.threedee.Vector;

public class Pinky extends Ghost{
    public Pinky(Game game){
        this(GameUtilities.GHOST_LOCATIONS[1], 2, game);
    }

    public Pinky(Location location, int direction, Game game){
        super(location, direction, game, 1);
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0){
            return GameUtilities.GHOST_CORNERS[1];
        }
        Vector dir = game.getPlayer().getDir();
        double yaw = dir.polarTransform()[0];
        Point tar = new Point(new Vector(game.getPlayer().getLocPoint()).add(dir.scalarProduct(4)));
        if (yaw > Math.PI / 4 && yaw < 3 * Math.PI / 4){
            tar = tar.add(new Point(Vector.fromPolarTransform(yaw + Math.PI / 2, 0, 4)));
        }
        return new Location(tar.x, tar.z);
    }
}
