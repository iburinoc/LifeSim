package life.threedee.game.maps;

import java.util.List;
import java.util.Map;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;

public class GameMap {
	private List<ThreeDeeObject> objects;
	private Map<MapLocation, List<ThreeDeeObject>> map;
	
	public GameMap() {
		objects = MapBuilder.createMap();
		map = MapBuilder.deserializeMap(objects);
	}
	
	public List<ThreeDeeObject> getObjects() {
		return objects;
	}
	
	public List<ThreeDeeObject> getObjects(Point pos) {
		return map.get(new MapLocation(pos.x, pos.z));
	}
}
