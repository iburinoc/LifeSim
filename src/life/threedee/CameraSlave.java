package life.threedee;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CameraSlave extends JPanel{
	private Camera master;
	
	private int x1,y1,x2,y2;

	private boolean running;
	
	private boolean job;
	private Graphics g;
	
	private Vector rightU;
	private Vector upU;
	
	private BufferedImage buffer;
	private Graphics offG;
	
	public CameraSlave(Camera master, int x1, int y1, int x2, int y2){
		super();
		this.master = master;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.running = true;
		this.buffer = new BufferedImage(x2-x1, y2-y1, BufferedImage.TYPE_INT_ARGB);
		this.setPreferredSize(new Dimension(x2 - x1, y2 - y1));
		this.offG = buffer.getGraphics();
	}
	
	@Override
	public void paintComponent(Graphics g){
		master.drawRange(g, x1, y1, x2, y2, x1);
	}
	
	public Image getBuffer(){
		return buffer;
	}
	
	public int getX(){
		return x1;
	}
	
	public int getY(){
		return y1;
	}
}
