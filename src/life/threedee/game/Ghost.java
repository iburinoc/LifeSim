package life.threedee.game;

import static life.threedee.game.GameUtilities.BLINKY;
import static life.threedee.game.GameUtilities.CLYDE;
import static life.threedee.game.GameUtilities.CRUISE_ELROY;
import static life.threedee.game.GameUtilities.CRUISE_ELROY_2;
import static life.threedee.game.GameUtilities.EATEN;
import static life.threedee.game.GameUtilities.INKY;
import static life.threedee.game.GameUtilities.PINKY;
import static life.threedee.game.GameUtilities.SCARED;
import static life.threedee.game.GameUtilities.SCARED_FLASHING;

import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;

public class Ghost implements Tickable{
    protected Point location, target;
    protected final Point eyesTarget = new Point(-0.5, 1, 3.5);
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
    protected boolean uTurn;
    protected Game game;
    protected GhostPlane[] facePlanes;
    protected Triangle[] faceTriangles;

    public Ghost(Game g, int ghostNum) {
        this.game=g;
        this.ghostNum=ghostNum;
        this.ghostId=ghostNum;
        this.location=GameUtilities.GHOST_LOCATIONS[ghostNum];
        this.direction=GameUtilities.GHOST_ORIENTATIONS[ghostNum];
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
        facePlanes[0] = new GhostPlane(lowerZPlusXPlus, lowerZMinusXPlus, zPlusXPlus, ghostNum);
        facePlanes[1] = new GhostPlane(lowerZMinusXPlus, lowerZMinusXMinus, zMinusXPlus, ghostNum);
        facePlanes[2] = new GhostPlane(lowerZMinusXMinus, lowerZPlusXMinus, zMinusXMinus, ghostNum);
        facePlanes[3] = new GhostPlane(lowerZPlusXMinus, lowerZPlusXPlus, zPlusXMinus, ghostNum);
        for (int i = 0; i < 4; i++) {
            g.addObject(faceTriangles[i]);
            g.addObject(facePlanes[i]);
        }
        facePlanes[direction].setFace(true);
        g.addTickable(this);
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
            Point choice = new Point(location.x + (i == 1 ? -1 : (i == 3 ? 1 : 0)), 1, location.z + (i == 0 ? 1 : (i == 2 ? -1 : 0)));
            if (open[i] && new Vector(choice, target).s() <= shortest){
                shortest = new Vector(choice, target).s();
                toReturn = i;
            }
        }
        return toReturn;
    }

    public Point findTarget() {
        switch(ghostNum) {
        case BLINKY:
        {
            if (game.getMode() == 0){
                return GameUtilities.GHOST_CORNERS[0];
            }
            return game.getPlayer().getLoc();
        }
        case PINKY:
        {
            if (game.getMode() == 0){
                return GameUtilities.GHOST_CORNERS[1];
            }
            Vector dir = game.getPlayer().getDir();
            double yaw = dir.polarTransform()[0];
            Point tar = new Point(new Vector(game.getPlayer().getLocPoint()).add(dir.scalarProduct(4)));
            if (yaw > Math.PI / 4 && yaw < 3 * Math.PI / 4){
                tar = tar.add(new Point(Vector.fromPolarTransform(yaw + Math.PI / 2, 0, 4)));
            }
            return tar;
        }
        case INKY:
        {
            if (game.getMode() == 0){
                return GameUtilities.GHOST_CORNERS[2];
            }
            Vector dir = game.getPlayer().getDir();
            double yaw = dir.polarTransform()[0];
            Point tar = new Point(new Vector(game.getPlayer().getLocPoint()).add(dir.scalarProduct(2)));
            if (yaw > Math.PI / 4 && yaw < 3 * Math.PI / 4){
                tar = tar.add(new Point(Vector.fromPolarTransform(yaw + Math.PI / 2, 0, 2)));
            }
            Point blinkyPosition = game.getGhosts().get(0).getLocation();
            return new Point(2 * tar.x - blinkyPosition.x, 1, 2 * tar.z - blinkyPosition.z);
        }
        case CLYDE:
        {
            if (game.getMode() == 0){
                return GameUtilities.GHOST_CORNERS[3];
            }
            if (new Vector(location, game.getPlayer().getLoc()).s() > 64){
                return game.getPlayer().getLoc();
            }
            return GameUtilities.GHOST_CORNERS[3];
        }
        case SCARED:
        case SCARED_FLASHING:
            return null;
        case CRUISE_ELROY:
        case CRUISE_ELROY_2:
            return game.getPlayer().getLoc();
        case EATEN:
            return null;
        default:
            return null;
        }
    }

    public void tick(){
        for (int i = 0; i < 4; i++) {
            facePlanes[i].shiftTexture();
        }
        // We'll need to rework this.
        // Andrey, you'll need to implement ALL the rules 
        // concerning Blinky turning into his 2 Cruise Elroy forms. 
        if (ghostId == BLINKY && GameUtilities.GAME_DATA[game.getLevel()][3]==game.getDotsRemaining()) {
            ghostNum = CRUISE_ELROY;
            for (int i = 0; i < 4; i++) {
                facePlanes[(direction+i)%4].setGhostNum(ghostNum);
            }
        }
        if (ghostId == BLINKY && (ghostNum == BLINKY || ghostNum == CRUISE_ELROY) && GameUtilities.GAME_DATA[game.getLevel()][3]==game.getDotsRemaining() * 2) {
            ghostNum = CRUISE_ELROY_2;
            for (int i = 0; i < 4; i++) {
                facePlanes[(direction+i)%4].setGhostNum(ghostNum);
            }
        }
        move();
        if (Math.abs(location.x) > 14){
            translate(new Vector(-28 * Math.signum(location.x), 0, 0));
        }
    }
    
    public void move() {
        //ANDREY, PUT MOVEMENT CODE HERE!
        translate(dirToV());
    }
    
    public void translate(Vector v) {
        // TRANSLATE ALL THE POINTS/LOCATIONS HERE AS WELL. OR SHOULD THAT BE IN MOVE?
        location = location.add(new Point(v));
        for (int i = 0; i < 4; i++) {
            facePlanes[i].translate(v);
            faceTriangles[i].translate(v);
        }
    }

    public Vector dirToV(){
        return Vector.fromPolarTransform(direction < 3 ? (direction + 1) * Math.PI / 2 : 0, 0, GameUtilities.GAME_DATA[game.getLevel()][1] / 400.0);
    }

    public Point getLocation(){
        return location;
    }
}
