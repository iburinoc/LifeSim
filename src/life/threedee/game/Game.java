package life.threedee.game;

import static life.threedee.game.GameUtilities.BLINKY;
import static life.threedee.game.GameUtilities.PINKY;
import static life.threedee.game.GameUtilities.INKY;
import static life.threedee.game.GameUtilities.CLYDE;
import static life.threedee.game.GameUtilities.SCARED;
import static life.threedee.game.GameUtilities.SCARED_FLASHING;
import static life.threedee.game.GameUtilities.CRUISE_ELROY;
import static life.threedee.game.GameUtilities.CRUISE_ELROY_2;
import static life.threedee.game.GameUtilities.EATEN;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import life.threedee.Point;
import life.threedee.TexturedPlane;
import life.threedee.ThreeDeeObject;
import life.threedee.Vector;
import life.threedee.game.maps.GameMap;
import life.threedee.game.maps.MapBuilder;
import life.threedee.game.maps.MapLocation;

public class Game implements Runnable, Tickable{
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
	
	private boolean running, first = false, second = false;

    private int mode, level, pelletsEaten, score, lives = 2;
	
    private Object objLock;
    
	public Game() {
		j = new JFrame("Game");
		
		m = new GameMap();
		
		p = new Player(this, m);
		
		objLock = new Object();
		
		setObjects(new ArrayList<ThreeDeeObject>());
        setTickables(new ArrayList<Tickable>());
		mode = 1;
        ghosts = new ArrayList<Ghost>();
        ghosts.add(new Ghost(this, BLINKY));
        ghosts.add(new Ghost(this, PINKY));
        ghosts.add(new Ghost(this, INKY));
        ghosts.add(new Ghost(this, CLYDE));
        /* CRUISE ELROY SUMMONING RITUAL. REMOVE LATER*/
        //ghosts.add(new Ghost(this, CRUISE_ELROY));
        //ghosts.add(new Ghost(this, CRUISE_ELROY_2));
        if (false) {
            ghosts.add(new Ghost(this, SCARED));
            ghosts.add(new Ghost(this, SCARED_FLASHING));
            ghosts.add(new Ghost(this, EATEN));
        }
		
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
		tickables.add(this);
	}
	
	private void removeCursor() {
		Toolkit tk= p.getToolkit();
		Cursor transparent = tk.createCustomCursor(tk.getImage(""), new java.awt.Point(), "trans");
		j.setCursor(transparent);
	}
	
	private void betaObjects() {
		objects.addAll(MapBuilder.generateBetaScreenShotObjects());
		Pellet p = new Pellet(new Point(0.5, 0, 3.5));
		objects.add(p);
		tickables.add(p);
		Energizer e = new Energizer(new Point(-0.5, 0.5, 3.5));
		objects.add(e);
		tickables.add(e);
	}
	
	private void tickablePellets() {
		for(ThreeDeeObject o : m.getObjects()) {
			if(o instanceof Pellet) {
				tickables.add((Pellet)o);
			}
		}
	}
	
	@Override
	public void run() {
		betaObjects();
		removeCursor();
		
		tickablePellets();
		
		RenderThread rt = new RenderThread();
		TickThread tt = new TickThread();
		rt.start();
		tt.start();
		
	}

	private class RenderThread extends Thread {
		@Override
		public void run() {
			long frame_time = System.currentTimeMillis();
			int frame_delta = 0;
			
			int frameRateMillis = 1000/FRAME_RATE;
			while(running) {
				long frameT = System.currentTimeMillis();
				frame_delta += (int) (frameT - frame_time);
				frame_time = frameT;
				if(frame_delta >= frameRateMillis) {
					drawFrame();
					frame_delta -= frameRateMillis;
				}
			}
		}
	}
	
	private class TickThread extends Thread {
		@Override
		public void run() {
			long tick_time = System.currentTimeMillis();
			int tick_delta = 0;

			int tickRateMillis = 1000/TICK_RATE;
			while(running) {
				long tickT = System.currentTimeMillis();
				tick_delta += (int) (tickT - tick_time);
				tick_time = tickT;
				if(tick_delta >= tickRateMillis) {
					tickTickables(tick_delta);
					tick_delta -= tickRateMillis;
				}
			}
		}
	}
	
    @Override
    public void tick(){
        if (pelletsEaten == 240){
            for(Pellet pellet : m.pelletsList()) {
                pellet.spawn();
            }
            level++;
            pelletsEaten = 0;
            die();
        }
        if (score >= 10000 && !first){
            first = true;
            lives++;
        }
        if (score >= 100000 && !second){
            second = true;
            lives++;
        }
        Point loc = p.getLoc();
        MapLocation coords = new MapLocation(loc.x, loc.z);
        for (Ghost ghost : ghosts){
            Point ghostLoc = ghost.getLocation();
            MapLocation ghostCoords = new MapLocation(ghostLoc.x, ghostLoc.z);
            if (coords.equals(ghostCoords)) {
                if (mode != -1){
                    lives--;
                    die();
                } else {
                    ghost.getAte();
                }
            }
        }
        for (Pellet food : m.pelletsList()){
            Point foodLoc = food.getCenter();
            MapLocation foodCoords = new MapLocation(foodLoc.x, foodLoc.z);
            if (coords.equals(foodCoords) && !food.getEaten()) {
                pelletsEaten++;
                food.eat();
                p.stop();
            }
        }
    }
	
	private void drawFrame() {
		long startT = System.currentTimeMillis();
		synchronized(objLock) {
			p.calcBuffer();
		}
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
	
	private void tickTickables(int delta){
		synchronized(objLock) {
			for(int i = 0; i < tickables.size(); i++){
				try{
					tickables.get(i).tick();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addTickable(Tickable t){
		tickables.add(t);
	}
	
	public void setTickables(List<Tickable> l){
		tickables = Collections.synchronizedList(l);
	}
	
	public List<Tickable> tickables(){
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
    
    public int getLevel(){
        return level;
    }

    public int getMode(){
        return mode;
    }

    public int getPelletsEaten(){
        return pelletsEaten;
    }

    public int getPelletsRemaining(){
        return 240 - getPelletsEaten();
    }

    public Player getPlayer(){
        return p;
    }

    public List<Ghost> getGhosts(){
        return ghosts;
    }
    
    public void die() {
        p.setLoc(new Point(0, 1, -8.5));
        p.setDir(new Vector(-1, 0, 0));
        for(Ghost ghost : ghosts) {
            ghost.reset();
        }
    }
}
