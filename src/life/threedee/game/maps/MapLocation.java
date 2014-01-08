package life.threedee.game.maps;

import life.threedee.Point;

public class MapLocation {
	
	/**
	 * Objects used as keys in hashmaps should probably be immutable, come to think of it.
	 */
	public final int mx, my;

    public MapLocation(Point point){
        this(point.x, point.z);
    }
	
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
			return (this.mx == m.mx && this.my == m.my) || (!this.inRange() && !m.inRange());
		}
		catch(ClassCastException e){
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		if(inRange())
			return mx * 36 + my;
		else
			return -1;
	}
	
	public boolean inRange(){
		return mx >= 0 && mx < 28 && my >= 0 && my < 36;
	}
}

