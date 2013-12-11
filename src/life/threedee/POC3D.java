package life.threedee;

import java.awt.Dimension;
import java.awt.Graphics;
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
		objects = new ArrayList<Plane>();
		Plane p0 = new Plane(new Point(0,0,1),new Vector(1,0,1));
		Plane p1 = new Plane(new Point(0,0,1),new Vector(-1,0,1));
		objects.add(p0);
		//objects.add(p1);
		//Plane p1 = new Plane(new Point(0,0,0),new Vector(0,1,0));
		//objects.add(p1);
		c = new Camera();
		while(true){
			c.draw(j.getGraphics(),objects);
			
			try{
				Thread.sleep(1000);
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
}
