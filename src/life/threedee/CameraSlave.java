package life.threedee;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

public class CameraSlave extends Thread{
	private Camera master;
	
	private int x1,y1,x2,y2;

	private boolean running;
	
	private boolean job;
	private Graphics g;
	
	private List<Plane> objects;
	
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
		this.offG = buffer.getGraphics();
		this.setDaemon(true);
	}
	
	@Override
	public void run(){
		while(running){
			try{
				Thread.sleep(1000);
			}
			catch (InterruptedException e){
				
			}
			if(job){
				master.drawRange(offG, objects, x1, y1, x2, y2, x1, rightU, upU);
				master.threadDone();
				job = false;
			}
		}
	}
	
	public void halt(){
		running = false;
	}
	
	public void draw(Graphics g, List<Plane> objects, Vector rightU, Vector upU){
		job = true;
		this.g = g;
		this.objects = objects;
		this.rightU = rightU;
		this.upU = upU;
		this.interrupt();
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
