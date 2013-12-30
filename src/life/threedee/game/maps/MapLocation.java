package life.threedee.game.maps;

import static life.threedee.game.GameUtilities.*;

import life.threedee.Point;

public class MapLocation {
	
	/**
	 * Objects used as keys in hashmaps should probably be immutable, come to think of it.
	 */
	private final int mx, my;
	
	public MapLocation(double x, double z) {
		this.mx = (int) (x + 14);
		this.my = (int) (-z + 18);
	}
	
	public MapLocation(int mx, int my) {
		this.mx = mx;
		this.my = my;
	}
	
	@Override
	public boolean equals(Object o) {
		try{
			MapLocation m = (MapLocation) o;
			return this.mx == m.mx && this.my == m.my;
		}
		catch(ClassCastException e){
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return mx * 36 + my;
	}
}
