package life.threedee.game.demo;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public final long seed;
	public final List<Action> actions;
	
	public Data(long seed, List<Action> actions) {
		super();
		this.seed = seed;
		this.actions = actions;
	}
}
