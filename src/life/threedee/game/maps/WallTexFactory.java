package life.threedee.game.maps;

import static life.threedee.game.GameUtilities.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class WallTexFactory {
	public static BufferedImage createWallTex(int tileWidth) {
		int w = (int) (tileWidth * TPM * PX_METER);
		int h = (int) (WALL_HEIGHT * TPM * PX_METER);
		return createGenTex(w, h);
	}
	
	private static BufferedImage createGenTex(int w, int h) {
		BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = b.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, w, h);
		g.setColor(W_BLUE);
		g.fillRect(0, 0, w, PX_WALL_BORDER);
		g.fillRect(0, h - PX_WALL_BORDER, w, PX_WALL_BORDER);
		g.fillRect(0, PX_WALL_BORDER, PX_WALL_BORDER, h - 2 * PX_WALL_BORDER);
		g.fillRect(w - PX_WALL_BORDER, PX_WALL_BORDER, PX_WALL_BORDER, h - 2 * PX_WALL_BORDER);
		
		return b;
	}
}
