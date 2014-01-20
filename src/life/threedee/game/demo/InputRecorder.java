package life.threedee.game.demo;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.game.Game;
import life.threedee.game.GameUtilities;
import life.threedee.game.Input;
import life.threedee.game.Player;

public class InputRecorder extends Input{

	// the start time
	private final long seed;
	
	// the list of all actions taken to date
	private List<Action> actions;
	
	/**
	 * ctor with the seed value to ensure its the same
	 * @param p
	 * @param g
	 * @param j
	 * @param seed
	 */
	public InputRecorder(Player p, Game g, JFrame j, long seed) {
		super(p, g, j);
		this.seed = seed;
		actions = new ArrayList<Action>();
	}

	/**
	 * Serializes this to the outputstream
	 * @param s
	 * @throws IOException
	 */
	public void serialize(OutputStream s) throws IOException {
		GameUtilities.writeLong(s, seed);
		GameUtilities.writeInt(s, actions.size());
		for(int i = 0; i < actions.size(); i++) {
			actions.get(i).serialize(s);
		}/*
		Data d = new Data(seed, actions);
		ObjectOutputStream o = new ObjectOutputStream(s);
		o.writeObject(d);
		o.close();*/
		s.close();
	}
	
	/**
	 * Captures the action and propogates it
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!mouseCaptured){
			return;
		}
        if(e.getX() != j.getWidth() / 2 || e.getY() != j.getHeight() / 2){
        	actions.add(new Action(0, e.getID(), System.currentTimeMillis(), e.getX(), e.getY()));
			super.mouseMoved(e);
		}
	}

	/**
	 * Captures the action and propogates it
	 */
	@Override
	public void keyPressed(KeyEvent e){
		actions.add(new Action(1, e.getID(), System.currentTimeMillis(), e.getKeyCode(), (int) e.getKeyChar()));
		super.keyPressed(e);
	}

	/**
	 * Captures the action and propogates it
	 */
	@Override
	public void keyReleased(KeyEvent e){	
		actions.add(new Action(2, e.getID(), System.currentTimeMillis(), e.getKeyCode(), (int) e.getKeyChar()));
		super.keyReleased(e);
	}
}
