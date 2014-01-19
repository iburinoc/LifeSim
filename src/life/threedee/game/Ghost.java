package life.threedee.game;

import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.game.maps.MapLocation;
import static life.threedee.game.GameUtilities.*;

public class Ghost implements Tickable{
    /**
     * Current location of the ghost.
     */
    protected Point location;

    /**
     * Where the ghost will be in one tick.
     */
    protected Point newLocation;

    /**
     * The target the ghost is trying to get to.
     */
    protected Point target;

    /**
     * The direction the ghost is facing (0-3).
     * 0 - North - Up
     * 1 - West - Left
     * 2 - South - Down
     * 3 - East - Right
     */
    protected int direction;

    /**
     * The direction the ghost is going to turn in the centre of this tile.
     */
    protected int decision;

    /**
     * The direction the ghost is going to turn in one tile.
     */
    protected int nextDecision;

    /**
     * The current state of the ghost. This is used to decide textures, behaviour, etc.
     */
    protected int ghostNum;

    /**
     * The true id of the ghost. This is used to remember who the ghost is upon exiting frightened mode.
     */
    protected int ghostId;

    /**
     * The ghost uses this counter to determine when it can leave the house.
     */
    protected int pelletCounter;

    /**
     * The ghost uses this timer to determine when it can leave the house.
     */
    protected int ghostTimer;

    /**
     * The number of ticks that the ghost is going to be scared for.
     */
    protected int scaredTicksLeft;

    /**
     * Whether the ghost should turn around upon reaching the next tile.
     */
    protected boolean uTurn;

    /**
     * A flag used for bouncing up and down inside the ghost house.
     */
    protected boolean flipFlag = true;

    /**
     * Whether or not the ghost has been told to leave the house.
     */
    protected boolean sentRelease;

    /**
     * Whether the ghost has been eaten already. Used to avoid eating ghosts again.
     */
    protected boolean frightenedThisMode;

    /**
     * The game the ghost gets information from.
     */
    protected Game game;

    /**
     * The planes on which most of the ghost is drawn.
     */
    protected GhostPlane[] facePlanes;

    /**
     * The planes on which the rest of the ghost is drawm.
     */
    protected Triangle[] faceTriangles;

    /**
     * This is the "default" constructor.
     * @param g This is the game that the ghost will get information from to make certain decisions.
     * @param ghostNum This is the number of the ghost. Ghost behaviour varies depending on ghostNum (and other factors). (See above.)
     */
    public Ghost(Game g, int ghostNum) {
        this.game=g;
        this.ghostNum=ghostNum;
        this.ghostId=ghostNum;
        this.location=GHOST_LOCATIONS[ghostNum];
        this.direction=GHOST_ORIENTATIONS[ghostNum];
        this.decision=GHOST_ORIENTATIONS[ghostNum];
        this.nextDecision=GHOST_ORIENTATIONS[ghostNum];
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
        faceTriangles[0] = new Triangle(top, zPlusXPlus, zPlusXMinus, GHOST_COLORS[ghostNum]);
        faceTriangles[1] = new Triangle(top, zMinusXMinus, zPlusXMinus, GHOST_COLORS[ghostNum]);
        faceTriangles[2] = new Triangle(top, zMinusXPlus, zMinusXMinus, GHOST_COLORS[ghostNum]);
        faceTriangles[3] = new Triangle(top, zPlusXPlus, zMinusXPlus, GHOST_COLORS[ghostNum]);
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

    /**
     * This method is run every tick. The ghost updates things such as timers. It represents 1 / 60 of a second.
     */
    public void tick(){
        if (game.getTicksThisMode() == 0 && game.getGameStage() != 0) {
            uTurn = true;
        }
        if (game.getMode() == -1 && ghostNum != EATEN && !frightenedThisMode) {
            int ticks = FRIGHTENED_DATA[game.getArraySafeLevel()][1] * 30;
            ghostNum = scaredTicksLeft < ticks && (scaredTicksLeft % 30) < 15 ? SCARED_FLASHING : SCARED;
            updatePlanes();
        }
        if (game.getMode() == -1) {
            scaredTicksLeft--;
            if (scaredTicksLeft == 0 && ghostNum != EATEN) {
                uTurn = true;
                ghostNum = ghostId;
                updatePlanes();
            }
        }
        for (int i = 0; i < 4; i++) {
            facePlanes[i].shiftTexture();
        }
        if (ghostId == BLINKY && ghostNum != SCARED && ghostNum != SCARED_FLASHING && ghostNum != EATEN && GAME_DATA[game.getArraySafeLevel()][3] >= game.getPelletsRemaining()) {
            ghostNum = CRUISE_ELROY;
            updatePlanes();
        }
        if (ghostId == BLINKY && (ghostNum == BLINKY || ghostNum == CRUISE_ELROY) && ghostNum != SCARED_FLASHING && ghostNum != EATEN && GAME_DATA[game.getArraySafeLevel()][3] >= game.getPelletsRemaining() * 2) {
            ghostNum = CRUISE_ELROY_2;
            updatePlanes();
        }
        move();
        if (Math.abs(location.x) > 14){
            translate(new Vector(-28 * MPT * Math.signum(location.x), 0, 0));
        }
    }

    /**
     * This method is responsible for looking ahead to decide where to go and the ghost moving forward by a tiny bit.
     */
    public void move() {
        newLocation = location.add(new Point(dirToV()));
        boolean leaving = (((game.getGlobalCounterEnabled() && game.getGlobalPelletCounter() >= POSTMORTEM_PELLETS[ghostId]) ||
                           (!game.getGlobalCounterEnabled() && pelletCounter >= EXIT_PELLETS[game.getArraySafeLevel()][ghostId]) ||
                             ghostTimer >= GAME_DATA[game.getArraySafeLevel()][4]) && inside());
        if (!sentRelease && leaving) {
            sentRelease = true;
            nextDecision = release();
        } else if (!new MapLocation(location).equals(new MapLocation(newLocation))) {
            nextDecision = leaving && ghostNum != EATEN ? release() : makeDecision();
        } else if ((((int) location.x + Math.signum(location.x) / 2 > location.x) != ((int) location.x + Math.signum(location.x) / 2 > newLocation.x)) ||
        		   (((int) location.z + Math.signum(location.z) / 2 > location.z) != ((int) location.z + Math.signum(location.z) / 2 > newLocation.z))) {
            direction = decision;
            decision = nextDecision;
        }
        facePlanes[direction].setFace(true);
        facePlanes[(direction+1)%4].setFace(false);
        facePlanes[(direction+2)%4].setFace(false);
        facePlanes[(direction+3)%4].setFace(false);
        translate(new Vector(newLocation.subtract(location)));
    }

    /**
     * The ghost looks at the current state of things in the game and decides where it will turn in one tile.
     * @return The direction in which the ghost will turn in one tile.
     */
    public int makeDecision(){
        if (justExited()) {
            return nextDecision;
        } else if (!inside()) {
            if (ghostNum == EATEN && Math.abs(location.x) < dirToV().s() && Math.abs(location.z - 3.5) < dirToV().s()) {
                direction = 2;
                decision = 2;
                return decision;
            }
            if (uTurn) {
                uTurn = false;
                direction = (direction + 2) % 4;
                decision = direction;
                decision = makeDecision();
                return decision;
            }
            MapLocation indices = new MapLocation(newLocation.add(new Point(decision % 2 == 0 ? 0 : decision - 2, 0, decision % 2 == 0 ? -decision + 1 : 0)));
            boolean[] open = INTERSECTIONS[indices.mx][indices.my].clone();
            if ((indices.mx == 12 || indices.mx == 15) && (indices.my == 14 || indices.my == 26) && game.getMode() == -1){
                open = new boolean[] {true, true, false, true};
            }
            target = findTarget();
            if (target == null) {
                int choices = 0;
                for (int i = 0; i < 4; i++) {
                    if (open[i] && i != (decision + 2) % 4) {
                        choices++;
                    }
                }
                int random = (int) (Math.random() * choices) + 1;
                for (int i = 0, counter = 0; i < 4; i++) {
                    if (open[i] && i != (decision + 2) % 4) {
                        counter++;
                    }
                    if (random == counter){
                        return i;
                    }
                }
            }
            open[(decision + 2) % 4] = false;
            double shortest = Double.MAX_VALUE;
            int toReturn = 3;
            for (int i = 3; i >= 0; i--){
                double s = new Vector(
                	new Point(location.x + (i % 2 == 0 ? 0 : i - 2) + (decision % 2 == 0 ? 0 : decision - 2),
                			1,
                			location.z + (i % 2 == 0 ? -i + 1 : 0) + (decision % 2 == 0 ? -decision + 1 : 0)),
                			target).s();
                if (open[i] && s <= shortest){
                    shortest = s;
                    toReturn = i;
                }
            }
            return toReturn;
        } else {
            switch (ghostNum) {
                case PINKY:
                case INKY:
                case CLYDE:
                case SCARED:
                case SCARED_FLASHING:
                    if (flipFlag) {
                        direction = (direction + 2) % 4;
                        decision = direction;
                        flipFlag = false;
                        return decision;
                    } else {
                        flipFlag = true;
                        return decision;
                    }
                case EATEN:
                    if (Math.abs(newLocation.x) < dirToV().s()) {
                        if (newLocation.z > 0) {
                            direction = 2;
                            decision = 2;
                            return decision;
                        } else if (ghostId == BLINKY || ghostId == PINKY) {
                            ghostNum = ghostId;
                            updatePlanes();
                            direction = 0;
                            decision = 0;
                            return 1;
                        } else {
                            direction = (int) (2 * (ghostId - 2.5) + 2);
                            decision = direction;
                            return decision;
                        }
                    } else if (Math.abs(location.x) > 2) {
                        ghostNum = ghostId;
                        updatePlanes();
                        direction = 0;
                        decision = 0;
                        return decision;
                    } else {
                        decision = direction;
                        return decision;
                    }
                case BLINKY:
                case CRUISE_ELROY:
                case CRUISE_ELROY_2:
                    direction = 0;
                    decision = 0;
                    return 1;
                default: throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Every ghost targets a different tile. This method determines where that tile is.
     * @return The location of the tile the ghost is targeting.
     */
    public Point findTarget() {
        if (game.getMode() == 0 && (ghostNum == BLINKY || ghostNum == PINKY || ghostNum == INKY || ghostNum == CLYDE)){
            return GHOST_CORNERS[ghostNum];
        }
        switch(ghostNum) {
            case BLINKY:
            case CRUISE_ELROY:
            case CRUISE_ELROY_2:
                return game.getPlayer().getLoc();
            case PINKY: {
                Vector dir = game.getPlayer().getDir();
                double yaw = dir.polarTransform()[0];
                Point tar = new Point(new Vector(game.getPlayer().getLoc()).add(dir.scalarProduct(4)));
                if (yaw > Math.PI / 4 && yaw < 3 * Math.PI / 4){
                    tar = tar.add(new Point(Vector.fromPolarTransform(yaw + Math.PI / 2, 0, 4)));
                }
                return tar;
            }
            case INKY: {
                Vector dir = game.getPlayer().getDir();
                double yaw = dir.polarTransform()[0];
                Point tar = new Point(new Vector(game.getPlayer().getLoc()).add(dir.scalarProduct(2)));
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
                return GHOST_CORNERS[3];
            }
            case EATEN:
                return EYES_TARGET;
            case SCARED:
            case SCARED_FLASHING:
            default:
                return null;
        }
    }

    /**
     * This will make the ghost's planes correspond with the current state it is in.
     */
    public void updatePlanes() {
        for (int i = 0; i < 4; i++) {
            facePlanes[(direction+i)%4].setGhostNum(ghostNum);
            faceTriangles[(direction+i)%4].setC(GHOST_COLORS[ghostNum]);
        }
    }

    /**
     * This method translates the entire ghost some distance.
     * @param v The vector by which the ghost must be translated.
     */
    public void translate(Vector v) {
        location = location.add(new Point(v));
        for (int i = 0; i < 4; i++) {
            facePlanes[i].translate(v);
            faceTriangles[i].translate(v);
        }
    }

    /**
     * This resets the ghost to its default location, as well as resetting some of its values.
     */
    public void reset(){
        this.ghostNum=this.ghostId;
        this.direction=GHOST_ORIENTATIONS[ghostNum];
        this.decision=GHOST_ORIENTATIONS[ghostNum];
        Vector v = new Vector(this.location, GHOST_LOCATIONS[ghostNum]);
        translate(v);
        updatePlanes();
    }

    /**
     * This method uses information from the game (e.g. what mode is the game in, what level, whether its in the tunnel, whether it has been eaten) to transform the direction the ghost is facing into a vector by which it can move in one tick.
     * @return The vector by which the ghost will move in one tick.
     */
    public Vector dirToV(){
        return new Vector(direction % 2 == 0 ? 0 : direction - 2, 0, direction % 2 == 1 ? 0 : -direction + 1).setScalar
            (ghostNum == EATEN ? GAME_DATA[game.getArraySafeLevel()][1] / 500.0 :
                (game.getMode() == -1 ? (GAME_DATA[game.getArraySafeLevel()][1] + 25) / 5000.0 :
                    ((Math.abs(location.x) > 9 && Math.abs(location.z - 0.5) < 0.5) ? (GAME_DATA[game.getArraySafeLevel()][1] + 5) / 5000.0 :
                        ((GAME_DATA[game.getArraySafeLevel()][1] + (ghostNum == CRUISE_ELROY ? 5 :
                            (ghostNum == CRUISE_ELROY_2 ? 10 : 0)))  / 2500.0))));
    }

    /**
     * Location getter.
     * @return The location of the ghost.
     */
    public Point getLocation(){
        return location;
    }

    /**
     * This method changes some values in case the ghost has been consumed by the player.
     */
    public void getAte() {
        ghostNum = EATEN;
        frightenedThisMode = true;
        updatePlanes();
    }

    /**
     * This method returns whether or not is inside the ghost house in the centre of the map (edges included).
     * @return Whether the ghost is inside or not.
     */
    public boolean inside() {
        return Math.abs(newLocation.x) < 4 && Math.abs(newLocation.z - 1) < 2;
    }

    /**
     * This method checks whether the ghost is inside the house and is not going to be anymore in one tick.
     * @return
     */
    public boolean justExited() {
        MapLocation coords = new MapLocation(location);
        boolean[] open = INTERSECTIONS[coords.mx][coords.my];
        return !open(open) && !inside();
    }

    /**
     * This method is called in case the ghost needs to be released from the house.
     * @return The decision the ghost should make to leave the house.
     */
    public int release() {
        if (Math.abs(location.x) < dirToV().s()) {
            direction = 0;
            decision = 0;
            translate(new Vector(new Point(-0.000001, location.y, location.z).subtract(location)));
            return 1;
        } else {
            direction = (int) Math.signum(-location.x) + 2;
            decision = direction;
            return decision;
        }
    }

    /**
     * If frightened mode is activated, this method is called, updating some values.
     * @param ticks The number of ticks this fright will last for. Varies with level.
     */
    public void scare(int ticks) {
        this.uTurn = true;
        frightenedThisMode = true;
        if (ghostNum != EATEN) {
            this.ghostNum = SCARED;
            this.updatePlanes();
            this.scaredTicksLeft = ticks;
        }
    }

    /**
     * This method adds to the counter controlling when the ghost gets to leave the house.
     */
    public void addToCounter() {
        pelletCounter++;
    }

    /**
     * This method resets the counter controlling when the ghost gets to leave the house.
     */
    public void resetCounter() {
        pelletCounter = 0;
    }

    /**
     * This method adds to the timer controlling when the ghost gets to leave the house.
     */
    public void addToTimer() {
        ghostTimer++;
    }

    /**
     * This method resets the timer controlling when the ghost gets to leave the house.
     */
    public void resetTimer() {
        ghostTimer = 0;
    }
}
