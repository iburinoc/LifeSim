package life.threedee.game;

import life.threedee.Point;
import life.threedee.Vector;

public class Pinky extends Ghost{
    private final Location corner;

    public Pinky(Game game){
        corner = new Location(-11.5, 18.5);
        super.location = new Location(0, 0.5);
        super.direction = 2;
        super.game = game;
    }

    public Pinky(Location location, int direction, Game game){
        corner = new Location(-11.5, 18.5);
        super.location = location;
        super.direction = direction;
        super.game = game;
    }

    public Location findTarget(){
        if (eaten){
            return eyesTarget;
        } else if (game.getMode() == 0){
            return corner;
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