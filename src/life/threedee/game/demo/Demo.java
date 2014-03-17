package life.threedee.game.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import life.threedee.game.GameUtilities;
import life.threedee.game.Player;

/**
 * Class containing the action data to be serialized
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 * 
 */
public class Demo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/**
	 * The seed
	 */
	public final long seed;
	
	/**
	 * The list of actions to be serialized
	 */
	public final List<PData> actions;
	
	/**
	 * ctor.  what else can i say
	 * @param seed
	 * @param actions
	 */
	public Demo(long seed) {
		super();
		this.seed = seed;
		this.actions = new ArrayList<PData>();
	}
	
	private Demo(long seed, List<PData> actions) {
		this.seed = seed;
		this.actions = actions;
	}
	
	public void recordTick(Player p) {
		actions.add(new PData(p.getDir(), p.getLoc()));
	}
	
	public void replayTick(Player p, long tickNum) {
		if(tickNum >= actions.size()) {
			return;
		}
	 	PData d = actions.get((int) tickNum);
	 	p.setDir(d.dir);
	 	p.setLoc(d.loc);
	}
	
	public void serialize(OutputStream o) throws IOException {
		GameUtilities.writeLong(o, seed);
		GameUtilities.writeInt(o, actions.size());
		for(int i = 0; i < actions.size(); i++) {
			actions.get(i).serialize(o);
		}
	}
	
	public static Demo deserialize(InputStream i) throws IOException {
		long seed = GameUtilities.readLong(i);
		int len = GameUtilities.readInt(i);
		List<PData> actions = new ArrayList<PData>(len);
		for(int j = 0; j < len; j++) {
			actions.add(PData.deserialize(i));
		}
		return new Demo(seed, actions);
	}
}
