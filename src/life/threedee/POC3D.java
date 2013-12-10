package life.threedee;

import java.awt.Dimension;
import java.awt.Graphics;
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
	}

	@Override
	public void run(){
		Plane p = new Plane(new Point(0,0,1),new Vector(0,-1,1));
		objects.add(p);
		c = new Camera();
	}
	
	@Override
	public void paintComponent(Graphics g){
		
	}

	public static void main(String[] args){
		new Thread(new POC3D()).start();
	}
}
