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
public class InputPlayback extends Input{
	
	/**
	 * Standard ctor with the inputstream to deserialize from
	 * @param p
	 * @param g
	 * @param j
	 * @param i
	 * @throws IOException
	 */
	public InputPlayback(Player p, Game g, JFrame j){
		super(p, g, j);
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
}
