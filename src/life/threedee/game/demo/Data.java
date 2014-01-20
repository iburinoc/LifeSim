package life.threedee.game.demo;

import java.io.Serializable;
import java.util.List;

/**
 * Class containing the action data to be serialized
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 * 
 */
public class Data implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The seed
	 */
	public final long seed;
	
	/**
	 * The list of actions to be serialized
	 */
	public final List<Action> actions;
	
	/**
	 * ctor.  what else can i say
	 * @param seed
	 * @param actions
	 */
	public Data(long seed, List<Action> actions) {
		super();
		this.seed = seed;
		this.actions = actions;
	}
}
