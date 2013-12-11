package life.threedee;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
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
	
	public POC3D(){
		j = new JFrame("Proof of Concept");

		this.setPreferredSize(new Dimension(480,360));

		j.add(this);
		j.pack();
		j.setVisible(true);
		j.setResizable(false);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void run(){
		Toolkit tk= getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		this.setCursor(transparent);
		objects = new ArrayList<Plane>();
		Plane p0 = new Plane(new Point(0,2,0),new Vector(0,1,0),Color.red);
		Plane p1 = new Plane(new Point(2,0,2),new Vector(1,0,0),Color.black);
		Plane p2 = new Plane(new Point(-2,0,2),new Vector(1,0,0),Color.blue);
		Plane p3 = new Plane(new Point(0,0,5),new Vector(0,0,1),Color.cyan);
		Plane p4 = new Plane(new Point(0,0,0),new Vector(0,1,0),Color.orange);
		Plane p5 = new Plane(new Point(0,0,-5),new Vector(0,0,1),Color.green);
		objects.add(p0);
		objects.add(p1);
		objects.add(p2);
		objects.add(p3);
		objects.add(p4);
		objects.add(p5);
		//Plane p1 = new Plane(new Point(0,0,0),new Vector(0,1,0));
		//objects.add(p1);
		c = new Camera();
		while(true){
			long startT = System.currentTimeMillis();
			c.draw(this.getGraphics(),objects);
			long time = System.currentTimeMillis() - startT;
			System.out.println(time);
			try{
				Thread.sleep((int) Math.max(0,66 - time));
			}
			catch (InterruptedException e){
				e.printStackTrace();
			}
			System.out.println("frame");
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
	
	class MouseMovementListener implements MouseListener,MouseMotionListener{

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
		
	}
}
