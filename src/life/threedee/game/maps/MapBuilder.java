package life.threedee.game.maps;

import static life.threedee.game.GameUtilities.BLANK;
import static life.threedee.game.GameUtilities.MPT;
import static life.threedee.game.GameUtilities.WALL_HEIGHT;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import life.threedee.Plane;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.WorldObject;
import life.threedee.game.GameUtilities;

/**
 * Parses 2D pacman map image and builds the object list for it.
 * @author iburinoc
 *
 */
public class MapBuilder {
	private static final BufferedImage map = GameUtilities.loadImage("resources/map.png");
	private static final BufferedImage wall = GameUtilities.loadImage("resources/wall.png");
	
	private static final int PX_TILE = 8;
	
	public static Map<MapLocation, List<ThreeDeeObject>> deserializeMap(List<ThreeDeeObject> map) {
		Map<MapLocation, List<ThreeDeeObject>> m = new HashMap<MapLocation, List<ThreeDeeObject>>();
		BufferedReader br = null;
		
            WorldObject wo = WorldObject.generateObject("(0,1,0);"
                    + "(0,2,0),(0.5,1.5,0),(0,1.5,0.5);"
                    + "(0,2,0),(0,1.5,0.5),(-0.5,1.5,0);"
                    + "(0,2,0),(-0.5,1.5,0),(0,1.5,-0.5);"
                    + "(0,2,0),(0,1.5,-0.5),(0.5,1.5,0);"
                    + "(0.425,0.5,0.425),(0.5,1.5,0),(0,1.5,0.5);"
                    + "(-0.425,0.5,0.425),(0,1.5,0.5),(-0.5,1.5,0);"
                    + "(-0.425,0.5,-0.425),(-0.5,1.5,0),(0,1.5,-0.5);"
                    + "(0.425,0.5,-0.425),(0,1.5,-0.5),(0.5,1.5,0);"
                    + "(0.5,1.5,0),(0.5,0,0),(0.425,0.5,0.425);"
                    + "(0.5,1.5,0),(0.5,0,0),(0.425,0.5,-0.425);"
                    + "(0,1.5,0.5),(0,0,0.5),(-0.425,0.5,0.425);"
                    + "(0,1.5,0.5),(0,0,0.5),(0.425,0.5,0.425);"
                    + "(-0.5,1.5,0),(-0.5,0,0),(-0.425,0.5,-0.425);"
                    + "(-0.5,1.5,0),(-0.5,0,0),(-0.425,0.5,0.425);"
                    + "(0,1.5,-0.5),(0,0,-0.5),(0.425,0.5,-0.425);"
                    + "(0,1.5,-0.5),(0,0,-0.5),(-0.425,0.5,-0.425)", Color.RED);
         
		try{
			br = new BufferedReader(new FileReader("resources/map.dat"));
		}
		catch(IOException e){}
		for(int x = 0; x < 28; x++) {
			for(int y = 0; y < 36; y++) {
				try{
					String l = br.readLine();
					List<ThreeDeeObject> vis = new ArrayList<ThreeDeeObject>();
					vis.add(map.get(0));
					vis.add(map.get(1));
					vis.add(wo);
					String[] walls = l.split(",");
					for(int i = 0; i < walls.length; i++) {
						try{
							vis.add(map.get(Integer.parseInt(walls[i]) + 2));
						}
						catch(NumberFormatException e) {
							e.printStackTrace();
						}
					}

					m.put(new MapLocation(x, y), vis);
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		try{
			br.close();
		}
		catch(IOException e){}
		m.put(new MapLocation(-1,-1), map);
		return m;
	}
	
	public static List<ThreeDeeObject> createMap() {
		List<ThreeDeeObject> l = new ArrayList<ThreeDeeObject>();
		
		l.add(new Plane(new Point(0, WALL_HEIGHT, 0), new Vector(0, 1, 0), Color.BLACK));
		l.add(new Plane(new Point(0, 0, 0), new Vector(0, 1, 0), Color.BLACK));
		
		int[][] map = parseMap();
		boolean[][] done = new boolean[28][36];
		
		for(int x = 0; x < 28; x++) {
			for(int y = 0; y < 36; y++) {
				if(!done[x][y]) {
					if(map[x][y] > 2) {
						if(x == 17 && y == 15){
							x = 17;
						}
						BufferedImage corner = WallTexFactory.createCornerTex();
						double corn_x, corn_y;
						Vector cv;
						switch(map[x][y]) {
							case 3:
								corn_x = (x - 14 + 0.5) * MPT;
								corn_y = -(y - 18) * MPT;
								cv = new Vector(1, 0, 1);
								break;
							case 4:
								corn_x = (x - 14 + 0.5) * MPT;
								corn_y = -(y - 18 + 1) * MPT;
								cv = new Vector(-1, 0, 1);
								break;
							case 5:
								corn_x = (x - 14 + 0.5) * MPT;
								corn_y = -(y - 18 + 1) * MPT;
								cv = new Vector(-1, 0, -1);
								break;
							case 6:
								corn_x = (x - 14 + 0.5) * MPT;
								corn_y = -(y - 18) * MPT;
								cv = new Vector(1, 0, -1);
								break;
							default:
								corn_x = Double.NaN;
								corn_y = Double.NaN;
								cv = null;
						}
						
						l.add(new MapPlane(new Point(corn_x, 0, corn_y), cv, corner));
						
						int dx;
						switch(map[x][y]) {
							case 3:
							case 4:
								dx = 1;
								break;
							case 5:
							case 6:
								dx = -1;
								break;
							default:
								dx = 0;
						}

						int cx = x + dx;
						try{
							while(map[cx][y] == 2 && !done[cx][y]) {
								done[cx][y] = true;
								cx += dx;
							}
						}
						catch(ArrayIndexOutOfBoundsException e){

						}

						if(cx - x - dx != 0) {
							BufferedImage tex = WallTexFactory.createWallTex((int) Math.abs(cx - x - dx));
							double px = (x + (dx == 1 ? 1 : 0) - 14) * MPT;
							double py = -(y - 18 + 0.5) * MPT;
							
							Point p = new Point(px, 0, py);
							Vector v = new Vector(0, 0, dx);
							
							l.add(new MapPlane(p, v, tex));
						}

						int dy;
						switch(map[x][y]) {
							case 3:
							case 6:
								dy = -1;
								break;
							case 4:
							case 5:
								dy = 1;
								break;
							default:
								dy = 0;
						}
						
						int cy = y + dy;
						try{
							while(map[x][cy] == 1 && !done[x][cy]) {
								done[x][cy] = true;
								cy += dy;
							}
						}
						catch(ArrayIndexOutOfBoundsException e){

						}

						if(cy - y - dy != 0) {
							BufferedImage tex = WallTexFactory.createWallTex((int) Math.abs(cy - y - dy));
							double px = (x - 14 + 0.5) * MPT;
							double py = -(y + (dy == 1 ? 1 : 0) - 18) * MPT;
							
							Point p = new Point(px, 0, py);
							Vector v = new Vector(dy, 0, 0);
							
							l.add(new MapPlane(p, v, tex));
						}
						
						done[x][y] = true;
					}
				}
			}
		}
		
		return l;
	}
	
	/**
	 * 0 = nothing
	 * 1 = wall vert
	 * 2 = wall horizontal
	 * 3 = top right
	 * 4 = bot right
	 * 5 = bot left
	 * 6 = top left
	 * @return
	 */
	private static int[][] parseMap() {
		int[][] map = new int[28][36];
		
		for(int x = 0; x < 28; x++) {
			for(int y = 0; y < 36; y++) {
				 boolean[] walls = parseWalls(x, y);
				 
				 if(walls[0] && walls[1]) {
					 map[x][y] = 3;
				 }else if(walls[1] && walls[2]) {
					 map[x][y] = 4;
				 }else if(walls[2] && walls[3]) {
					 map[x][y] = 5;
				 }else if(walls[3] && walls[0]) {
					 map[x][y] = 6;
				 }else if(walls[0] && walls[2]) {
					 map[x][y] = 1;
				 }else if(walls[1] && walls[3]) {
					 map[x][y] = 2;
				 }
			}
		}
		
		for(int y = 0; y < 36; y++) {
			for(int x = 0; x < 28; x++) {
				System.out.print(map[x][y] + "");
			}
			System.out.println();
		}
		
		return map;
	}
	
	private static boolean[] parseWalls(int x, int y) {
		boolean[] walls = new boolean[4];
		walls[0] = (wall.getRGB(x * 8 + 3, y * 8) & 0xFFFFFF) == 0
				|| (wall.getRGB(x * 8 + 4, y * 8) & 0xFFFFFF) == 0;
		
		walls[1] = (wall.getRGB(x * 8 + 7, y * 8 + 3) & 0xFFFFFF) == 0
				|| (wall.getRGB(x * 8 + 7, y * 8 + 4) & 0xFFFFFF) == 0;
		
		walls[2] = (wall.getRGB(x * 8 + 3, y * 8 + 7) & 0xFFFFFF) == 0
				|| (wall.getRGB(x * 8 + 4, y * 8 + 7) & 0xFFFFFF) == 0;
		
		walls[3] = (wall.getRGB(x * 8, y * 8 + 3) & 0xFFFFFF) == 0
				|| (wall.getRGB(x * 8, y * 8 + 4) & 0xFFFFFF) == 0;

		return walls;
	}
}
