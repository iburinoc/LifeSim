package life.threedee;

import life.threedee.game.Player;

import java.awt.AWTException;
import java.awt.Color;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class POC3D implements Runnable{

	private JFrame j;
	private List<ThreeDeeObject> objects;

	private double x;
	private double y;
	private double z;

	private Player p;
	
	private MouseMovementListener m;
	
	private boolean w,d,s,a,up;
	
	public Thread main;
	
	public POC3D(){
		j = new JFrame("Proof of Concept");

		p = new Player();
		p.setPreferredSize(new Dimension(p.screenWidth, p.screenHeight));

		j.add(p);
		j.pack();
		j.setVisible(true);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m = new MouseMovementListener();
		j.addMouseListener(m);
		j.addMouseMotionListener(m);
		j.addKeyListener(m);
		j.addMouseWheelListener(m);
	}

	@Override
	public void run(){
		Toolkit tk= p.getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		j.setCursor(transparent);
		objects = new ArrayList<ThreeDeeObject>();
		Plane p0 = new Plane(new Point(0,15,0),new Vector(0,1,0),Color.red);
		Plane p1 = new Plane(new Point(20,0,20),new Vector(1,0,0),Color.black);
		Plane p2 = new Plane(new Point(-20,0,20),new Vector(1,0,0),Color.blue);
		Plane p3 = new Plane(new Point(0,0,20),new Vector(0,0,1),Color.cyan);
		Plane p4 = new Plane(new Point(0,0,0),new Vector(0,1,0),Color.orange);
		Plane p5 = new Plane(new Point(0,0,-20),new Vector(0,0,1),Color.green);
		{
			Point a = new Point(0,0,0), b = new Point(0,1,0), c = new Point(1,0,0);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			this.p.add(p6);
		}
		{
			Point a = new Point(5,1,5), b = new Point(5,1,1.5), c = new Point(1,0,5);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			this.p.add(p6);
		}
		{
			Point a = new Point(0,1,0), b = new Point(0,1,1), c = new Point(1,1,0);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			this.p.add(p6);
		}
		{
			Point a = new Point(1,1,0), b = new Point(1,1,1), c = new Point(0,1,1);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			this.p.add(p6);
		}
		{
            WorldObject wo = WorldObject.generateObject("(2, 2, 2);"
                    + "((1,1,1),(3,1,1), (2, 1, 3)); "
                    + "((2,3,2),  (3,1,1),  (2,1, 3))  ;"
                    + "((1,1,   1),(2 ,3,2), (2, 1,  3));"
                    + "((1,1\t,1),(3,   1,1), (2, 3, 2))");
            this.p.add(wo);
        }
		this.p.add(p0);
		this.p.add(p1);
		this.p.add(p2);
		this.p.add(p3);
		this.p.add(p4);
		this.p.add(p5);
		//Plane p1 = new Plane(new Point(0,0,0),new Vector(0,1,0));
		//objects.add(p1);
//		this.p.addTickable(new BulletGun(p));
		while(true) {
			long startT = System.currentTimeMillis();
			p.repaint();
			long time = System.currentTimeMillis() - startT;
//			System.out.println(time);
			try{
				Thread.sleep((int) Math.max(0,66 - time));
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
			//			System.out.println("frame");
			if(w){
				p.move(0);
			}if(a){
				p.move(1);
			}if(s){
				p.move(2);
			}if(d){
				p.move(3);
			}if(up){
                System.out.println("boolean cleared 1");
				p.jump();
			}
//			System.out.println("frame");
            if(w){
                p.move(0);
            }if(a){
                p.move(1);
            }if(s){
                p.move(2);
            }if(d){
                p.move(3);
            }if(up){
                System.out.println("boolean cleared 2");
                p.jump();
            }
		}
	}

	public static void main(String[] args){
		new Thread(new POC3D()).start();
	}
	
	class MouseMovementListener implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener{

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
            }else if(e.getKeyChar() == ' '){
                up = true;
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
            }else if(e.getKeyChar() == ' '){
                up = false;
            }
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e){
			//System.out.println(e.getPreciseWheelRotation());
			p.scroll(e.getWheelRotation());
		}
		
	}
}
