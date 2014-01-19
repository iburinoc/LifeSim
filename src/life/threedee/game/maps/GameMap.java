package life.threedee.game.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.game.Consumable;
import life.threedee.game.Energizer;
import life.threedee.game.Pellet;

/**
 * Contains the representation of the pacman map as one big list as well as a HashMap of lists to allow for fast access to the sublists containing whats visible from each tile
 * @author Sean
 *
 */
public class GameMap {
	
	/**
	 * The big list of objects containing the entire map
	 */
	private List<ThreeDeeObject> objects;
	
	/**
	 * HashMap containing a mapping from MapLocations in the map to the list of planes visible from there
	 */
	private Map<MapLocation, List<ThreeDeeObject>> map;
	
	/**
	 * The list of pellets in the map
	 */
	private List<Pellet> pelletsList;
	
	/**
	 * The list of energizers in the map
	 */
	private List<Energizer> energyList;
	
	/**
	 * The general list of consumables in the map
	 */
	private List<Consumable> consumablesList;
	
	/**
	 * Game map constructor, creates the big list and then creates the HashMap of the sublists
	 */
	public GameMap() {
		objects = MapBuilder.createMap();
		map = MapBuilder.deserializeMap(objects);
	}
	
	/**
	 * Getter for objects
	 * @return
	 */
	public List<ThreeDeeObject> getObjects() {
		return objects;
	}
	
	/**
	 * Gets the sublist for that given point
	 * @param pos
	 * @return
	 */
	public List<ThreeDeeObject> getObjects(Point pos) {
		return map.get(new MapLocation(pos.x, pos.z));
	}
	
	/**
	 * Getter for pellets list
	 * @return
	 */
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
	
	/**
	 * Getter for energyList
	 * @return
	 */
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
	
	/**
	 * Getter for consumableList
	 * @return
	 */
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
