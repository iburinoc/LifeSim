package life.threedee;

public final class CameraSlave extends Thread{
	private Camera master;
	
	private int x1,y1,x2,y2;

	private boolean running;
	
	private boolean job;
	private boolean done;
	
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
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			if(job) {
				master.drawRange(x1, y1, x2, y2);
				setDone(true);
			}
		}
	}
	
	public void draw(){
		setDone(false);
		job = true;
		this.interrupt();
	}
	
	public synchronized boolean done(){
		return done;
	}
	
	private synchronized void setDone(boolean val){
		done = val;
	}
	
	public int getX(){
		return x1;
	}
	
	public int getY(){
		return y1;
	}
}
