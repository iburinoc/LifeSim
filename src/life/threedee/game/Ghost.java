package life.threedee.game;

import life.threedee.Point;
import life.threedee.Triangle;
import life.threedee.Vector;
import life.threedee.game.maps.MapLocation;

import static life.threedee.game.GameUtilities.*;

public class Ghost implements Tickable{
    protected Point location, newLocation, target;
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
    protected int direction, decision, nextDecision, ghostNum, ghostId, pelletCounter = 0, scaredTicksLeft;
    protected boolean uTurn, flipFlag = true, sentRelease;
    protected Game game;
    protected GhostPlane[] facePlanes;
    protected Triangle[] faceTriangles;

    public Ghost(Game g, int ghostNum) {
        this.game=g;
        this.ghostNum=ghostNum;
        this.ghostId=ghostNum;
        this.location=GameUtilities.GHOST_LOCATIONS[ghostNum];
        this.direction=GameUtilities.GHOST_ORIENTATIONS[ghostNum];
        this.decision=GameUtilities.GHOST_ORIENTATIONS[ghostNum];
        this.nextDecision=GameUtilities.GHOST_ORIENTATIONS[ghostNum];
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
        scaredTicksLeft--;
        if (scaredTicksLeft <= 0) {
            ghostNum = ghostId;
        }
        for (int i = 0; i < 4; i++) {
            facePlanes[i].shiftTexture();
        }
        // We'll need to rework this.
        // Andrey, you'll need to implement ALL the rules 
        // concerning Blinky turning into his 2 Cruise Elroy forms. 
        if (ghostId == BLINKY && GameUtilities.GAME_DATA[game.getArraySafeLevel()][3] == game.getPelletsRemaining()) {
            ghostNum = CRUISE_ELROY;
            updatePlanes();
        }
        if (ghostId == BLINKY && (ghostNum == BLINKY || ghostNum == CRUISE_ELROY) && GameUtilities.GAME_DATA[game.getArraySafeLevel()][3] == game.getPelletsRemaining() * 2) {
            ghostNum = CRUISE_ELROY_2;
            updatePlanes();
        }
        move();
        if (Math.abs(location.x) > 14){
            translate(new Vector(-28*GameUtilities.MPT * Math.signum(location.x), 0, 0));
        }
    }

    public void move() {
        newLocation = location.add(new Point(dirToV()));
        boolean leaving = pelletCounter >= GameUtilities.EXIT_PELLETS[game.getArraySafeLevel()][ghostId] && inside();
        if (!sentRelease && leaving) {
            sentRelease = true;
            nextDecision = release();
            facePlanes[decision].setFace(true);
            facePlanes[(decision+1)%4].setFace(false);
            facePlanes[(decision+2)%4].setFace(false);
            facePlanes[(decision+3)%4].setFace(false);
        } else if (!new MapLocation(location).equals(new MapLocation(newLocation))) {
            nextDecision = leaving ? release() : makeDecision();
            facePlanes[decision].setFace(true);
            facePlanes[(decision+1)%4].setFace(false);
            facePlanes[(decision+2)%4].setFace(false);
            facePlanes[(decision+3)%4].setFace(false);
        } else if ((Math.abs(location.x % 1) < 0.5 != Math.abs(newLocation.x % 1) < 0.5) || (Math.abs(location.z % 1) < 0.5 != Math.abs(newLocation.z % 1) < 0.5)) {
            direction = decision;
            decision = nextDecision;
        }
        translate(new Vector(newLocation.subtract(location)));
    }

    public int makeDecision(){
        if (justExited()) {
            return nextDecision;
        } else if (!inside()) {
            if (uTurn) {
                uTurn = false;
                direction = (direction + 2) % 4;
                decision = direction;
                decision = makeDecision();
                return 0;
            }
            if (ghostNum == EATEN && Math.abs(location.x) < dirToV().s() && Math.abs(location.z - 3.5) < dirToV().s()) {
                direction = 2;
                decision = 2;
                return 0;
            }
            MapLocation indices = new MapLocation(newLocation.add(new Point(decision % 2 == 0 ? 0 : decision - 2, 0, decision % 2 == 0 ? -decision + 1 : 0)));
            boolean[] open = GameUtilities.INTERSECTIONS[indices.mx][indices.my].clone();
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
                				target)
                .s();
                if (open[i] && s <= shortest){
                    shortest = s;
                    toReturn = i;
                }
            }
            if (ghostId==BLINKY) {
                for (int i = 0; i < 4; i++) {
                    System.out.print(open[i]?1:0);
                }
                System.out.print(nextDecision);
                System.out.print(decision);
                System.out.println(direction);
            }
            return toReturn;
        } else {
            if (ghostId==BLINKY) {
                System.out.print(nextDecision);
                System.out.print(decision);
                System.out.println(direction);
            }
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
                        return 0;
                    } else {
                        flipFlag = true;
                        return 0;
                    }
                case EATEN:
                    if (Math.abs(location.x) < dirToV().s()) {
                        if (location.z > 0) {
                            direction = 2;
                            decision = 2;
                            return 0;
                        } else {
                            if (ghostId == BLINKY || ghostId == PINKY) {
                                ghostNum = ghostId;
                                direction = 0;
                                decision = 0;
                                return 0;
                            } else {
                                direction = (int) (2 * (ghostId - 2.5) + 2);
                                decision = direction;
                                return decision;
                            }
                        }
                    } else if (Math.abs(location.x) > 2) {
                        ghostNum = ghostId;
                        direction = 0;
                        decision = 0;
                        return 0;
                    } else {
                        return direction;
                    }
                case BLINKY:
                case CRUISE_ELROY:
                case CRUISE_ELROY_2:
                default: throw new IllegalArgumentException("Blinky movement not implemented here yet.");
            }
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
                return GameUtilities.GHOST_CORNERS[3];
            }
            case EATEN:
                return eyesTarget;
            case SCARED:
            case SCARED_FLASHING:
            default:
                return null;
        }
    }
    
    public void updatePlanes() {
        for (int i = 0; i < 4; i++) {
            facePlanes[(direction+i)%4].setGhostNum(ghostNum);
            faceTriangles[(direction+i)%4].setC(GameUtilities.GHOST_COLORS[ghostNum]);
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
        this.direction=GameUtilities.GHOST_ORIENTATIONS[ghostNum];
        this.decision=GameUtilities.GHOST_ORIENTATIONS[ghostNum];
        Vector v = new Vector(this.location, GameUtilities.GHOST_LOCATIONS[ghostNum]);
        translate(v);
        updatePlanes();
    }

    public Vector dirToV(){
        // ANDREY! ADD THE CORRECT TUNNEL SPEEDS HERE!
        // ANDREY! DO EVERYTHING!
        return new Vector(direction % 2 == 0 ? 0 : direction - 2, 0, direction % 2 == 1 ? 0 : -direction + 1).setScalar
                (game.getMode() == -1 ? (GAME_DATA[game.getArraySafeLevel()][1] + 15) / 5000.0 :
                        ((Math.abs(location.x) > 9 && Math.abs(location.z - 0.5) < 0.5) ? (GAME_DATA[game.getArraySafeLevel()][1] + 5) / 5000.0 :
                                ((GAME_DATA[game.getArraySafeLevel()][1] + (ghostNum == CRUISE_ELROY ? 5 :
                                        (ghostNum == CRUISE_ELROY_2 ? 10 : 0))) / 2500.0)));
    }

    public Point getLocation(){
        return location;
    }

    public void getAte() {
        ghostNum = EATEN;
    }

    public void addToCounter() {
        pelletCounter++;
    }

    public boolean inside() {
        return Math.abs(newLocation.x) < 4 && newLocation.z < 3 && newLocation.z > -1;
    }

    /*public boolean inside() {
        MapLocation coords = new MapLocation(newLocation);
        boolean[] open = GameUtilities.INTERSECTIONS[coords.mx][coords.my];
        return !(open[0] || open[1] || open[2] || open[3]);
    }*/

    public boolean justExited() {
        MapLocation coords = new MapLocation(location);
        boolean[] open = GameUtilities.INTERSECTIONS[coords.mx][coords.my];
        return !(open[0] || open[1] || open[2] || open[3]) && !inside();
    }

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
    
    public void scare(int ticks) {
        this.ghostNum = SCARED;
        this.updatePlanes();
        this.scaredTicksLeft = ticks;
    }
}
