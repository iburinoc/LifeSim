package life.threedee.game.maps;

import static life.threedee.game.GameUtilities.PX_METER;
import static life.threedee.game.GameUtilities.PX_WALL_BORDER;
import static life.threedee.game.GameUtilities.MPT;
import static life.threedee.game.GameUtilities.WALL_HEIGHT;
import static life.threedee.game.GameUtilities.W_BLUE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class WallTexFactory {
	
	private static Map<Integer, BufferedImage> walls = new HashMap<Integer, BufferedImage>();
	
	public static BufferedImage createWallTex(int tileWidth) {
		BufferedImage wall = walls.get(tileWidth);
		if(wall == null){
			int w = (int) (tileWidth * MPT * PX_METER);
			int h = (int) (WALL_HEIGHT * MPT * PX_METER);
			wall = createGenTex(w, h);
			walls.put(tileWidth, wall);
		}
		
		return wall;
	}
	
	private static BufferedImage CORNER_TEX;
	
	public static BufferedImage createCornerTex() {
		if(CORNER_TEX == null) {
			CORNER_TEX = createGenTex(
					(int) (0.5 * (Math.sqrt(2.0) + 0.1) * MPT * PX_METER),
					(int) (WALL_HEIGHT * MPT * PX_METER));
		}
		
		return CORNER_TEX;
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
