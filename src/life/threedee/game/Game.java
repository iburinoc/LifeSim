package life.threedee.game;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import life.threedee.Point;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;
import life.threedee.game.maps.MapBuilder;

public class Game implements Runnable{
	public static final int TICK_RATE = 60;
	public static final int FRAME_RATE = 30;
	
	public static void main(String[] args){
		new Thread(new Game()).start();
	}
	
	private List<ThreeDeeObject> objects;
	private List<Tickable> tickables;

    private List<Ghost> ghosts;
	
	private Player p;
	private GameMap m;
	
	private JFrame j;
	
	private Input i;
	
	private boolean running;

    private int mode;
	
	public Game() {
		j = new JFrame("Game");
		
		m = new GameMap();
		
		p = new Player(this, m);
		
        ghosts = new ArrayList<Ghost>();
        ghosts.add(new Blinky(this));
        ghosts.add(new Pinky(this));
        ghosts.add(new Inky(this));
        ghosts.add(new Clyde(this));
		setObjects(new ArrayList<ThreeDeeObject>());
		setTickables(new ArrayList<Tickable>());
		
		i = new Input(p, this, j);
		
		running = true;
		
		j.addMouseListener(i);
		j.addMouseMotionListener(i);
		j.addKeyListener(i);
		
		j.add(p);
		j.pack();
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tickables.add(p);
	}
	
	private void removeCursor() {
		Toolkit tk= p.getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		j.setCursor(transparent);
	}
	
	private void betaObjects() {
		objects.addAll(MapBuilder.generateBetaScreenShotObjects());
	}
	
	@Override
	public void run() {
		betaObjects();
		removeCursor();
		
		long tick_time = System.currentTimeMillis();
		int tick_delta = 0;
		
		long frame_time = System.currentTimeMillis();
		int frame_delta = 0;
		
		int tickRateMillis = 1000/TICK_RATE;
		int frameRateMillis = 1000/FRAME_RATE;
		while(running) {
			long frameT = System.currentTimeMillis();
			frame_delta += (int) (frameT - frame_time);
			frame_time = frameT;
			if(frame_delta >= frameRateMillis) {
				drawFrame();
				frame_delta -= frameRateMillis;
			}
			
			long tickT = System.currentTimeMillis();
			tick_delta += (int) (tickT - tick_time);
			tick_time = tickT;
			if(tick_delta >= tickRateMillis) {
				tickTickables(tick_delta);
				tick_delta -= tickRateMillis;
			}
		}
	}
	
	private void drawFrame() {
		long startT = System.currentTimeMillis();
		p.calcBuffer();
		p.repaint();
		p.registerWait(Thread.currentThread());
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
		}
		System.out.println("frame");
		long time = System.currentTimeMillis() - startT;
		System.out.println(time);
	}
	
	private void tickTickables(int delta) {
		for(int i = 0; i < tickables.size(); i++){
			tickables.get(i).tick(delta);
		}
	}
	
	public void addTickable(Tickable t) {
		tickables.add(t);
	}
	
	public void setTickables(List<Tickable> l) {
		tickables = Collections.synchronizedList(l);
	}
	
	public List<Tickable> tickables() {
		return tickables;
	}
	
	public void addObject(ThreeDeeObject o) {
		objects.add(o);
	}
	
	public void setObjects(List<ThreeDeeObject> o){
		objects = Collections.synchronizedList(o);
	}
	
	public List<ThreeDeeObject> objects() {
		return objects;
	}

    public int getMode(){
        return mode;
    }

    public Player getPlayer(){
        return p;
    }

    public List<Ghost> getGhosts(){
        return ghosts;
    }
}
