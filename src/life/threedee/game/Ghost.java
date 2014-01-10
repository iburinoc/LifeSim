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
import life.threedee.game.maps.MapLocation;

public class Ghost implements Tickable{
    protected Point location, target;
    protected final Point eyesTarget = new Point(-0.5, 1, 3.5);
    // ghostNum is the current state of the ghost (0-8). This is used to decide textures, behaviour, etc.
    // 0 - Blinky
    // 1 - Pinky
    // 2 - Inky
    // 3 - Clyde
    // 4 - Scared
    // 5 - ScaredFlashing
    // 6 - Cruise Elroy
    // 7 - Cruise Elroy MK. II
    // 8 - Eaten
    // ghostId is the true id of the ghost. It should be from (0-3). This is used to remember who the ghost is upon exiting frightened mode.
    protected int direction, decision, ghostNum, ghostId, pelletCounter = 0;
    protected boolean uTurn, releasing;
    protected boolean[] open;
    protected MapLocation indices;
    protected Game game;
    protected GhostPlane[] facePlanes;
    protected Triangle[] faceTriangles;
    
    private final int GHOST_DEBUG = 0;

    public Ghost(Game g, int ghostNum) {
        this.game=g;
        this.ghostNum=ghostNum;
        this.ghostId=ghostNum;
        this.location=GameUtilities.GHOST_LOCATIONS[GHOST_DEBUG];
        this.direction=GameUtilities.GHOST_ORIENTATIONS[GHOST_DEBUG];
        this.decision=GameUtilities.GHOST_ORIENTATIONS[GHOST_DEBUG];
        Point top = new Point(0.0, 1.0, 0.0).add(this.location);
        Point zPlusXPlus = new Point(0.25, 0.5, 0.25).add(this.location);
        Point zMinusXPlus = new Point(0.25, 0.5, -0.25).add(this.location);
        Point zMinusXMinus = new Point(-0.25, 0.5, -0.25).add(this.location);
        Point zPlusXMinus = new Point(-0.25, 0.5, 0.25).add(this.location);
        Point lowerZPlusXPlus = new Point(0.5, -1.0, 0.5).add(this.location);
        Point lowerZMinusXPlus = new Point(0.5, -1.0, -0.5).add(this.location);
        Point lowerZMinusXMinus = new Point(-0.5, -1.0, -0.5).add(this.location);
        Point lowerZPlusXMinus = new Point(-0.5, -1.0, 0.5).add(this.location);
        facePlanes = new GhostPlane[4];
        faceTriangles = new Triangle[4];
        faceTriangles[0] = new Triangle(top, zPlusXPlus, zPlusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        faceTriangles[1] = new Triangle(top, zMinusXMinus, zPlusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        faceTriangles[2] = new Triangle(top, zMinusXPlus, zMinusXMinus, GameUtilities.GHOST_COLORS[ghostNum]);
        faceTriangles[3] = new Triangle(top, zPlusXPlus, zMinusXPlus, GameUtilities.GHOST_COLORS[ghostNum]);
        facePlanes[0] = new GhostPlane(lowerZPlusXPlus, lowerZPlusXMinus, zPlusXPlus, ghostNum);
        facePlanes[1] = new GhostPlane(lowerZPlusXMinus, lowerZMinusXMinus, zPlusXMinus, ghostNum);
        facePlanes[2] = new GhostPlane(lowerZMinusXMinus, lowerZMinusXPlus, zMinusXMinus, ghostNum);
        facePlanes[3] = new GhostPlane(lowerZMinusXPlus, lowerZPlusXPlus, zMinusXPlus, ghostNum);
        for (int i = 0; i < 4; i++) {
            g.addObject(faceTriangles[i]);
            g.addObject(facePlanes[i]);
        }
        facePlanes[direction].setFace(true);
        g.addTickable(this);
    }

    public void tick(){
        if (game.getTicksThisMode() == 0 && game.getGameStage() != 0) {
            uTurn = true;
        }
        for (int i = 0; i < 4; i++) {
            facePlanes[i].shiftTexture();
        }
        // We'll need to rework this.
        // Andrey, you'll need to implement ALL the rules 
        // concerning Blinky turning into his 2 Cruise Elroy forms. 
        if (ghostId == BLINKY && GameUtilities.GAME_DATA[game.getLevel()][3]==game.getPelletsRemaining()) {
            ghostNum = CRUISE_ELROY;
            updatePlanes();
        }
        if (ghostId == BLINKY && (ghostNum == BLINKY || ghostNum == CRUISE_ELROY) && GameUtilities.GAME_DATA[game.getLevel()][3]==game.getPelletsRemaining() * 2) {
            ghostNum = CRUISE_ELROY_2;
            updatePlanes();
        }
        move();
        if (Math.abs(location.x) > 14){
            translate(new Vector(-28*GameUtilities.MPT * Math.signum(location.x), 0, 0));
        }
    }

    public void move() {
        if (game.getTicksThisMode() == 299)
            System.out.println("ticks = " + game.getTicksThisMode());
        Point newLocation = location.add(new Point(dirToV()));
        indices = new MapLocation(newLocation);
        if (!new MapLocation(location).equals(new MapLocation(newLocation))) {
            if (uTurn){
                uTurn = false;
                direction = (direction + 2) % 4;
            } else {
                direction = decision;
            }
            open(true);
            decision = makeDecision();
            facePlanes[direction].setFace(true);
            facePlanes[(direction+1)%4].setFace(false);
            facePlanes[(direction+2)%4].setFace(false);
            facePlanes[(direction+3)%4].setFace(false);
        }
        if (new MapLocation(location).mx != new MapLocation(newLocation).mx) {
            newLocation = new Point(newLocation.x, newLocation.y, Math.floor(newLocation.z) + 0.5);
        } else {
            newLocation = new Point(Math.floor(newLocation.x) + 0.5, newLocation.y, newLocation.z);
        }
        translate(new Vector(newLocation.subtract(location)));
    }

    public int makeDecision(){
        if (atLeastOne()) {
            if ((indices.mx == 12 || indices.mx == 15) && (indices.my == 11 || indices.my == 23) && game.getMode() == -1){
                open = GameUtilities.nd.clone();
            }
            target = findTarget();
            open[(direction + 2) % 4] = false;
            double shortest = Double.MAX_VALUE;
            int toReturn = 3;
            for (int i = 0; i < 4; i++){
                double s = new Vector(new Point(location.x + (i == 1 ? -1 : (i == 3 ? 1 : 0)) + (direction % 2 == 1 ? direction - 2 : 0), 1, location.z + (i == 0 ? 1 : (i == 2 ? -1 : 0)) + (direction % 2 == 0 ? -direction + 1 : 0)), target).s();
                if (open[i] && s < shortest){
                    shortest = s;
                    toReturn = i;
                }
            }
            return toReturn;
        } else if (releasing) {
            return release();
        } else {
                switch (ghostNum) {
                    case BLINKY:
                    case SCARED:
                    case SCARED_FLASHING:
                    case CRUISE_ELROY:
                    case CRUISE_ELROY_2:
                        throw new IllegalArgumentException();
                    case PINKY:
                    case INKY:
                    case CLYDE:
                        if (pelletCounter == GameUtilities.EXIT_PELLETS[game.getLevel()][ghostId]) {
                            return release();
                        } else {
                            //bumping up and down
                        }
                        break;//can comment later
                    case EATEN:
                        if (location.x == 0 && location.y == 0) {
                            ghostNum = ghostId;
                            release();
                        } else {
                            return 2;
                        }
                }
            throw new IllegalArgumentException();
        }
    }

    public Point findTarget() {
        if (game.getMode() == 0 && (ghostNum == BLINKY || ghostNum == PINKY || ghostNum == INKY || ghostNum == CLYDE)){
            return GameUtilities.GHOST_CORNERS[ghostNum];
        }
        switch(ghostNum) {
            case BLINKY:
            case CRUISE_ELROY:
            case CRUISE_ELROY_2:
                return game.getPlayer().getLoc();
            case PINKY: {
                Vector dir = game.getPlayer().getDir();
                double yaw = dir.polarTransform()[0];
                Point tar = new Point(new Vector(game.getPlayer().getLocPoint()).add(dir.scalarProduct(4)));
                if (yaw > Math.PI / 4 && yaw < 3 * Math.PI / 4){
                    tar = tar.add(new Point(Vector.fromPolarTransform(yaw + Math.PI / 2, 0, 4)));
                }
                return tar;
            }
            case INKY: {
                Vector dir = game.getPlayer().getDir();
                double yaw = dir.polarTransform()[0];
                Point tar = new Point(new Vector(game.getPlayer().getLocPoint()).add(dir.scalarProduct(2)));
                if (yaw > Math.PI / 4 && yaw < 3 * Math.PI / 4){
                    tar = tar.add(new Point(Vector.fromPolarTransform(yaw + Math.PI / 2, 0, 2)));
                }
                Point blinkyPosition = game.getGhosts().get(0).getLocation();
                return new Point(2 * tar.x - blinkyPosition.x, 1, 2 * tar.z - blinkyPosition.z);
            }
            case CLYDE: {
                if (new Vector(location, game.getPlayer().getLoc()).s() > 64){
                    return game.getPlayer().getLoc();
                }
                return GameUtilities.GHOST_CORNERS[3];
            }
            case SCARED:
            case SCARED_FLASHING:
                return null;
            case EATEN:
                return eyesTarget;
            default:
                return null;
        }
    }
    
    public void updatePlanes() {
        for (int i = 0; i < 4; i++) {
            facePlanes[(direction+i)%4].setGhostNum(ghostNum);
        }
    }
    
    public void translate(Vector v) {
        location = location.add(new Point(v));
        for (int i = 0; i < 4; i++) {
            facePlanes[i].translate(v);
            faceTriangles[i].translate(v);
        }
    }

    public void reset(){
        this.ghostNum=this.ghostId;
        this.direction=GameUtilities.GHOST_ORIENTATIONS[GHOST_DEBUG];
        this.decision=GameUtilities.GHOST_ORIENTATIONS[GHOST_DEBUG];
        Vector v = new Vector(this.location, GameUtilities.GHOST_LOCATIONS[GHOST_DEBUG]);
        translate(v);
        updatePlanes();
    }

    public Vector dirToV(){
        // ANDREY! ADD THE CORRECT TUNNEL SPEEDS HERE!
        // ANDREY! DO EVERYTHING!
        return new Vector(direction % 2 == 0 ? 0 : direction - 2, 0, direction % 2 == 1 ? 0 : -direction + 1).setScalar((GameUtilities.GAME_DATA[game.getLevel()][1] + (ghostNum == CRUISE_ELROY ? 5 : (ghostNum == CRUISE_ELROY_2 ? 10 : 0))) / 2500.0);
    }

    public Point getLocation(){
        return location;
    }

    public void getAte() {
        ghostNum = EATEN;
    }

    public void open(boolean shift) {
        if (shift) {
            open = GameUtilities.INTERSECTIONS[(indices.mx + (direction % 2 == 1 ? direction - 2 : 0) + 28) % 28][(indices.my - 3 + (direction % 2 == 0 ? direction - 1 : 0) + 31) % 31].clone();
        } else {
            open = GameUtilities.INTERSECTIONS[indices.mx][indices.my - 3];
        }
    }

    public void addToCounter() {
        pelletCounter++;
    }

    public int release() {
        releasing = atLeastOne();
        if (releasing) {
            direction = location.x >= 0 ? (int) Math.signum(location.x) : 3;
            decision = location.x >= 0 ? (int) Math.signum(location.x) : 3;
        }
        return decision;
    }

    public boolean atLeastOne() {
        open(false);
        return open[0] || open[1] || open[2] || open[3];
    }

    public boolean inside() {
        open(false);
        return !(releasing || atLeastOne());
    }
}
