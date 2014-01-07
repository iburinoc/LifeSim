package life.threedee.game;

import life.threedee.GhostPlane;
import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;

public class Ghost implements Tickable{
    protected Location location, target;
    protected final Location eyesTarget = new Location(-0.5, 3.5);
    // ghostNum is the current state of the ghost (0-7). This is used to decide textures, behaviour, etc.
    // 0 - Blinky
    // 1 - Pinky
    // 2 - Inky
    // 3 - Clyde
    // 4 - Scared
    // 5 - ScaredFlashing
    // 6 - Cruise Elroy
    // 7 - Cruise Elroy MK. II
    // ghostId is the true id of the ghost. It should be from (0-3). This is used to remember who the ghost is upon exiting frightened mode.
    protected int direction, ghostNum, ghostId;
    protected boolean uTurn, eaten;
    protected Game game;
    protected GhostPlane[] facePlanes;
    protected Triangle[] faceTriangles;

    public Ghost(Game g, int ghostNum) {
        this.ghostNum=ghostNum;
        this.ghostId=ghostNum;
        Point top = new Point(0.0, 2.0, 0.0);
        Point zPlusXPlus = new Point(0.25, 1.5, 0.25);
        Point zMinusXPlus = new Point(0.25, 1.5, -0.25);
        Point zMinusXMinus = new Point(-0.25, 1.5, -0.25);
        Point zPlusXMinus = new Point(-0.25, 1.5, 0.25);
        Point lowerZPlusXPlus = new Point(0.5, 0.0, 0.5);
        Point lowerZMinusXPlus = new Point(0.5, 0.0, -0.5);
        Point lowerZMinusXMinus = new Point(-0.5, 0.0, -0.5);
        Point lowerZPlusXMinus = new Point(-0.5, 0.0, 0.5);
        facePlanes = new GhostPlane[4];
        faceTriangles = new Triangle[4];
        faceTriangles[0] = new Triangle(top, zPlusXPlus, zPlusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        faceTriangles[1] = new Triangle(top, zPlusXPlus, zMinusXPlus, GameUtilities.GHOST_COLORS[ghostNum]);
        faceTriangles[2] = new Triangle(top, zMinusXPlus, zMinusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        faceTriangles[3] = new Triangle(top, zMinusXMinus, zPlusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        facePlanes[0] = new GhostPlane(lowerZPlusXPlus, new Vector(lowerZPlusXPlus, zPlusXPlus).crossProduct(new Vector(lowerZPlusXPlus, lowerZMinusXPlus)), ghostNum);
        facePlanes[1] = new GhostPlane(lowerZMinusXPlus, new Vector(lowerZMinusXPlus, zMinusXPlus).crossProduct(new Vector(lowerZMinusXPlus, lowerZMinusXMinus)), ghostNum);
        facePlanes[2] = new GhostPlane(lowerZMinusXMinus, new Vector(lowerZMinusXMinus, zMinusXMinus).crossProduct(new Vector(lowerZMinusXMinus, lowerZPlusXMinus)), ghostNum);
        facePlanes[3] = new GhostPlane(lowerZPlusXMinus, new Vector(lowerZPlusXMinus, zPlusXMinus).crossProduct(new Vector(lowerZPlusXMinus, lowerZPlusXPlus)), ghostNum);
        for (int i = 0; i < 4; i++) {
            g.addObject(faceTriangles[i]);
            g.addObject(facePlanes[i]);
        }
        g.addTickable(this);
    }
    
    public Ghost(Location location, int direction, Game game, int ghostNum) {
        this(game, ghostNum);
        this.location = location;
        this.direction = direction;
        this.game = game;
        facePlanes[direction].setFace(true);
    }
    
    public int makeDecision(boolean[] open){
        if (uTurn){
            return (direction + 2) % 4;
        }
        target = findTarget();
        open[(direction + 2) % 4] = false;
        double shortest = Double.MAX_VALUE;
        int toReturn = 0;
        for (int i = 3; i >= 0; i--){
            Location choice = new Location(location.x + (i == 1 ? -1 : (i == 3 ? 1 : 0)), location.z + (i == 0 ? 1 : (i == 2 ? -1 : 0)));
            if (open[i] && choice.distanceTo(target) <= shortest){
                shortest = choice.distanceTo(target);
                toReturn = i;
            }
        }
        return toReturn;
    }

    public Location findTarget() {
        switch(ghostNum) {
        case 0:
        {
            if (eaten){
                return eyesTarget;
            } else if (game.getMode() == 0){
                return GameUtilities.GHOST_CORNERS[0];
            }
            return game.getPlayer().getLoc();
        }
        case 1:
        {
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
        case 2:
        {
            if (eaten){
                return eyesTarget;
            } else if (game.getMode() == 0){
                return GameUtilities.GHOST_CORNERS[2];
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
        case 3:
        {
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
        case 4:
        case 5:
            return null;
        case 6:
        case 7:
            if (eaten){
                return eyesTarget;
            }
            return game.getPlayer().getLoc();
        default:
            return null;
        }
    }

    public void tick(){
        for (int i = 0; i < 4; i++) {
            facePlanes[i].shiftTexture();
        }
        // We'll need to rework this 
        if (ghostId == 0 && GameUtilities.GAME_DATA[game.getLevel()][3]==game.getDotsRemaining()) {
            ghostNum = 6;
            facePlanes[direction].setTexture(GameUtilities.GHOST_FACE_TEXTURES[ghostNum]);
            facePlanes[direction+1%4].setTexture(GameUtilities.GHOST_SIDE_TEXTURES[ghostNum]);
            facePlanes[direction+2%4].setTexture(GameUtilities.GHOST_SIDE_TEXTURES[ghostNum]);
            facePlanes[direction+3%4].setTexture(GameUtilities.GHOST_SIDE_TEXTURES[ghostNum]);
        }
    }
    
    public void move(Vector v) {
        //ANDREY, PUT MOVEMENT CODE HERE!
        for (int i = 0; i < 4; i++) {
            facePlanes[i].translate(v);
            faceTriangles[i].translate(v);
        }
    }

    public Location getLocation(){
        return location;
    }
}
