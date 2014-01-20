package life.threedee.game.demo;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.game.Game;
import life.threedee.game.Input;
import life.threedee.game.Player;

/**
 * A version of Input that plays back 
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class InputPlayback extends Input implements Runnable{

	// the seed
	private long seed;
	// the start time
	private long start;
	// the list of actions to be replayed
	private List<Action> actions;
	
	/**
	 * Standard ctor with the inputstream to deserialize from
	 * @param p
	 * @param g
	 * @param j
	 * @param i
	 * @throws IOException
	 */
	public InputPlayback(Player p, Game g, JFrame j, InputStream i) throws IOException{
		super(p, g, j);
		deserialize(i);
	}
	
	private void deserialize(InputStream s) throws IOException{
		/*try {
			seed = GameUtilities.readLong(s);
			int num = GameUtilities.readInt(s);
			
			actions = new ArrayList<Action>(num);
			for(int i = 0; i < num; i++) {
				actions.add(Action.deserialize(s));
			}
			if(s.available() != 0) {
				System.err.println("Not all data read");
			}
			s.close();
		}
		catch(IOException e) {
			System.err.println("Not valid file");
			throw e;
		}
		*/
		try{
			ObjectInputStream o = new ObjectInputStream(s);
			Data d = (Data) o.readObject();
			this.seed = d.seed;
			this.actions = d.actions;
			o.close();
			s.close();
		}
		catch(ClassNotFoundException e) {

		}
	}
	
	/**
	 * Iterates through each action and executes it
	 */
	@Override
	public void run() {
		start = System.currentTimeMillis();
		for(int i = 0; i < actions.size(); i++) {
			Action a = actions.get(i);
			try{
				Thread.sleep(Math.max(0, ((a.when - seed + start) - System.currentTimeMillis())));
			}
			catch(InterruptedException e) {
				
			}
			switch(a.type) {
			case 0:
				super.mouseMoved(new MouseEvent(this.j, a.id, a.when, 0, a.field1, a.field2, 1, false));
				break;
			case 1:
				super.keyPressed(new KeyEvent(this.j, a.id, a.when, 0, a.field1, (char) a.field2));
				break;
			case 2:
				super.keyReleased(new KeyEvent(this.j, a.id, a.when, 0, a.field1, (char) a.field2));
				break;
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e){
	}

	@Override
	public void keyReleased(KeyEvent e){	
	}
	
	/**
	 * Getter
	 * @return
	 */
	public long seed() {
		return seed;
	}
}
