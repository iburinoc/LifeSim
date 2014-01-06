package life.threedee.game.maps;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import life.threedee.ThreeDeeObject;
import life.threedee.game.GameUtilities;
import life.threedee.game.Player;

public class MapTest implements Runnable{

	private JFrame j;
	private List<ThreeDeeObject> objects;

	private double x;
	private double y;
	private double z;

	private Player p;
	
	private MouseMovementListener m;
	
	private boolean w,d,s,a;
	
	public Thread main;
	
	public MapTest(){
		j = new JFrame("Map Test");

		p = new Player(null, new GameMap());
		p.setPreferredSize(new Dimension(GameUtilities.SC_WIDTH, GameUtilities.SC_HEIGHT));

		j.add(p);
		j.pack();
		j.setVisible(true);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m = new MouseMovementListener();
		j.addMouseListener(m);
		j.addMouseMotionListener(m);
		j.addKeyListener(m);
	}

	@Override
	public void run(){
		Toolkit tk= p.getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		j.setCursor(transparent);
		//this.p.setObjects(MapBuilder.createMap());
		while(true) {
			long startT = System.currentTimeMillis();
			p.calcBuffer();
			//p.repaint();
			paint();
			long time = System.currentTimeMillis() - startT;
			System.out.println(time);
			try{
				Thread.sleep(Math.max(0, 33-time));
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
			//System.out.println("frame");
		}
	}

	private void paint() {
		p.repaint();
		p.registerWait(Thread.currentThread());
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
			
		}
		System.out.println("frame");
	}
	
	public static void main(String[] args){
		new Thread(new MapTest()).start();
	}
	
	class MouseMovementListener implements MouseListener, MouseMotionListener, KeyListener{

		private boolean mouseCaptured;
		
		private Robot recenter;
		
		
		public MouseMovementListener(){
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
			
			System.out.println(arg0.getX() + ";" + arg0.getY() + ";" + j.getWidth() / 2 + ";" + j.getHeight() / 2);
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
			System.out.println("Key:" + e.getKeyCode());
			if(e.getKeyCode() == 27){
				this.mouseCaptured = !mouseCaptured;
			}
			if(e.getKeyChar() == 'w'){
				w = true;
			}else if(e.getKeyChar() == 'a'){
				a = true;
			}else if(e.getKeyChar() == 's'){
				s = true;
			}else if(e.getKeyChar() == 'd'){
                d = true;
            }
		}

		@Override
		public void keyReleased(KeyEvent e){	
			if(e.getKeyChar() == 'w'){
				w = false;
			}else if(e.getKeyChar() == 'a'){
				a = false;
			}else if(e.getKeyChar() == 's'){
				s = false;
			}else if(e.getKeyChar() == 'd'){
                d = false;
            }
		}
	}
}
