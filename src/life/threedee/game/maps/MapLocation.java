package life.threedee.game.maps;

import life.threedee.Point;

public class MapLocation {
	
	/**
	 * The coordinates of this map location
	 */
	public final int mx, my;

	/**
	 * Constructs a map location from the given point
	 * @param point
	 */
    public MapLocation(Point point){
        this(point.x, point.z);
    }
	
    /**
     * Constructs a map location from the given x and z by converting from world locations to map locations
     * @param x
     * @param z
     */
	public MapLocation(double x, double z) {
		this.mx = (int) ((x + 42) % 28);
		this.my = (int) (-z + 18);
	}
	
	/**
	 * Constructs a map location from the given values
	 * @param mx
	 * @param my
	 */
	public MapLocation(int mx, int my) {
		this.mx = mx;
		this.my = my;
	}
	
	/**
	 * Tests if this has identical values to or they are both outside of range
	 */
	@Override
	public boolean equals(Object o) {
		try{
			MapLocation m = (MapLocation) o;
			return this.hashCode() == o.hashCode();
		}
		catch(ClassCastException e){
            e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns a hashcode, which is unique for any location inside the map, and -1 for all values outside
	 */
	@Override
	public int hashCode() {
		if(inRange())
			return mx * 36 + my;
		else
			return -1;
	}
	
	/**
	 * Returns whether this map location is inside the actual pacman map
	 * @return
	 */
	public boolean inRange(){
		return mx >= 0 && mx < 28 && my >= 0 && my < 36;
	}
}

