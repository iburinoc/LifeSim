package life.threedee.game.maps;

import static java.lang.Math.PI;
import static life.threedee.game.GameUtilities.MPT;
import static life.threedee.game.GameUtilities.R_INC;
import static life.threedee.game.GameUtilities.SC_HEIGHT;
import static life.threedee.game.GameUtilities.SC_WIDTH;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;

/** Builds a list of viewable planes from each spot so that we can just use that for the objects list on render;
 * 
 * @author iburinoc
 *
 */
public class MapAnalysis {
	
	private static final double FOV = 75.0 * Math.PI / 180.0;
	
	/**
	 * Analyze and output.
	 * @param args
	 */
	public static void main(String[] args) {
		List<ThreeDeeObject> map = MapBuilder.createMap();
		Map<MapLocation, List<ThreeDeeObject>> m = analyseMap(map);
		System.out.println(serializeMap(m));
//		Map<MapLocation, List<ThreeDeeObject>> n = new HashMap<MapLocation,List<ThreeDeeObject>>();
//		n.put(new MapLocation(0,0), map);
//		System.out.println(serializeMap(n));
	}

	private static String serializeMap(Map<MapLocation, List<ThreeDeeObject>> m) {
		//StringBuffer s = new StringBuffer();	
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new FileWriter("resources/map.dat"));
			for(int x = 0; x < 28; x++) {
				for(int y = 0; y < 36; y++) {
					MapLocation l = new MapLocation(x, y);
					List<ThreeDeeObject> o = m.get(l);
					if(o != null){
						StringBuffer s = new StringBuffer();
						for(int i = 0; i < o.size(); i++) {
							try{
								s.append(((MapPlane) (o.get(i))).id);
								if(i != o.size() - 1){
									s.append(',');
								}
							}
							catch(ClassCastException e) {
								e.printStackTrace();
							}
						}
						bw.write(s.toString());
						bw.newLine();
					}
				}
			}
			bw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 
	 * @return nope not a joke.  do need to come up with a better way of handling this tho, 1008 lists is a bit messy
	 * ok this is slightly better i think, not sure how hashmaps will do for performance but we'll see
	 */
	public static Map<MapLocation, List<ThreeDeeObject>> analyseMap(List<ThreeDeeObject> map) {
		Map<MapLocation, List<ThreeDeeObject>> m = new HashMap<MapLocation, List<ThreeDeeObject>>();
		
		AnalysisCamera c = new AnalysisCamera();
		c.setObjects(map);
		
		List<List<Vector>> dirs = createDirs();
		
		for(int x = 0; x < 28; x++) {
			for(int y = 0; y < 36; y++) {
				MapLocation l = new MapLocation(x, y);
				List<ThreeDeeObject> visible = analyseTile(c, x, y, map, dirs);
				m.put(l, visible);
				System.out.println(x + ";" + y);
			}
		}
		
		Map<MapLocation, List<ThreeDeeObject>> nm = new HashMap<MapLocation,List<ThreeDeeObject>>();
		
		for(int x = 0; x < 28; x++) {
			for(int y = 0; y < 36; y++) {
				MapLocation l = new MapLocation(x, y);
				List<ThreeDeeObject> n = (List<ThreeDeeObject>) ((ArrayList<ThreeDeeObject>) m.get(l)).clone();
				for(int i = 0; i < 9; i++) {
					int dx = 0;
					int dy = 0;
					switch(i) {
						case 0: dy = -1; break;
						case 1: dx = 1; break;
						case 2: dy = 1; break;
						case 3: dx = -1; break;
						case 4: dy = -1; dx = -1; break;
						case 5: dy = -1; dx = 1; break;
						case 6: dy = 1; dx = -1; break;
						case 7: dy = 1; dx = 1; break;
						case 8: dy = 2; break;
					}
					int nx = x + dx;
					int ny = y + dy;
					MapLocation nl = new MapLocation(nx, ny);
					List<ThreeDeeObject> no = m.get(nl);
					if(no != null && nl.inRange()) {
						for(ThreeDeeObject o : no) {
							if(!n.contains(o)) {
								n.add(o);
							}
						}
					}
				}
				nm.put(l, n);
				System.out.println(x+";"+y);
			}
		}
		return nm;
	}
	
	private static List<ThreeDeeObject> analyseTile(AnalysisCamera c, int x, int y, List<ThreeDeeObject> map, List<List<Vector>> dirs) {
		List<ThreeDeeObject> v = new ArrayList<ThreeDeeObject>();
		double px = (x  - 14 + 0.5) * MPT;
		double py = -(y - 18 - 0.5) * MPT;
		Point loc = new Point(px, 1, py);
		c.setLoc(loc);
		int i = 0;
		for(List<Vector> l : dirs) {
			c.setDir(l.get(0));
			falseRender(c, v, loc, l.get(0), l.get(1), l.get(2));
			System.out.println("\t" + i);
			i++;
		}
		
		return v;
	}
	
	private static void falseRender(AnalysisCamera c, List<ThreeDeeObject> vis, Point loc, Vector dir, Vector rightU, Vector upU) {
		for(int x = 0; x < SC_WIDTH; x+=R_INC){
			for(int y = 0; y < SC_HEIGHT; y+=R_INC){
				Vector v = dir.add(c.getVectorForPixel(x+R_INC/2, y+R_INC/2, rightU, upU));
				ThreeDeeObject o = c.closestInFront(v, loc).o;
				if(o != null && !vis.contains(o)) {
					vis.add(o);
				}
			}
		}
	}
	
	private static List<List<Vector>> createDirs() {
		List<List<Vector>> dirs = new ArrayList<List<Vector>>(5);
		
		double yaw = 0;
		
		for(int i = 0; i < 5; i++){
			List<Vector> l = new ArrayList<Vector>(3);
			dirs.add(l);
			Vector d = Vector.fromPolarTransform(yaw, 0, 1);
			Vector upU = Vector.fromPolarTransform(yaw, PI/2, 1);
			Vector rightU = Vector.fromPolarTransform(yaw - PI/2, 0, 1);
			
			l.add(d);
			l.add(upU);
			l.add(rightU);
			yaw += FOV;
		}
		return dirs;
	}
}
