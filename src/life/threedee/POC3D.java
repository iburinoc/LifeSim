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
import javax.swing.SwingUtilities;

public class POC3D extends JPanel implements Runnable{

	private JFrame j;
	private List<ThreeDeeObject> objects;

	private double x;
	private double y;
	private double z;

	private Camera c;
	
	private MouseMovementListener m;
	
	private boolean w,d,s,a;
	
	public Thread main;
	
	public POC3D(){
		j = new JFrame("Proof of Concept");

		this.setPreferredSize(new Dimension(960,720));

		j.add(this);
		j.pack();
		j.setVisible(true);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = new Camera();
		m = new MouseMovementListener();
		this.addMouseListener(m);
		this.addMouseMotionListener(m);
		j.addKeyListener(m);
	}

	@Override
	public void run(){
		Toolkit tk= getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		this.setCursor(transparent);
		objects = new ArrayList<ThreeDeeObject>();
		Plane p0 = new Plane(new Point3D(0,5,0),new Vector(0,1,0),Color.red);
		Plane p1 = new Plane(new Point3D(20,0,20),new Vector(1,0,0),Color.black);
		Plane p2 = new Plane(new Point3D(-20,0,20),new Vector(1,0,0),Color.blue);
		Plane p3 = new Plane(new Point3D(0,0,20),new Vector(0,0,1),Color.cyan);
		Plane p4 = new Plane(new Point3D(0,0,0),new Vector(0,1,0),Color.orange);
		Plane p5 = new Plane(new Point3D(0,0,-20),new Vector(0,0,1),Color.green);
		{
			Point3D a = new Point3D(0,0,0), b = new Point3D(0,1,0.0000000000001), c = new Point3D(1,0,0);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			objects.add(p6);
		}
		{
			Point3D a = new Point3D(5,1,5), b = new Point3D(5,1,1.5), c = new Point3D(1,0,5);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			objects.add(p6);
		}
		{
			Point3D a = new Point3D(0,1,0), b = new Point3D(0,1,1), c = new Point3D(1,1,0);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			objects.add(p6);
		}
		{
			Point3D a = new Point3D(1,1,0), b = new Point3D(1,1,1), c = new Point3D(0,1,1);
			
			Plane p6 = new Triangle(a,b,c,Color.pink);
			objects.add(p6);
		}
		{
            WorldObject wo = WorldObject.generateObject("(2, 2, 2);"
                    + "((1,1,1),(3,1,1), (2, 1, 3)); "
                    + "((2,3,2),  (3,1,1),  (2,1, 3))  ;"
                    + "((1,1,   1),(2 ,3,2), (2, 1,  3));"
                    + "((1,1\t,1),(3,   1,1), (2, 3, 2))");
            objects.add(wo);
        }
		objects.add(p0);
		objects.add(p1);
		objects.add(p2);
		objects.add(p3);
		objects.add(p4);
		objects.add(p5);
		//Plane p1 = new Plane(new Point3D(0,0,0),new Vector(0,1,0));
		//objects.add(p1);
		while(true){
			long startT = System.currentTimeMillis();
			this.repaint();
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
				java.awt.Point p = new java.awt.Point(c.screenWidth / 2, c.screenHeight / 2);
				SwingUtilities.convertPointToScreen(p, POC3D.this);
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
			
			System.out.println(arg0.getX() + ";" + arg0.getY());
			if(arg0.getX() != c.screenWidth / 2 || arg0.getY() != c.screenHeight / 2)
				c.mouseMoved(arg0.getX()-c.screenWidth / 2, arg0.getY()-c.screenHeight / 2);
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
