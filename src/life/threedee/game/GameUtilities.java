package life.threedee.game;

import life.threedee.Vector;

public class GameUtilities{
	public static final int SC_WIDTH = 720;
	public static final int SC_HEIGHT = 540;
	public static final int R_INC = 4;
	
	public static final Vector G = new Vector(0, -0.981, 0);
	
	public static boolean equals(double a, double b) {
		return Math.abs(a - b) < 1e-15;
	}
}
