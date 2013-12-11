package life.threedee;

import java.awt.Graphics;
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
	
	public CameraSlave(Camera master, int x1, int y1, int x2, int y2){
		super();
		this.master = master;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.running = true;
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
				master.drawRange(g, objects, x1, y1, x2, y2, rightU, upU);
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
}
