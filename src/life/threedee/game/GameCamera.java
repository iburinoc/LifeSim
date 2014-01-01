package life.threedee.game;

import life.threedee.Camera;
import life.threedee.game.maps.GameMap;


public class GameCamera extends Camera{
	
	private GameMap m;
	
	public GameCamera(GameMap m) {
		this.m = m;
	}
	
	@Override
	public void calcBuffer() {
		this.objects = m.getObjects(this.loc);
	}
}
