package life.threedee.game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import life.threedee.Vector;

public class GameUtilities{
	public static final int SC_WIDTH = 720;
	public static final int SC_HEIGHT = 540;
	public static final int R_INC = 4;
	
	public static final int PX_METER = 25;
	
	public static final Vector G = new Vector(0, -0.981, 0);
	
	public static final Color BLANK = new Color(0, 0, 0, 0);
	
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
}
