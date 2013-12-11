package life.threedee;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class POC3D extends JPanel implements Runnable{

	private JFrame j;
	private List<Plane> objects;

	private double x;
	private double y;
	private double z;

	private Camera c;
	
	private MouseMovementListener m;
	
	private boolean w,d,s,a;
	
	public POC3D(){
		j = new JFrame("Proof of Concept");

		this.setPreferredSize(new Dimension(960,720));

		j.add(this);
		j.pack();
		j.setVisible(true);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m = new MouseMovementListener();
		this.addMouseListener(m);
		this.addMouseMotionListener(m);
		j.addKeyListener(m);
		
	}

	@Override
	public void run(){
		Toolkit tk= getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
//		this.setCursor(transparent);
		objects = new ArrayList<Plane>();
		Plane p0 = new Plane(new Point3D(0,2,0),new Vector(0,1,0),Color.red);
		Plane p1 = new Plane(new Point3D(20,0,20),new Vector(1,0,0),Color.black);
		Plane p2 = new Plane(new Point3D(-20,0,20),new Vector(1,0,0),Color.blue);
		Plane p3 = new Plane(new Point3D(0,0,20),new Vector(0,0,1),Color.cyan);
		Plane p4 = new Plane(new Point3D(0,0,0),new Vector(0,1,0),Color.orange);
		Plane p5 = new Plane(new Point3D(0,0,-20),new Vector(0,0,1),Color.green);
		objects.add(p0);
		objects.add(p1);
		objects.add(p2);
		objects.add(p3);
		objects.add(p4);
		objects.add(p5);
		//Plane p1 = new Plane(new Point3D(0,0,0),new Vector(0,1,0));
		//objects.add(p1);
		c = new Camera();
		while(true){
			long startT = System.currentTimeMillis();
			c.draw(this.getGraphics(),objects);
			long time = System.currentTimeMillis() - startT;
//			System.out.println(time);
			try{
				Thread.sleep((int) Math.max(0,66 - time));
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
//			System.out.println("frame");
			m.recenter();
			if(w){
				c.move(0);
			}if(d){
				c.move(1);
			}if(s){
				c.move(2);
			}if(a){
				c.move(3);
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(c != null)
			c.draw(g, objects);
	}

	public static void main(String[] args){
		new Thread(new POC3D()).start();
	}
	
	class MouseMovementListener implements MouseListener,MouseMotionListener, KeyListener{

		private boolean mouseCaptured;
		
		private Robot recenter;
		
		private int oldX, oldY;
	
		private boolean reset, recentered;
		
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
			if(mouseCaptured && false){
				recentered = true;
				recenter.mouseMove(j.getX() + j.getWidth()/2, j.getY() + j.getHeight()/2);
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			mouseMoved(arg0);
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			if(!mouseCaptured || recentered){
				recentered = false;
				return;
			}
			if(reset){
				oldX = arg0.getX();
				oldY = arg0.getY();
				reset = false;
				return;
			}
			reset = true;
			recenter();
			c.mouseMoved(arg0.getX()-oldX, arg0.getY()-oldY);
			oldX = arg0.getX();
			oldY = arg0.getY();
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
			reset = true;
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
			}else if(e.getKeyChar() == 'd'){
				d = true;
			}else if(e.getKeyChar() == 's'){
				s = true;
			}else if(e.getKeyChar() == 'a'){
				a = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e){	
			if(e.getKeyChar() == 'w'){
				w = false;
			}else if(e.getKeyChar() == 'd'){
				d = false;
			}else if(e.getKeyChar() == 's'){
				s = false;
			}else if(e.getKeyChar() == 'a'){
				a = false;
			}
		}
		
	}
}
