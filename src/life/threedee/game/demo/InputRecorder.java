package life.threedee.game.demo;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.game.Game;
import life.threedee.game.GameUtilities;
import life.threedee.game.Input;
import life.threedee.game.Player;

public class InputRecorder extends Input{

	private final long seed;
	
	private List<Action> actions;
	
	public InputRecorder(Player p, Game g, JFrame j, long seed) {
		super(p, g, j);
		this.seed = seed;
		actions = new ArrayList<Action>();
	}

	public void serialize(OutputStream s) throws IOException {
		GameUtilities.writeLong(s, seed);
		GameUtilities.writeInt(s, actions.size());
		for(int i = 0; i < actions.size(); i++) {
			actions.get(i).serialize(s);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if(!mouseCaptured){
			return;
		}
        if(e.getX() != j.getWidth() / 2 || e.getY() != j.getHeight() / 2){
			actions.add(new Action(0, e.getID(), e.getWhen(), e.getX(), e.getY()));
			super.mouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e){
		actions.add(new Action(1, e.getID(), e.getWhen(), e.getKeyCode(), (int) e.getKeyChar()));
		super.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e){	
		actions.add(new Action(2, e.getID(), e.getWhen(), e.getKeyCode(), (int) e.getKeyChar()));
		super.keyReleased(e);
	}
}
