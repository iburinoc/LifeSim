package life.threedee.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import life.threedee.Point;

/**
 * A class containing various variables and methods that are used throughout the program. <br>
 * This also allows for easy editing of the game rules, speeds, and resources.
 * 
 * @author iburinoc
 * @author doome
 * @auther Andrey
 */

public class GameUtilities{
    /**
     * The width of the display in pixels.
     */
	public static final int SC_WIDTH = 720;
	/**
	 * The height of the display in pixels.
	 */
	public static final int SC_HEIGHT = 540;
	/**
	 * The pixels incrementer value.
	 * If it is set to a natural number x, then x screen pixels equals one in-game pixels.
	 * This allows us to change the resolution by just altering one value. 
	 * Note that a 2x increase in R_INC leads to a 4x decrease in game speed.
	 */
	public static int R_INC = 8;
	
	/**
	 * The amount of pixels per meter.
	 * Used to convert pixels in textures to in-game distances.
	 */
	public static final int PX_METER = 20;
	
	public static final int PX_WALL_BORDER = 2;
	
	// Wall height in tiles - changeable
	public static final int WALL_HEIGHT = 2;
	
	// Floor width in tiles - should not be changed
	public static final int FLOOR_WIDTH = 2;
	
	// meters per tile.  we can modify this if we need to.  use this in all calculations necessary so scaling is smooth
	public static final float MPT = 1.0f;
	
	/**
	 * A blank, transparent color. 
	 */
	public static final Color BLANK = new Color(0, 0, 0, 0);

	/**
	 * The color of the wall borders.
	 */
	public static final Color W_BLUE = Color.BLUE;

	// These arrays refer to the possible directions a ghost can go in a given intersection. 
    private static final boolean[] no = {false, false, false, false}; // no directions
    private static final boolean[] ud = { true, false,  true, false}; // up/down
    private static final boolean[] rl = {false,  true, false,  true}; // right/left
    private static final boolean[] ul = { true,  true, false, false}; // up/left
    private static final boolean[] dl = {false,  true,  true, false}; // down/left 
    private static final boolean[] dr = {false, false,  true,  true}; // down/right
    private static final boolean[] ur = { true, false, false,  true}; // up/right
    private static final boolean[] nu = {false,  true,  true,  true}; // not up
    private static final boolean[] nl = { true, false,  true,  true}; // not left
    private static final boolean[] nd = { true,  true, false,  true}; // not down
    private static final boolean[] nr = { true,  true,  true, false}; // not right
    private static final boolean[] al = { true,  true,  true,  true}; // all

    /**
     * An array storing the directions in which ghosts can go at any given tile.
     */
    public static final boolean[][][] INTERSECTIONS = mirror(new boolean[][][]
            {{no, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no, rl, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no, no},
             {no, no, no, no, dr, ud, ud, ud, nl, ud, ud, ur, no, no, no, no, no, rl, no, no, no, no, no, dr, ud, ud, ur, no, no, dr, ud, ud, ur, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, dl, ud, ud, nd, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, no},
             {no, no, no, no, nu, ud, ud, ud, al, ud, ud, nr, ud, ud, ud, ud, ud, al, ud, ud, ud, ud, ud, al, ud, ud, nl, ud, ud, ul, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, no, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, no, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, nu, ud, ud, ur, no, no, dr, ud, ud, nr, ud, ud, nl, ud, ud, nd, no, no, nu, ud, ud, ur, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no, no, no},
             {no, no, no, no, rl, no, no, no, rl, no, no, rl, no, no, rl, no, no, no, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no, no, rl, no, no, no},
             {no, no, no, no, dl, ud, ud, ud, nd, no, no, dl, ud, ud, rl, no, no, no, no, no, rl, no, no, dl, ud, ud, rl, no, no, dl, ud, ud, nd, no, no, no},
             {no, no, no, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no, no, no, rl, no, no, no}});

    // Flips the above hard-coded map to provide the larger, symmetrical, correct map. 
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
        return toReturn;
    }

    //pacman speed, ghost speed, fruit bonus, dots left for elroy
    public static final int[][] GAME_DATA = new int[][] {{80, 75, 100, 20, 960}, {90, 85, 300, 30, 960}, {90, 85, 500, 40, 960}, {90, 85, 500, 40, 960}, {100, 95, 700, 40, 720}, {100, 95, 700, 50, 720}, {100, 95, 1000, 50, 720}, {100, 95, 1000, 50, 720}, {100, 95, 2000, 60, 720}, {100, 95, 2000, 60, 720}, {100, 95, 3000, 60, 720}, {100, 95, 3000, 80, 720}, {100, 95, 5000, 80, 720}, {100, 95, 5000, 80, 720}, {100, 95, 5000, 80, 720}, {100, 95, 5000, 100, 720}, {100, 95, 5000, 100, 720}, {100, 95, 5000, 100, 720}, {100, 95, 5000, 100, 720}, {100, 95, 5000, 120, 720}, {100, 95, 5000, 120, 720}, {90, 95, 5000, 120, 720}};

    //pellets needed for the ghosts to exit the ghost house
    public static final int[][] EXIT_PELLETS = new int[][] {{Integer.MAX_VALUE, 0, 30, 60}, {Integer.MAX_VALUE, 0, 0, 50}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}, {Integer.MAX_VALUE, 0, 0, 0}};
    public static final int[] POSTMORTEM_PELLETS = new int[] {Integer.MAX_VALUE, 7, 17, Integer.MAX_VALUE};

    public static final int STARTING_LIVES = 2;
    
    //fright time, warning flashes
    public static final int[][] FRIGHTENED_DATA = new int[][] {{1440, 5}, {1200, 5}, {960, 5}, {720, 5}, {480, 5}, {1200, 5}, {480, 5}, {480, 5}, {240, 3}, {1200, 5}, {480, 5}, {240, 3}, {240, 3}, {720, 5}, {240, 3}, {240, 3}, {0, 0}, {240, 3}, {0, 0}, {0, 0}, {0, 0}};

    //scatter, chase, scatter, chase......
    public static final int[][] MODE_TIMES = new int[][] {{1680, 4800, 1680, 4800, 1200, 4800, 1200}, {1680, 4800, 1680, 4800, 1200, 247920, 4}, {1680, 4800, 1680, 4800, 1200, 247920, 4}, {1680, 4800, 1680, 4800, 1200, 247920, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}, {1200, 4800, 1200, 4800, 1200, 248880, 4}};

    public static final Color[] GHOST_COLORS = new Color[] {new Color(230, 46, 37), new Color(240, 178, 254), new Color(95, 248, 251), new Color(244, 171, 76), new Color(0, 51, 255), new Color(250, 250, 250), new Color(230, 46, 37), new Color(230, 46, 37), BLANK};

    public static final Point EYES_TARGET = new Point(-0.5, 1, 3.5);

    public static final Point[] GHOST_LOCATIONS = new Point[] {new Point(0*MPT, 1*MPT, 3.5*MPT), new Point(0*MPT, 1*MPT, 0.5*MPT), new Point(-2*MPT, 1*MPT, 0.5*MPT), new Point(2*MPT, 1*MPT, 0.5*MPT)};
    
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
   
    public static final BufferedImage MAP = GameUtilities.loadImage("resources/map.png");
    
    public static final BufferedImage PAC_FULL = GameUtilities.loadImage("resources/pacFull.png");
    
    public static final BufferedImage PAC_SPRITES_FULL = GameUtilities.loadImage("resources/pacsprites.png");
    public static final BufferedImage[][] PAC_SPRITES_ARR = new BufferedImage[][]{
    	{PAC_FULL, pacSubImage(2, 0), pacSubImage(2, 1)},
    	{PAC_FULL, pacSubImage(1, 0), pacSubImage(1, 1)},
    	{PAC_FULL, pacSubImage(3, 0), pacSubImage(3, 1)},
    	{PAC_FULL, pacSubImage(0, 0), pacSubImage(0, 1)}
    };
    
    private static final BufferedImage pacSubImage(int x, int y) {
    	return PAC_SPRITES_FULL.getSubimage(x * 16, y * 16, 16, 16);
    }
    
    public static final int BLINKY = 0;
    public static final int PINKY = 1;
    public static final int INKY = 2;
    public static final int CLYDE = 3;
    public static final int SCARED = 4;
    public static final int SCARED_FLASHING = 5;
    public static final int CRUISE_ELROY = 6;
    public static final int CRUISE_ELROY_2 = 7;
    public static final int EATEN = 8;

    public static final Point FRUIT_LOCATION = new Point(0, 1, -2.5);

    public static final boolean SCARY_FACES = false;
    
    public static final Font SCORE_FONT = loadFont("resources/visitorMod.ttf");
    
    public static final Font GAME_OVER_FONT = SCORE_FONT.deriveFont(30f);
    
    public static final Font TITLE_FONT = SCORE_FONT.deriveFont(150f);
    
    public static final Font TITLE_OPTION_FONT = SCORE_FONT.deriveFont(60f);
    
	public static boolean equals(double a, double b) {
		return Math.abs(a - b) < 1e-15;
	}

    public static boolean open(boolean[] inter) {
        return inter[0] || inter[1] || inter [2] || inter[3];
    }
	
	public static Font loadFont(String ref) {
		Font temp = null;
		try {
			temp = Font.createFont(java.awt.Font.TRUETYPE_FONT, 
					GameUtilities.class.getClassLoader().getResourceAsStream(ref));
		} catch (Exception e) {
			try {
				temp = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
						new File(ref));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			}
		}
		return temp.deriveFont(20f);
	}
	
	public static BufferedImage loadImage(String ref) {
		try {
			return ImageIO.read(GameUtilities.class.getResourceAsStream("/" + ref));
		} catch (Exception e) {
			try {
				return ImageIO.read(new File("./" + ref));
			} catch (IOException e1) {
				e.printStackTrace();
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	public static int idCount = 0; // for map objects

    public static void rIncSet(boolean up) {
        R_INC += up ? -1 : 1;
    }
	
    /**
     * Reads a big-endian int from the input stream
     */
    public static int readInt(InputStream s) throws IOException{
    	int t = 0;
    	for(int i = 0; i < 4; i++) {
    		t |= s.read();
    		t <<= 8;
    	}
    	return t;
    }
    
    /**
     * Reads a big-endian long from the input stream
     * @param s
     * @return
     * @throws IOException
     */
    public static long readLong(InputStream s) throws IOException{
    	int t = 0;
    	for(int i = 0; i < 8; i++) {
    		t |= s.read();
    		t <<= 8;
    	}
    	return t;
    }
    
    /**
     * Writes an int to the output stream.  Big-endian
     * @param s
     * @param t
     * @throws IOException
     */
    public static void writeInt(OutputStream s, int t) throws IOException{
    	for(int i = 0; i < 4; i++) {
    		s.write((int) ((t & (0xffL << ((3 - i) * 8))) >>> ((3 - i) * 8)));
    	}
    }
    
    /**
     * Writes a long to the output stream.  Big-endian
     * @param s
     * @param t
     * @throws IOException
     */
    public static void writeLong(OutputStream s, long t) throws IOException{
    	for(int i = 0; i < 8; i++) {
    		s.write((int) ((t & (0xffL << ((7 - i) * 8))) >>> ((7 - i) * 8)));
    	}
    }

    public static final boolean DEVELOPER_MODE = false;
    
	public static final String CONNECT_URL = "http://99.225.251.49:3005";
}
