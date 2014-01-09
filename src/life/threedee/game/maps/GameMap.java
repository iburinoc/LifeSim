package life.threedee.game.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.game.Consumable;
import life.threedee.game.Energizer;
import life.threedee.game.Pellet;

public class GameMap {
	private List<ThreeDeeObject> objects;
	private Map<MapLocation, List<ThreeDeeObject>> map;
	
	private List<Pellet> pelletsList;
	private List<Energizer> energyList;
	
	private List<Consumable> consumablesList;
	
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
	
	public List<Pellet> pelletsList() {
		if(pelletsList == null) {
			pelletsList = new ArrayList<Pellet>();
			for(ThreeDeeObject o : objects) {
				if(o instanceof Pellet) {
					pelletsList.add((Pellet) o);
				}
			}
		}
		return pelletsList;
	}
	
	public List<Energizer> energyList() {
	    if(energyList == null) {
            energyList = new ArrayList<Energizer>();
            for(ThreeDeeObject o : objects) {
                if(o instanceof Energizer) {
                    energyList.add((Energizer) o);
                }
            }
        }
        return energyList;
	}
	
	public List<Consumable> consumableList() {
		if(consumablesList == null) {
			consumablesList = new ArrayList<Consumable>();
			for(ThreeDeeObject o : objects) {
				if(o instanceof Consumable) {
					consumablesList.add((Consumable) o);
				}
			}
		}
		return consumablesList;
	}
}
