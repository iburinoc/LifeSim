package life.threedee.game;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The input class. This accepts all user input, and acts on it.
 * 
 * @author Andrey Boris Khesin
 * @author Dmitry Andreevich Paramonov
 * @author Sean Christopher Papillon Purcell
 *
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener{
    // The player to whom this input will apply.
	private Player p;
	// The game for which this input will apply. 
	private Game g;
	/**
	 * The JFrame from which this input will arrive.
	 */
	protected JFrame j;

	/**
	 * 
	 */
	protected boolean mouseCaptured;

	private Robot recenter;

	public Input(Player p, Game g, JFrame j){
		this();
		this.p = p;
		this.g = g;
		this.j = j;
	}
	
	private Input(){
		try{
			recenter = new Robot();
		}
		catch (AWTException e){
			e.printStackTrace();
		}
		mouseCaptured = true;
		recenter();
	}
	
	public void recenter(){
		if(j == null)
			return;
		if(mouseCaptured){
			java.awt.Point p = new java.awt.Point(j.getWidth() / 2, j.getHeight() / 2);
			SwingUtilities.convertPointToScreen(p, j);
			recenter.mouseMove(p.x,p.y);
			
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		mouseMoved(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if(!mouseCaptured){
			return;
		}
        if(arg0.getX() != j.getWidth() / 2 || arg0.getY() != j.getHeight() / 2){
			p.mouseMoved(arg0.getX() - j.getWidth() / 2, arg0.getY() - j.getHeight() / 2);
			recenter();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		//mouseCaptured = true;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent e){
		
	}

	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == 27){
			this.mouseCaptured = !mouseCaptured;
		}
		if(e.getKeyChar() == 'w' || e.getKeyCode() == 38){
			p.w = true;
		}else if(e.getKeyChar() == 'a' || e.getKeyCode() == 37){
			p.a = true;
		}else if(e.getKeyChar() == 's' || e.getKeyCode() == 40){
			p.s = true;
		}else if(e.getKeyChar() == 'd' || e.getKeyCode() == 39){
            p.d = true;
        }else if(e.getKeyChar() == '\\' && GameUtilities.DEVELOPER_MODE){
            g.pelletEaten();
        }else if(e.getKeyChar() == '=' && GameUtilities.DEVELOPER_MODE){
            g.rackTest();
        }else if(e.getKeyChar() == '`' && GameUtilities.DEVELOPER_MODE){
            g.startFrightened();
        }else if(e.getKeyChar() == 'r' && GameUtilities.DEVELOPER_MODE){
            g.die();
        }else if(e.getKeyChar() == ','){
            GameUtilities.rIncSet(false);
        }else if(e.getKeyChar() == '.'){
            GameUtilities.rIncSet(true);
        }
		g.keyPressed(e.getKeyCode(), e.getKeyChar());
	}

	@Override
	public void keyReleased(KeyEvent e){	
		if(e.getKeyChar() == 'w' || e.getKeyCode() == 38){
			p.w = false;
		}else if(e.getKeyChar() == 'a' || e.getKeyCode() == 37){
			p.a = false;
		}else if(e.getKeyChar() == 's' || e.getKeyCode() == 40){
			p.s = false;
		}else if(e.getKeyChar() == 'd' || e.getKeyCode() == 39){
            p.d = false; 
        }
	}
}
