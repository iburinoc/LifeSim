package life.threedee.game;

import life.threedee.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameUtilities{
	public static final int SC_WIDTH = 720;
	public static final int SC_HEIGHT = 540;
	public static final int R_INC = 8;
	
	public static final int PX_METER = 20;
	
	public static final int PX_WALL_BORDER = 2;
	
	// Wall height in tiles - changeable
	public static final int WALL_HEIGHT = 2;
	
	// Floor width in tiles - should not be changed
	public static final int FLOOR_WIDTH = 2;
	
	// meters per tile.  we can modify this if we need to.  use this in all calculations necessary so scaling is smooth
	public static final float MPT = 1.0f;
	
	public static final Color BLANK = new Color(0, 0, 0, 0);

	//The color of the wall borders.  In case we ever want to change it.
	public static final Color W_BLUE = Color.BLUE;

    public static final boolean t = true, f = false;

    public static final boolean[] no = {f, f, f, f}, ud = {t, f, t, f}, rl = {f, t, f, t}, ul = {t, t, f, f}, dl = {f, t, t, f}, dr = {f, f, t, t}, ur = {t, f, f, t}, nu = {f, t, t, t}, nl = {t, f, t, t}, nd = {t, t, f, t}, nr = {t, t, t, f}, al = {t, t, t, t};

    public static final boolean[][][] INTERSECTIONS = mirror(new boolean[][][]
            {{no, no, no, no, no, no, no, no, no, no, no, no, no, no, rl, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no},
             {no, dr, ud, ud, ud, nl, ud, ud, ur, no, no, no, no, no, rl, no, no, no, no, no, dr, ud, ud, ur, no, no, dr, ud, ud, ur, no},
             {no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no},
             {no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, dl, ud, ud, nd, no, no, rl, no},
             {no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no},
             {no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no},
             {no, nu, ud, ud, ud, al, ud, ud, nr, ud, ud, ud, ud, ud, al, ud, ud, ud, ud, ud, al, ud, ud, nl, ud, ud, ul, no, no, rl, no},
             {no, rl, no, no, no, rl, no, no, no, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no},
             {no, rl, no, no, no, rl, no, no, no, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no},
             {no, rl, no, no, no, nu, ud, ud, ur, no, no, dr, ud, ud, nr, ud, ud, nl, ud, ud, nd, no, no, nu, ud, ud, ur, no, no, rl, no},
             {no, rl, no, no, no, rl, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no},
             {no, rl, no, no, no, rl, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no},
             {no, dl, ud, ud, ud, nd, no, no, dl, ud, ud, nd, no, no, no, no, no, rl, no, no, dl, ud, ud, nd, no, no, dl, ud, ud, nd, no},
             {no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no}});

    private static boolean[][][] mirror(boolean[][][] intersections){
        boolean[][][] toReturn = new boolean[intersections.length * 2][intersections[0].length][intersections[0][0].length];
        for (int i = 0; i < intersections.length; i++){
            for (int j = 0; j < toReturn[i].length; j++){
                for (int k = 0; k < toReturn[i][j].length; k++){
                    toReturn[i][j][k] = intersections[i][j][k];
                    toReturn[toReturn.length-1-i][j][k] = intersections[i][j][(k + ((k % 2) * 2)) % 4];
                }
            }
        }
        for (int i = 0; i < toReturn.length; i++){
            for (int j = 0; j < toReturn[i].length; j++){
                for (int k = 0; k < toReturn[i][j].length; k++){
                    System.out.print(toReturn[i][j][k]?1:0);
                }
                System.out.print(",");
            }
            System.out.println();
        }
        return toReturn;
    }

    //pacman speed, ghost speed, fruit bonus, dots left for elroy
    public static final int[][] GAME_DATA = new int[][] {{80, 75, 100, 20}, {90, 85, 300, 30}, {90, 85, 500, 40}, {90, 85, 500, 40}, {100, 95, 700, 40}, {100, 95, 700, 50}, {100, 95, 1000, 50}, {100, 95, 1000, 50}, {100, 95, 2000, 60}, {100, 95, 2000, 60}, {100, 95, 3000, 60}, {100, 95, 3000, 80}, {100, 95, 5000, 80}, {100, 95, 5000, 80}, {100, 95, 5000, 80}, {100, 95, 5000, 100}, {100, 95, 5000, 100}, {100, 95, 5000, 100}, {100, 95, 5000, 100}, {100, 95, 5000, 120}, {100, 95, 5000, 120}, {90, 95, 5000, 120}};

    //fright time, warning flashes
    public static final int[][] FRIGHTENED_DATA = new int[][] {{6, 5}, {5, 5}, {4, 5}, {3, 5}, {2, 5}, {5, 5}, {2, 5}, {2, 5}, {1, 3}, {5, 5}, {2, 5}, {1, 3}, {1, 3}, {3, 5}, {1, 3}, {1, 3}, {0, 0}, {1, 3}, {0, 0}};

    public static final double[][] MODE_TIMES = new double[][] {{7, 20, 7, 20, 5, 20, 5}, {7, 20, 7, 20, 5, 1033, 1 / 60}, {7, 20, 7, 20, 5, 1033, 1 / 60}, {7, 20, 7, 20, 5, 1033, 1 / 60}, {5, 20, 5, 20, 5, 1037, 1 / 60}};

    public static Color[] GHOST_COLORS = new Color[] {new Color(230, 46, 37), new Color(240, 178, 254), new Color(95, 248, 251), new Color(244, 171, 76), Color.BLUE, Color.WHITE, new Color(230, 46, 37), new Color(230, 46, 37), null};

    public static final Point[] GHOST_LOCATIONS = new Point[] {new Point(0*MPT, 1*MPT, 3.5*MPT), new Point(0*MPT, 1*MPT, 0.5*MPT), new Point(-2*MPT, 1*MPT, 0.5*MPT), new Point(2*MPT, 1*MPT, 0.5*MPT), new Point(0*MPT, 1*MPT, 0*MPT), new Point(0*MPT, 1*MPT, 0*MPT), new Point(2*MPT, 1*MPT, 3.5*MPT), new Point(-2*MPT, 1*MPT, 3.5*MPT), new Point(0*MPT, 1*MPT, 3.5*MPT)};
    public static final int[] GHOST_ORIENTATIONS = new int[] {1, 2, 0, 0, 0, 0, 1, 1, 0};
    public static final Point[] GHOST_CORNERS = new Point[] {new Point(11.5*MPT, 1*MPT, 18.5*MPT), new Point(-11.5*MPT, 1*MPT, 18.5*MPT), new Point(13.5*MPT, 1*MPT, -16.5*MPT), new Point(-13.5*MPT, 1*MPT, -16.5*MPT), new Point(0.0*MPT, 1*MPT, 0.0*MPT), new Point(0.0*MPT, 1*MPT, 0.0*MPT), new Point(11.5*MPT, 1*MPT, 18.5*MPT), new Point(11.5*MPT, 1*MPT, 18.5*MPT), new Point(0*MPT, 1*MPT, 3.5*MPT)};
    public static final BufferedImage[] GHOST_SIDE_TEXTURES = new BufferedImage[] {GameUtilities.loadImage("resources/BlinkySide.png"), 
                                                                                   GameUtilities.loadImage("resources/PinkySide.png"), 
                                                                                   GameUtilities.loadImage("resources/InkySide.png"), 
                                                                                   GameUtilities.loadImage("resources/ClydeSide.png"),
                                                                                   GameUtilities.loadImage("resources/ScaredSide.png"),
                                                                                   GameUtilities.loadImage("resources/ScaredFlashSide.png"),
                                                                                   GameUtilities.loadImage("resources/BlinkySide.png"),
                                                                                   GameUtilities.loadImage("resources/BlinkySide.png"),
                                                                                   GameUtilities.loadImage("resources/EatenSide.png"),};
    public static final BufferedImage[] GHOST_FACE_TEXTURES = new BufferedImage[] {GameUtilities.loadImage("resources/BlinkyFace.png"), 
                                                                                   GameUtilities.loadImage("resources/PinkyFace.png"), 
                                                                                   GameUtilities.loadImage("resources/InkyFace.png"), 
                                                                                   GameUtilities.loadImage("resources/ClydeFace.png"),
                                                                                   GameUtilities.loadImage("resources/ScaredFace.png"),
                                                                                   GameUtilities.loadImage("resources/ScaredFlashFace.png"),
                                                                                   GameUtilities.loadImage("resources/CruiseElroyFace.png"),
                                                                                   GameUtilities.loadImage("resources/CruiseElroy2Face.png"),
                                                                                   GameUtilities.loadImage("resources/EatenFace.png"),};
    
    public static final int BLINKY = 0;
    public static final int PINKY = 1;
    public static final int INKY = 2;
    public static final int CLYDE = 3;
    public static final int SCARED = 4;
    public static final int SCARED_FLASHING = 5;
    public static final int CRUISE_ELROY = 6;
    public static final int CRUISE_ELROY_2 = 7;
    public static final int EATEN = 8;
    
    public static final boolean SCARY_FACES = false;
    
	public static boolean equals(double a, double b) {
		return Math.abs(a - b) < 1e-15;
	}
	
	public static BufferedImage loadImage(String ref) {
		try {
			return ImageIO.read(GameUtilities.class.getResourceAsStream("/" + ref));
		} catch (Exception e) {

			try {
				System.out.println("Get as stream didnt work");
				return ImageIO.read(new File("./" + ref));
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static int idCount = 0; // for map objects
}
